package com.skc.springsecurity.controller;

import com.skc.springsecurity.account.Account;
import com.skc.springsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String signUpForm(Model model) {
        model.addAttribute("account", new Account());

        return "signup";
    }

    @PostMapping
    public String processSignUp(@ModelAttribute Account account) {
        account.setRole("USER");
        accountService.createNew(account);

        return "redirect:/";
    }

}
