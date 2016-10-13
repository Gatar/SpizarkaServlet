package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/spizarka")
public class InputDataController {

    @Autowired
    DataService dataService;

    /**
     * Save new/update existing item in database.
     * Attention!! NEW item MUST be add always BEFORE add connected with it barcode.
     * @param itemDTO received
     * @return
     */
    @RequestMapping(value = "/{user}/saveItem", method = RequestMethod.POST)
    public HttpStatus saveItem(@PathVariable String username, @RequestBody ItemDTO itemDTO){
        dataService.saveItem(itemDTO, username);
        return HttpStatus.OK;
    }

    /**
     * Save new/update existing barcode in database.
     * Attention!! Barcode binded with NEW item MUST be add always AFTER this item. Otherwise we have error beacause item with connected id doesn't exist.
     * @param barcodeDTO received
     * @return
     */
    @RequestMapping(value = "/{user}/saveBarcode",method = RequestMethod.POST)
    public HttpStatus saveBarcode(@PathVariable String username, @RequestBody BarcodeDTO barcodeDTO){
        dataService.saveBarcode(barcodeDTO, username);
        return HttpStatus.OK;
    }

}
