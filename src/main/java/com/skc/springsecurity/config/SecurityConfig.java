package com.skc.springsecurity.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/custom/login").permitAll()
        ;
        http.httpBasic();

        http.logout()
                .logoutUrl("/custom/logout").permitAll()
                .logoutSuccessUrl("/")
        ;

        http.sessionManagement()
                .sessionFixation()
                    .changeSessionId();
//                .maximumSessions(1)
//                    .expiredUrl("/") // 세션이 만료되었을 때 리다이렉트 시킬 url
//                    .maxSessionsPreventsLogin(true); //false로 되어있으면 기존 세션이 있을 때 로그인 허용 안함.

        // REST api를 다룰 때 사용. formLogin방식에는 부적
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // TODO ExceptionTranslatorFilter -> FilterSecurityInterceptor (AccessDecisionManager, AffirmativeBased)
        // TODO AuthenticationException -> AuthenticationEntryPoint: 해당 유저를 로그인할 수 있게끔 인증이 가능한 페이지로 보낸다.
        // TODO AccessDeniedException -> AccessDeniedHandler: 기본은 403 에러 페이지를 보여주는 것.

        //Access Denied Page로 이동시킴
//        http.exceptionHandling()
//                .accessDeniedPage("/access-denied");

        // 조금 더 추가적인 작업을 할 수 있음.
        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() { // 실제로는 Class로 구현하여 bean으로 등록하여 사용
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        String username = principal.getUsername();
                        // 실제로는 Logger를 사용
                        System.out.println(username + " is denied to access " + request.getRequestURI());
                        response.sendRedirect("/access-denied");
                    }
                });

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}
