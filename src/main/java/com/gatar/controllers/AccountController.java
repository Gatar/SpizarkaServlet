package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import com.gatar.services.AccountServiceImpl;
import com.gatar.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LongSummaryStatistics;

@RestController
public class AccountController {

    @Autowired
    AccountServiceImpl accountServiceImpl;

    @Autowired
    EmailServiceImpl emailServiceImpl;

    /**
     * Add new Account with data like in AccountDTO: username, password and email
     * @param accountDTO new Account data
     * @return ResponseEntity containing: HttpStatus.CREATED after put data in database, HttpStatus.NOT_ACCEPTABLE if account with this username already exist
     */
    @RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
    public ResponseEntity<Void> addNewAccount(@RequestBody AccountDTO accountDTO){
        AccountServiceImpl.AccountFeedback responseAccountService = accountServiceImpl.saveAccount(accountDTO);
        HttpStatus responseHttpStatus = (responseAccountService.equals(AccountServiceImpl.AccountFeedback.AccountCreated)) ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE;

        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Get version of data saved in servlet database.
     * Database version is used for ensure the same data in every single device connected with Account.
     * @param username name of user needed to connect with right Account
     * @return ResponseEntity containing: Long version of Account's database and -1L if Account doesn't exist. HttpStatus.OK when everything was OK, HttpStatus.BAD_REQUEST if account doesn't exist
     */
    @RequestMapping(value = "/{username}/getDataVersion", method = RequestMethod.GET)
    public ResponseEntity<Long> getDataVersion(@PathVariable String username){
        Long databaseVersion = accountServiceImpl.getDataVersion(username);
        HttpStatus responseHttpStatus = (databaseVersion.equals(-1L)) ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<Long>(databaseVersion,responseHttpStatus);
    }

    /**
     * Save new version of data to servlet database. Version should be increased by 1 after EVERY modification in phone.
     * Database version is used for ensure the same data in every single device connected with Account.
     * @param username name of user needed to connect with right Account
     * @param databaseVersion new version of database
     * @return ResponseEntity containing: HttpStatus.OK - if version is one higher than version in database (ex. new 3453, old 3452), HttpStatus.NOT_ACCEPTABLE if version difference is other
     *
     * @deprecated pass data version with single item data in {@link com.gatar.domain.EntityDTO} object
     */
    @Deprecated
    @RequestMapping(value = "/{username}/putDataVersion", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Void> putDataVersion(@PathVariable String username, @RequestBody String databaseVersion){
        AccountServiceImpl.AccountFeedback responseAccountSerivce = accountServiceImpl.putDataVersion(Long.parseLong(databaseVersion),username);
        HttpStatus responseHttpStatus = (responseAccountSerivce.equals(AccountServiceImpl.AccountFeedback.AccountDatabaseNumberActualized))? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Send an email with Account data saved in database. Password is randomly reset to 6-digit string.
     * If user didn't connect any email with his account email naturally won't be send and access will be permanently lost.
     * This functionality are NOT secured.
     * @param username name of user needed to connect with right Account
     */
    @RequestMapping(value = "/{username}/rememberAccountData")
    public void sendEmailWithResetedPassword(@PathVariable String username){
        emailServiceImpl.sendAccountDataRemember(username);
    }

    /**
     * Change account password, for new one (for example after it reset to random value).
     * @param username name of user needed to connect with right Account
     * @param newPassword new password as String
     * @return ResponseEntity containing: HttpStatus.OK - password changed, HttpStatus.NOT_ACCEPTABLE Account doesn't exist
     */
    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestBody String newPassword){
        AccountServiceImpl.AccountFeedback responseAccountSerivce = accountServiceImpl.changePassword(newPassword,username);
        HttpStatus responseHttpStatus = (responseAccountSerivce.equals(AccountServiceImpl.AccountFeedback.AccountPasswordActualized))? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;

        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Permanent delete Account from database.
     * @param username name of user needed to connect with right Account
     */
    @RequestMapping(value = "/{username}/delete")
    public void deleteAccount(@PathVariable String username){
        accountServiceImpl.deleteAccount(username);
    }
}
