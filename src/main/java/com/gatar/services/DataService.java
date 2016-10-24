package com.gatar.services;

import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.Item;
import com.gatar.domain.ItemDTO;

import java.util.List;

public interface DataService {

    /**
     * Save new/update existing item in database. Searching first in database if there exist item.
     * @param itemDTO item data
     * @param username to bind Item with Account
     * @return SaveFeedback response
     */
    SaveFeedback saveItem(ItemDTO itemDTO, String username);

    /**
     * Save new barcode. If existing barcode will be find nothing should be saved (to prevent duplication).
     * @param barcodeDTO barcode data
     * @param username to bind Barcode with Account
     * @return SaveFeedback response
     */
    SaveFeedback saveBarcode(BarcodeDTO barcodeDTO, String username);


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
        ItemForBarcodeNotExist
    }
}
