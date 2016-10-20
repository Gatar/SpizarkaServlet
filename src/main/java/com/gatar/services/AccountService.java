package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for account access, adding new accounts etc.
 */
@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;

    /**
     * Get single account from database, based on username.
     * @param username
     * @return
     */
    public Account getAccount(String username){
        return accountDAO.findByUsername(username);
    }


    /**
     * Actualizing version of database for received account.
     * @param dataVersion new version of database
     * @param username to reach correct account
     * @return AccountFeedback.AccountDatabaseNumberActualized - database number correctly actualized, AccountFeedback.AccountDatabaseNumberIncorrect - incorrect number (must be 1 higher than previous)
     */
    public AccountFeedback actualizeDataVersion(Long dataVersion, String username){
        Account account = getAccount(username);
        if(isVersionCorrect(dataVersion, account)){
            account.setDataVersion(dataVersion);
            accountDAO.save(account);
            return AccountFeedback.AccountDatabaseNumberActualized;
        }else{
            return AccountFeedback.AccountDatabaseNumberIncorrect;
        }
    }

    /**
     * Creating new account in database.
     * @param accountDTO new account data
     * @return AccountFeedback.AccountCreated - account succesufly created, AccountFeedback.AccountAlreadyExist - account with this username exist
     */
    public AccountFeedback saveAccount(AccountDTO accountDTO){
        if(!isAccountExist(accountDTO.getUsername())) {
            Account account = accountDTO.toAccount();
            accountDAO.save(account);
            return AccountFeedback.AccountCreated;
        }else {
            return AccountFeedback.AccountAlreadyExist;
        }
    }

    private boolean isVersionCorrect(Long dataVersion, Account account){
        return (account.getDataVersion() == dataVersion - 1);
    }

    public boolean isAccountExist(String username){
        Optional<Account> receivedAccount = Optional.ofNullable(accountDAO.findByUsername(username));
        return receivedAccount.isPresent();
    }

    public void deleteAccount(String username){
        Optional<Account> receivedAccount = Optional.ofNullable(accountDAO.findByUsername(username));
        if(receivedAccount.isPresent()) accountDAO.delete(receivedAccount.get());
    }

    /**
     * Possible return info about saving database version or adding new account.
     */
    public enum AccountFeedback{
        AccountCreated,
        AccountAlreadyExist,
        AccountDatabaseNumberActualized,
        AccountDatabaseNumberIncorrect
    }
}
