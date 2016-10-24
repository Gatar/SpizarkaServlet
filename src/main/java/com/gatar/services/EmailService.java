package com.gatar.services;


import org.springframework.http.HttpStatus;

interface EmailService {

    /**
     * Sending shopping list (items with quantity below minimum).
     * @param recipietEmail email on which will be send shopping list.
     * @param username username of list
     * @return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    HttpStatus sendShoppingListEmail(String recipietEmail, String username);


    /**
     * Sending Account data to email, which is binded to Account.
     * @param username username for reset password
     * @return return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    HttpStatus sendAccountDataRemember(String username);


    //-------------------E-Mail Account settings-------------------------
    String SENDING_FROM = "gatar.webserivces@gmail.com";
    String EMAIL_ACCOUNT_LOGIN = "gatar.webserivces@gmail.com";
    String EMAIL_ACCOUNT_PASSWORD = "creative!#";
    String HOST = "smtp.gmail.com";

    int PORT = 465;
    boolean AUTHENTICATION = true;
    EmailProtocolType EMAIL_PROTOCOL_TYPE = EmailProtocolType.SMTPS;
    boolean DEBUG = true;

    enum EmailProtocolType {
        SMTPS,
        TLS
    }


}
