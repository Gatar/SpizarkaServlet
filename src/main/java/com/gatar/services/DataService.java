package com.gatar.services;

public interface DataService {

    /**
     * Possible return info about saving items and barcodes to database or adding new account.
     */
    public enum SaveFeedback {
        AddedNewItem,
        UpdatedExistingItem,
        AddedNewBarcode,
        BarcodeAlreadyExist,
        ItemForBarcodeNotExist
    }
}
