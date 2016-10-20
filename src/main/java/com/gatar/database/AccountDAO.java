package com.gatar.database;

import com.gatar.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountDAO extends CrudRepository <Account, Long> {
    Account findByUsername(String username);
    List<Account> findAll();
}
