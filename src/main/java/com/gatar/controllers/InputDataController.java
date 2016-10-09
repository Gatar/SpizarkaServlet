package com.gatar.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatar.domain.Barcode;
import com.gatar.domain.Item;
import com.gatar.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/spizarka")
public class InputDataController {

    @Autowired
    DatabaseService databaseService;

    /**
     * Save new/update existing item in database.
     * Attention!! NEW item MUST be add always BEFORE add connected with it barcode.
     * @param item received
     * @return
     */
    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    public HttpStatus saveItem(@RequestBody Item item){
        databaseService.saveItem(item);
        return HttpStatus.OK;
    }

    /**
     * Save new/update existing barcode in database.
     * Attention!! Barcode binded with NEW item MUST be add always AFTER this item. Otherwise we have error beacause item with connected id doesn't exist.
     * @param barcode received
     * @return
     */
    @RequestMapping(value = "/saveBarcode",method = RequestMethod.POST)
    public HttpStatus saveBarcode(@RequestBody Barcode barcode){
        databaseService.saveBarcode(barcode);
        return HttpStatus.OK;
    }

}
