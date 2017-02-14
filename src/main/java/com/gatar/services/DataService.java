package com.gatar.services;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.EntityDTO;
import com.gatar.domain.Item;
import com.gatar.domain.ItemDTO;

import java.util.List;

public interface DataService {

    /**
     * Save new/update entity (item with barcodes).
     * @param entityDTO item with barcodes object
     * @param username to bind Barcode with Account
     * @return SaveFeedback response
     */
    SaveFeedback saveEntity(EntityDTO entityDTO, String username);

    /**
     * Get all Barcodes from database, for Account described by username.
     * @param username specifying Account for Barcodes extract
     * @return list of all Barcodes connected with one Account
     */
    List<BarcodeDTO> getAllBarcodes(String username);

    /**
     * Get all Items from database, for Account described by username.
     * @param username specifying Account for Items extract
     * @return list of all Items connected with one Account
     */
    List<ItemDTO> getAllItems(String username);


    /**
     * Get list of all items with quantity lower than minimum quantity.
     * @param username specifying Account
     * @return list of Items with quantity lower than minimum
     */
    List<Item> getShoppingList(String username);


    /**
     * Possible return info about saving items and barcodes to database or adding new account.
     */
    enum SaveFeedback {
        AddedNewItem,
        UpdatedExistingItem,
        AddedNewBarcode,
        BarcodeAlreadyExist,
        ItemForBarcodeNotExist,
        EntityAddedOrUpdated,
        EntityAddingFail
    }
}
