package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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
     * Save new/update existing item in database.
     * @param itemDTO object
     */
    public void saveItem(ItemDTO itemDTO, String username){
        Item item = itemDTO.toItem();
        item.setAccount(accountService.getAccount(username));
        itemDAO.save(item);
    }

    /**
     * Save new/update existing barcode.
     * @param barcodeDTO object
     */
    public void saveBarcode(BarcodeDTO barcodeDTO, String username){
        Barcode barcode = barcodeDTO.toBarcode();
        Account account = accountService.getAccount(username);
        barcode.setItem(itemDAO.findByIdItemAndroidAndAccount(barcodeDTO.getIdItemAndroid(),account));

        barcodeDAO.save(barcode);
    }

    /**
     * Get list of all barcodes from database, for one account.
     * @return barcodeDTO object
     */
    public List<BarcodeDTO> getAllBarcodes(String username){
        List<Item> items = getItemListByUser(username);
        List<BarcodeDTO> barcodeDTOs = new LinkedList<>();

        for(Item item : items){
            for(Barcode barcode : item.getBarcodes()){
                BarcodeDTO actualBarcodeDTO = new BarcodeDTO();
                actualBarcodeDTO.setBarcode(barcode.getBarcode());
                actualBarcodeDTO.setIdItemAndroid(item.getIdItemAndroid());

                barcodeDTOs.add(actualBarcodeDTO);
            }
        }

        return barcodeDTOs;
    }

    /**
     * Get list of all items from database, for one account.
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
}