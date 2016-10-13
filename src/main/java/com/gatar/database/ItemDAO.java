package com.gatar.database;

import com.gatar.domain.Account;
import com.gatar.domain.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDAO extends CrudRepository <Item, Long> {
    List<Item> findAll();
    Item findByIdItemAndroidAndAccount(Long idItemAndroid, Account account);
    List<Item> findByAccount(Account account);
}
