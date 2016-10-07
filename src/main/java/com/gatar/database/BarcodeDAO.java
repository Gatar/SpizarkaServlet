package com.gatar.database;

import com.gatar.domain.Barcode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarcodeDAO extends CrudRepository <Barcode,String> {
}
