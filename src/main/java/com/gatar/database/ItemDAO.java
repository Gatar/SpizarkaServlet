package com.gatar.database;

import com.gatar.domain.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDAO extends CrudRepository <Item, String> {
}
