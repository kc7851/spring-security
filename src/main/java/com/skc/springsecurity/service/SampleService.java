package com.skc.springsecurity.service;

import com.skc.springsecurity.account.Account;
import com.skc.springsecurity.account.AccountContext;
import com.skc.springsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
        Account account = AccountContext.getAccount();
        System.out.println("===================");
        System.out.println(account.getId());
        System.out.println("===================");
    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async Service is called.");
    }

}
