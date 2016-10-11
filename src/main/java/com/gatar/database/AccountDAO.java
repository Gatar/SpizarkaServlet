package com.gatar.database;

import com.gatar.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDAO extends CrudRepository <Account, Long> {

}
