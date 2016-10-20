package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataService;
import com.gatar.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OutputDataController {

    @Autowired
    DataService dataService;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/{username}/getAllItems", method = RequestMethod.GET)
    public List<ItemDTO> getAllItems(@PathVariable String username){
        return dataService.getAllItems(username);
    }

    @RequestMapping(value = "/{username}/getAllBarcodes", method = RequestMethod.GET)
    public List<BarcodeDTO> getAllBarcodes(@PathVariable String username){
        return dataService.getAllBarcodes(username);
    }

    @RequestMapping(value = "/{username}/getShopping/{email}", method = RequestMethod.HEAD)
    public HttpStatus sendShopingListtoEmail(@PathVariable String username, String recipietEmail){
        return emailService.sendShoppingListEmail(recipietEmail,username);
    }


}
