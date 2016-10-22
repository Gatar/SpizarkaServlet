package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import com.gatar.services.AccountService;
import com.gatar.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    EmailService emailService;

    /**
     * Add new Account with data like in AccountDTO: username, password and email
     * @param accountDTO new Account data
     * @return HttpStatus.CREATED after put data in database, HttpStatus.NOT_ACCEPTABLE if account with this username already exist
     */
    @RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
    public ResponseEntity<Void> addNewAccount(@RequestBody AccountDTO accountDTO){
        AccountService.AccountFeedback responseAccountService = accountService.saveAccount(accountDTO);
        HttpStatus responseHttpStatus = (responseAccountService.equals(AccountService.AccountFeedback.AccountCreated)) ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE;

        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Get version of data saved in servlet database.
     * @param username name of user needed to connect with right Account
     * @return version of database for the username Account
     */
    @RequestMapping(value = "/{username}/getDataVersion", method = RequestMethod.GET)
    public ResponseEntity<Long> getDataVersion(@PathVariable String username){
        Long databaseVersion = accountService.getDataVersion(username);
        HttpStatus responseHttpStatus = (databaseVersion.equals(-1L)) ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<Long>(databaseVersion,responseHttpStatus);
    }

    /**
     * Save new version of data to servlet database.
     * @param username name of user needed to connect with right Account
     * @param databaseVersion new version of database after some modification in phone
     * @return HttpStatus.OK - if version is one higher than version in database (ex. new 3453, old 3452), HttpStatus.NOT_ACCEPTABLE if version difference is other
     */
    @RequestMapping(value = "/{username}/putDataVersion", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Void> putDataVersion(@PathVariable String username, @RequestBody Long databaseVersion){
        AccountService.AccountFeedback responseAccountSerivce = accountService.putDataVersion(databaseVersion,username);
        HttpStatus responseHttpStatus = (responseAccountSerivce.equals(AccountService.AccountFeedback.AccountDatabaseNumberActualized))? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;

        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Send an email with Account data saved in database. Password is randomly reset to 6-digit string. If user didn't connect any email with his account email naturally won't be send.
     * @param username name of user needed to connect with right Account
     */
    @RequestMapping(value = "/{username}/rememberAccountData")
    public void sendEmailWithResetedPassword(@PathVariable String username){
        emailService.sendAccountDataRemember(username);
    }

    /**
     * Change account password, for example if user reset password to random by sending an email.
     * @param username name of user needed to connect with right Account
     * @param newPassword new password as String
     * @return HttpStatus.OK - password changed, HttpStatus.NOT_ACCEPTABLE Account doesn't exist
     */
    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestBody String newPassword){
        AccountService.AccountFeedback responseAccountSerivce = accountService.changePassword(newPassword,username);
        HttpStatus responseHttpStatus = (responseAccountSerivce.equals(AccountService.AccountFeedback.AccountPasswordActualized))? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;

        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Permanent delete Account from database.
     * @param username name of user needed to connect with right Account
     */
    @RequestMapping(value = "/{username}/delete")
    public void deleteAccount(@PathVariable String username){
        accountService.deleteAccount(username);
    }
}
