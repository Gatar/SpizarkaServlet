package com.gatar.controllers;

import com.gatar.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InputDataController {

    @Autowired
    DatabaseService databaseService;
}
