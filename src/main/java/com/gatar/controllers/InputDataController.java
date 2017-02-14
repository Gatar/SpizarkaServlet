package com.gatar.controllers;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.EntityDTO;
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
     * @param itemDTO DTO object with new/update Item data
     * @param username for specifying Account
     * @return ResponseEntity containing: HttpStatus.CREATED after create new Item in database, HttpStatus.ACCEPTED after update Item data
     *
     * @deprecated use saveEntity from this class (contains both barcodes and item data)
     * */
    @Deprecated
    @RequestMapping(value = "/{username}/saveItem", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(@PathVariable String username, @RequestBody ItemDTO itemDTO){
        DataService.SaveFeedback responseDataService = dataServiceImpl.saveItem(itemDTO, username);
        HttpStatus responseHttpStatus = (responseDataService.equals(DataService.SaveFeedback.AddedNewItem)) ? HttpStatus.CREATED : HttpStatus.ACCEPTED;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Save new/update existing barcode in database.
     * Attention!! Barcode binded with NEW item MUST be add always AFTER this item. Otherwise we have error beause item with connected id doesn't exist.
     * @param barcodeDTO DTO object with new Barcode data
     * @param username for specifying Account
     * @return ResponseEntity containing: HttpStatus.OK when everything was OK, HttpStatus.CONFLICT if there were Exceptions in email sending method and HttpStatus.NOT_ACCEPTABLE when Account doesn't exist or when list is empty
     *
     * @deprecated use saveEntity from this class (contains both barcodes and item data)
     */
    @Deprecated
    @RequestMapping(value = "/{username}/saveBarcode",method = RequestMethod.POST)
    public ResponseEntity<Void> saveBarcode(@PathVariable String username, @RequestBody BarcodeDTO barcodeDTO){
        DataService.SaveFeedback responseDataService = dataServiceImpl.saveBarcode(barcodeDTO, username);
        HttpStatus responseHttpStatus = (responseDataService.equals(DataService.SaveFeedback.AddedNewBarcode)) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

    /**
     * Save new/update existing entity, which means item with its all barcodes
     * @param username for specifying Account
     * @param entityDTO DTO object with Item and Barcodes data
     * @return ResponseEntity containing: HttpStatus.OK when everything was OK, HttpStatus.CONFLICT if entity wasn't saved correctly
     */
    @RequestMapping(value="/{username}/saveEntity", method = RequestMethod.POST)
    public ResponseEntity<Void> saveEntity(@PathVariable String username, @RequestBody EntityDTO entityDTO){
        DataService.SaveFeedback responseDataService = dataServiceImpl.saveEntity(entityDTO,username);
        HttpStatus responseHttpStatus = (responseDataService.equals(DataService.SaveFeedback.EntityAddedOrUpdated)) ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        return new ResponseEntity<Void>(responseHttpStatus);
    }

}
