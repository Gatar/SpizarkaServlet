package com.gatar.database;

import com.gatar.domain.Barcode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeDAO extends CrudRepository <Barcode,String> {
    List<Barcode> findAll();
}
