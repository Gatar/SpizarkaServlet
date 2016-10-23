package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataServiceImpl;
import com.gatar.services.EmailServiceImpl;
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
    DataServiceImpl dataServiceImpl;

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @RequestMapping(value = "/{username}/getAllItems", method = RequestMethod.GET)
    public List<ItemDTO> getAllItems(@PathVariable String username){
        return dataServiceImpl.getAllItems(username);
    }

    @RequestMapping(value = "/{username}/getAllBarcodes", method = RequestMethod.GET)
    public List<BarcodeDTO> getAllBarcodes(@PathVariable String username){
        return dataServiceImpl.getAllBarcodes(username);
    }

    @RequestMapping(value = "/{username}/getShopping/{email}", method = RequestMethod.HEAD)
    public HttpStatus sendShopingListToEmail(@PathVariable String username, String recipietEmail){
        return emailServiceImpl.sendShoppingListEmail(recipietEmail,username);
    }


}
