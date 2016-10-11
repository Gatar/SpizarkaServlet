package com.gatar.controllers;

import com.gatar.domain.Barcode;
import com.gatar.domain.Item;
import com.gatar.services.DatabaseService;
import com.gatar.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/spizarka")
public class OutputDataController {

    @Autowired
    DatabaseService databaseService;

    @RequestMapping("/getAllItems")
    public List<Item> getAllItems(){
        return databaseService.getAllItems();
    }

    @RequestMapping("/getAllBarcodes")
    public List<Barcode> getAllBarcodes(){
        return databaseService.getAllBarcodes();
    }

    @RequestMapping("/getShopping")
    public List<Item> getShoppingList(){
        return databaseService.getShoppingList();
    }


}
