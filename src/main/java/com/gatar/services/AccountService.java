package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;

    public Account getAccount(String username){
        return accountDAO.findByUsername(username);
    }

    public boolean putDataVersion(Long dataVersion, String username){
        Account account = getAccount(username);
        if(isVersionCorrect(dataVersion, account)){
            account.setDataVersion(dataVersion);
            accountDAO.save(account);
            return true;
        }else{
            return false;
        }
    }

    public void saveAccount(AccountDTO accountDTO){
        Account account = accountDTO.toAccount();
        accountDAO.save(account);
    }

    private boolean isVersionCorrect(Long dataVersion, Account account){
        return (account.getDataVersion() == dataVersion - 1);
    }
}
