package com.skc.springsecurity.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithAnonymousUser
    public void index_anonymous() throws Exception{
        mvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    public void index_user() throws Exception{
        mvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "skc", roles = "USER")
    public void admin_user() throws Exception{
        mvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void admin_admin() throws Exception{
        mvc.perform(get("/admin")
        .with(user("skc").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

}