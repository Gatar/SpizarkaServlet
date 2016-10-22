package com.gatar.database;

import com.gatar.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends CrudRepository <Account, Long> {
    Account findByUsername(String username);
    List<Account> findAll();
}
