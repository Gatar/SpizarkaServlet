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
     * @return true - actualize was correct, false - version of new database is incorrect (MUST be 1 higher than previous)
     */
    public boolean actualizeDataVersion(Long dataVersion, String username){
        Account account = getAccount(username);
        if(isVersionCorrect(dataVersion, account)){
            account.setDataVersion(dataVersion);
            accountDAO.save(account);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Creating new account in database.
     * @param accountDTO new account data
     * @return true - account is created, false - account exists before, it cannot be created one more time
     */
    public boolean saveAccount(AccountDTO accountDTO){
        if(!isAccountExist(accountDTO.getUsername())) {
            Account account = accountDTO.toAccount();
            accountDAO.save(account);
            return true;
        }else {
            return false;
        }
    }

    private boolean isVersionCorrect(Long dataVersion, Account account){
        return (account.getDataVersion() == dataVersion - 1);
    }

    public boolean isAccountExist(String username){
        Optional<Account> receivedAccount = Optional.of(accountDAO.findByUsername(username));
        return receivedAccount.isPresent();
    }
}
