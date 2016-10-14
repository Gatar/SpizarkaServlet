package com.gatar.services;

interface EmailServiceSettings {
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
