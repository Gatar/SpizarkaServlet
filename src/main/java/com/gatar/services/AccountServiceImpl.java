package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for account access, adding new accounts etc.
 */
@Service
public class AccountServiceImpl implements AccountService {

    final Long INITIAL_DATABASE_VERSION = 1L;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Account getAccount(String username) {
        return accountDAO.findByUsername(username);
    }

    /**{@inheritDoc}
     *
     * @param dataVersion new version of database
     * @param username Account which database version should be changed username
     * @return AccountFeedback.AccountDatabaseNumberActualized - database number correctly actualized, AccountFeedback.AccountDatabaseNumberIncorrect - incorrect number (must be 1 higher than previous)
     */
    public AccountFeedback putDataVersion(Long dataVersion, String username) {
        if(!isAccountExist(username)) return AccountFeedback.AccountDoesntExist;

        Account account = getAccount(username);
        if (isVersionCorrect(dataVersion, account)) {
            account.setDataVersion(dataVersion);
            accountDAO.save(account);
            return AccountFeedback.AccountDatabaseNumberActualized;
        } else {
            return AccountFeedback.AccountDatabaseNumberIncorrect;
        }
    }

    /**
     * {@inheritDoc}
     * @param username Account username
     * @return database version for Account, if Account doesn't exist return -1.
     */
    public Long getDataVersion(String username){
        if(!isAccountExist(username)) return -1L;
        return getAccount(username).getDataVersion();
    }

    /**
     * {@inheritDoc}
     *
     * @param newPassword new password
     * @param username username of Account
     * @return AccountFeedback.AccountPasswordActualized - password actualized, AccountFeedback.AccountDoesntExist - account doesn't exist
     */
    public AccountFeedback changePassword(String newPassword, String username){
        if (isAccountExist(username)){
            Account account = getAccount(username);
            account.setPassword(passwordEncoder.encode(newPassword));
            accountDAO.save(account);
            return AccountFeedback.AccountPasswordActualized;
        } else {
            return AccountFeedback.AccountDoesntExist;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param accountDTO new account data
     * @return AccountFeedback.AccountCreated - account succesufly created, AccountFeedback.AccountAlreadyExist - account with this username exist
     */
    public AccountFeedback saveAccount(AccountDTO accountDTO) {
        if (!isAccountExist(accountDTO.getUsername())) {
            Account account = accountDTO.toAccount();
            account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            account.setDataVersion(INITIAL_DATABASE_VERSION);
            accountDAO.save(account);
            return AccountFeedback.AccountCreated;
        } else {
            return AccountFeedback.AccountAlreadyExist;
        }
    }


    public void deleteAccount(String username) {
        Optional<Account> receivedAccount = Optional.ofNullable(accountDAO.findByUsername(username));
        if (receivedAccount.isPresent()) accountDAO.delete(receivedAccount.get());
    }

    private boolean isVersionCorrect(Long dataVersion, Account account) {
        Optional<Long> accountDatabaseVersion = Optional.ofNullable(account.getDataVersion());
        if (accountDatabaseVersion.isPresent()) return (accountDatabaseVersion.get() == dataVersion - 1);
        else return true;
    }

    private boolean isAccountExist(String username) {
        Optional<Account> receivedAccount = Optional.ofNullable(accountDAO.findByUsername(username));
        return receivedAccount.isPresent();
    }

}
