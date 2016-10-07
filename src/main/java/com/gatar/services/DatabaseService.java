package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    BarcodeDAO barcodeDAO;
}
