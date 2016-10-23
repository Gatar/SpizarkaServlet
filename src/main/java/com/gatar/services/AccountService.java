package com.gatar.services;

import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;

public interface AccountService {

    /** Get single account from database, based on username.
     * @param username
     * @return Account reference from database.
     */
    public Account getAccount(String username);


    /** Actualize version of database for received account.
     *
     * @param dataVersion new version of database
     * @param username Account which database version should be changed username
     * @return AccountFeedback
     */
    public AccountFeedback putDataVersion(Long dataVersion, String username);


    /**
     * Return database version.
     * @param username Account username
     * @return database version for Account
     */
    public Long getDataVersion(String username);

    /**
     * Change Account password in database.
     *
     * @param newPassword new password
     * @param username username of Account
     * @return AccountFeedback
     */
    public AccountFeedback changePassword(String newPassword, String username);


    /**
     * Creating new account in database or actualize existing Account.
     *
     * @param accountDTO new account data
     * @return AccountFeedback
     */
    public AccountFeedback saveAccount(AccountDTO accountDTO);

    /**
     * Delete from database Account described by username.
     * @param username of Account to delete.
     */
    public void deleteAccount(String username);

    /**
     * Possible return info about saving database version or adding new account.
     */
    public enum AccountFeedback {
        AccountCreated,
        AccountAlreadyExist,
        AccountDatabaseNumberActualized,
        AccountDatabaseNumberIncorrect,
        AccountPasswordActualized,
        AccountDoesntExist
    }

}
