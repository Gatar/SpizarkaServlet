package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class ensuring service of main databases BarcodeDAO and ItemDAO. They are often used together so it's one service for handling them..
 */
@Service
public class DataService {

    @Autowired
    BarcodeDAO barcodeDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    AccountService accountService;

    /**
     * Save new/update existing item in database. Searching first in database if there exist item.
     * @param itemDTO item data
     * @param username
     * @return SaveFeedback.UpdatedExistingItem - item exist before in database, has been updated, SaveFeedback.AddedNewItem - new item added to database
     */
    public SaveFeedback saveItem(ItemDTO itemDTO, String username){
        SaveFeedback returnState = SaveFeedback.AddedNewItem;
        Account account = accountService.getAccount(username);
        Optional<Item> itemFromDatabase = Optional.ofNullable(itemDAO.findByIdItemAndroidAndAccount(itemDTO.getIdItemAndroid(),account));

        Item item = itemDTO.toItem();
        item.setAccount(accountService.getAccount(username));

        if(itemFromDatabase.isPresent()) {
            item.setIdItem(itemFromDatabase.get().getIdItem());
            returnState = SaveFeedback.UpdatedExistingItem;
        }
        itemDAO.save(item);
        return  returnState;
    }

    /**
     * Save new barcode. If existing barcode will be find nothing will be saved (to prevent duplication).
     * @param barcodeDTO barcode data
     * @param username
     * @return SaveFeedback.ItemForBarcodeNotExist - item describen in Barcode doesn't exist in database, SaveFeedback.BarcodeAlreadyExist - there are the same barcode for this item in database, SaveFeedback.AddedNewBarcode - barcode added correctly
     */
    public SaveFeedback saveBarcode(BarcodeDTO barcodeDTO, String username){
        Account account = accountService.getAccount(username);

        //Check for presence of item
        Optional<Item> itemFromDatabase = Optional.ofNullable(itemDAO.findByIdItemAndroidAndAccount(barcodeDTO.getIdItemAndroid(),account));
        if(!itemFromDatabase.isPresent()) return SaveFeedback.ItemForBarcodeNotExist;

        //Check for presence of barcode value
        List<Barcode> barcodesFromItem = itemFromDatabase.get().getBarcodes();
        for(Barcode barcode : barcodesFromItem) {
            if(barcode.getBarcodeValue().equals(barcodeDTO.getBarcodeValue())) return SaveFeedback.BarcodeAlreadyExist;
        }

        Barcode barcode = barcodeDTO.toBarcode();
        barcode.setItem(itemDAO.findByIdItemAndroidAndAccount(barcodeDTO.getIdItemAndroid(), account));
        barcodeDAO.save(barcode);
        return SaveFeedback.AddedNewBarcode;
    }

    /**
     * Get list of all barcodes SENDING_FROM database, for one account.
     * @return barcodeDTO object
     */
    public List<BarcodeDTO> getAllBarcodes(String username){
        List<Item> items = getItemListByUser(username);
        List<BarcodeDTO> barcodeDTOs = new LinkedList<>();

        for(Item item : items){
            for(Barcode barcode : item.getBarcodes()){
                BarcodeDTO actualBarcodeDTO = new BarcodeDTO();
                actualBarcodeDTO.setBarcodeValue(barcode.getBarcodeValue());
                actualBarcodeDTO.setIdItemAndroid(item.getIdItemAndroid());

                barcodeDTOs.add(actualBarcodeDTO);
            }
        }

        return barcodeDTOs;
    }

    /**
     * Get list of all items SENDING_FROM database, for one account.
     * @return itemDTO object
     */
    public List<ItemDTO> getAllItems(String username){
        List<Item> items = getItemListByUser(username);
        List<ItemDTO> itemDTOs = items.stream()
                .map(x -> x.toItemDTO())
                .collect(Collectors.toList());

        return itemDTOs;
    }

    /**
     * Get list of all items with quantity lower than minimum.
     * @return item object
     */
    public List<Item> getShoppingList(String username){
        List<Item> list = getItemListByUser(username);
        list = list.stream()
                .filter(s -> (s.getQuantity() < s.getMinimumQuantity()))
                .sorted((o1, o2) -> o1.getCategory().compareToIgnoreCase(o2.getCategory()))
                .collect(Collectors.toList());

        return list;
    }

    private List<Item> getItemListByUser(String username){
        Account account = accountService.getAccount(username);
        return itemDAO.findByAccount(account);
    }

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
