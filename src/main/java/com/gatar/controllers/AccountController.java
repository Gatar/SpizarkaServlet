package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import com.gatar.services.AccountService;
import com.gatar.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.ServletSecurity;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    EmailService emailService;

    /**
     * Get version of data saved in servlet database.
     * @param username name of user needed to connect with right Account
     * @return version of database for the username Account
     */
    @RequestMapping(value = "/{username}/getDataVersion", method = RequestMethod.GET)
    public ResponseEntity<Long> getDataVersion(@PathVariable String username){
        return new ResponseEntity<Long>(accountService.getAccount(username).getDataVersion(),HttpStatus.OK);
    }

    //TODO Fix javadocs....
    /**
     * Save new version of data to servlet database.
     * @param username name of user needed to connect with right Account
     * @param databaseVersion new version of database after some modification in phone
     * @return HttpStatus.OK - if version is one higher than version in database (ex. new 3453, old 3452), HttpStatus.NOT_ACCEPTABLE if version difference is other
     */
    @RequestMapping(value = "/{username}/putDataVersion", method = RequestMethod.POST)
    public ResponseEntity<Long> putDataVersion(@PathVariable String username, @RequestBody Long databaseVersion){
        AccountService.AccountFeedback responseAccountSerivce = accountService.actualizeDataVersion(databaseVersion,username);

        HttpStatus responseHttpStatus = (responseAccountSerivce.equals(AccountService.AccountFeedback.AccountDatabaseNumberActualized))? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
        Long actualDatabaseVersion = accountService.getAccount(username).getDataVersion();

        return new ResponseEntity<Long>(actualDatabaseVersion,responseHttpStatus);
    }

    /**
     * Add new Account with data like in AccountDTO: username, password and email
     * @param accountDTO new Account data
     * @return HttpStatus.CREATED after put data in database, HttpStatus.NOT_ACCEPTABLE if account with this username already exist
     */
    @RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
    public ResponseEntity<AccountDTO> addNewAccount(@RequestBody AccountDTO accountDTO){
        AccountService.AccountFeedback responseAccountService = accountService.saveAccount(accountDTO);

        HttpStatus responseHttpStatus = (responseAccountService.equals(AccountService.AccountFeedback.AccountCreated)) ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE;
        AccountDTO actualAccountDTO = accountService.getAccount(accountDTO.getUsername()).toAccountDTO();

        return new ResponseEntity<AccountDTO>(actualAccountDTO,responseHttpStatus);
    }

    /**
     * Send an email for email saved in database. If user didn't connect any email with his account email naturally won't be send.
     * @param username name of user needed to connect with right Account
     * @return HttpStatus.OK when everything was OK
     */
    @RequestMapping(value = "/{username}/rememberAccountData")
    public HttpStatus sendEmailWithAccountData(@PathVariable String username){
        return emailService.sendAccountDataRemember(username);
    }

    //TODO Tylko do testów, finalnie usunąć
    @RequestMapping(value = "/{username}/delete")
    private HttpStatus deleteAccount(@PathVariable String username){
        accountService.deleteAccount(username);
        return HttpStatus.OK;
    }
}
