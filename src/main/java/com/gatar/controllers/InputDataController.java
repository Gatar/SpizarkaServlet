package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.ItemDTO;
import com.gatar.services.DataService;
import com.gatar.services.DataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InputDataController {

    @Autowired
    DataServiceImpl dataServiceImpl;

    /**
     * Save new/update existing item in database.
     * Attention!! NEW item MUST be add always BEFORE add connected with it barcode.
     * @param itemDTO received
     * @return
     */
    @RequestMapping(value = "/{username}/saveItem", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(@PathVariable String username, @RequestBody ItemDTO itemDTO){
        DataService.SaveFeedback responseDataService = dataServiceImpl.saveItem(itemDTO, username);
        HttpStatus responseHttpStatus = (responseDataService.equals(DataService.SaveFeedback.AddedNewItem)) ? HttpStatus.CREATED : HttpStatus.ACCEPTED;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Save new/update existing barcode in database.
     * Attention!! Barcode binded with NEW item MUST be add always AFTER this item. Otherwise we have error beacause item with connected id doesn't exist.
     * @param barcodeDTO received
     * @return
     */
    @RequestMapping(value = "/{username}/saveBarcode",method = RequestMethod.POST)
    public ResponseEntity<Void> saveBarcode(@PathVariable String username, @RequestBody BarcodeDTO barcodeDTO){
        DataService.SaveFeedback responseDataService = dataServiceImpl.saveBarcode(barcodeDTO, username);
        HttpStatus responseHttpStatus = (responseDataService.equals(DataService.SaveFeedback.AddedNewBarcode)) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

}
