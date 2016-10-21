package com.gatar.security;

import com.gatar.domain.Account;

public interface SecurityConfigurationInterface {
    String adminName = "Gatar";
    String adminPass = "password";
    Account adminAccount = new Account(adminName,adminPass);

}
