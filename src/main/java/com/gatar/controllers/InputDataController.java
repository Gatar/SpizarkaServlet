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
