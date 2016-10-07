package com.gatar.services;

import com.gatar.database.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    ItemDAO itemDAO;
}
