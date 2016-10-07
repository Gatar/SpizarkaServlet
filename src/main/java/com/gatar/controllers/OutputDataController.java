package com.gatar.controllers;

import com.gatar.services.DatabaseService;
import com.gatar.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutputDataController {

    @Autowired
    DatabaseService databaseService;
}
