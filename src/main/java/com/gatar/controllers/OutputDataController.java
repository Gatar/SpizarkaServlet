package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataServiceImpl;
import com.gatar.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OutputDataController {

    @Autowired
    DataServiceImpl dataServiceImpl;

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @RequestMapping(value = "/{username}/getAllItems", method = RequestMethod.GET)
    public ResponseEntity<List<ItemDTO>> getAllItems(@PathVariable String username){
        List<ItemDTO> itemsFromDatabase = dataServiceImpl.getAllItems(username);
        HttpStatus responseHttpStatus = isListEmpty(itemsFromDatabase) ? HttpStatus.CONFLICT : HttpStatus.OK;

        return  new ResponseEntity<List<ItemDTO>>(itemsFromDatabase,responseHttpStatus);
    }

    @RequestMapping(value = "/{username}/getAllBarcodes", method = RequestMethod.GET)
    public ResponseEntity<List<BarcodeDTO>> getAllBarcodes(@PathVariable String username){
        List<BarcodeDTO> barcodesFromDatabase = dataServiceImpl.getAllBarcodes(username);
        HttpStatus responseHttpStatus = isListEmpty(barcodesFromDatabase) ? HttpStatus.CONFLICT : HttpStatus.OK;

        return  new ResponseEntity<List<BarcodeDTO>>(barcodesFromDatabase,responseHttpStatus);
    }

    @RequestMapping(value = "/{username}/getShopping", method = RequestMethod.POST)
    public ResponseEntity<Void> sendShopingListToEmail(@PathVariable String username, @RequestBody String recipietEmail){
        HttpStatus responceHttpStatus = emailServiceImpl.sendShoppingListEmail(recipietEmail,username);

        return new ResponseEntity<Void>(responceHttpStatus);
    }

    private <T> boolean isListEmpty(List<T> list){
        return (list.size() == 0);
    }


}
