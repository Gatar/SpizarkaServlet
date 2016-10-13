package com.gatar.controllers;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import com.gatar.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/spizarka")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * Get version of data saved in servlet database.
     * @param username name of user needed to connect with right Account
     * @return version of database for the username Account
     */
    @RequestMapping(value = "/{username}/getDataVersion", method = RequestMethod.GET)
    public Long getDataVersion(@PathVariable String username){
        return accountService.getAccount(username).getDataVersion();
    }

    /**
     * Save new version of data to servlet database.
     * @param username name of user needed to connect with right Account
     * @param databaseVersion new version of database after some modification in phone
     * @return HttpStatus.OK - if version is one higher than version in database (ex. new 3453, old 3452), HttpStatus.NOT_ACCEPTABLE if version difference is other
     */
    @RequestMapping(value = "/{username}/putDataVersion", method = RequestMethod.POST)
    public HttpStatus putDataVersion(@PathVariable String username, @RequestBody Long databaseVersion){
        boolean isVersionCorrect = accountService.putDataVersion(databaseVersion,username);
        return (isVersionCorrect) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
    }

    /**
     * Add new Account with data like in AccountDTO: username, password and email
     * @param accountDTO new Account data
     * @return HttpStatus.CREATED after put data in database
     */
    @RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
    public HttpStatus addNewAccount(@RequestBody AccountDTO accountDTO){
        accountService.saveAccount(accountDTO);
        return HttpStatus.CREATED;
    }

    /**
     * Send an email for email saved in database. If user didn't connect any email with his account email naturally won't be send.
     * @param username name of user needed to connect with right Account
     * @return HttpStatus.OK when everything was OK, HttpStatus.NOT_ACCEPTABLE if there are no email in database
     */
    @RequestMapping(value = "/{username}/rememberAccountData")
    public HttpStatus sendEmailWithAccountData(@PathVariable String username){
        Optional<String> email = Optional.ofNullable(accountService.getAccount(username).getEmail());

        //TODO dodać funkcję wysłania maila
        return (email.isPresent())? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
    }
}
