package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.Item;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/spizarka")
public class OutputDataController {

    @Autowired
    DataService dataService;

    @RequestMapping(value = "/{username}/getAllItems", method = RequestMethod.GET)
    public List<ItemDTO> getAllItems(@PathVariable String username){
        return dataService.getAllItems(username);
    }

    @RequestMapping(value = "/{username}/getAllBarcodes", method = RequestMethod.GET)
    public List<BarcodeDTO> getAllBarcodes(@PathVariable String username){
        return dataService.getAllBarcodes(username);
    }

    @RequestMapping(value = "/{username}/getShopping", method = RequestMethod.GET)
    public List<Item> getShoppingList(@PathVariable String username){
        return dataService.getShoppingList(username);
    }


}
