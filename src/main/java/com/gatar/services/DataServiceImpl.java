package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class ensuring service of main databases BarcodeDAO and ItemDAO. They are often used together so it's one service for handling them.
 */
@Service
public class DataServiceImpl implements DataService{

    @Autowired
    BarcodeDAO barcodeDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    AccountServiceImpl accountServiceImpl;

    /**
     * {@inheritDoc}
     * @param itemDTO item data
     * @param username to bind Item with Account
     * @return SaveFeedback.UpdatedExistingItem - item exist before in database, has been updated, SaveFeedback.AddedNewItem - new item added to database
     */
    public SaveFeedback saveItem(ItemDTO itemDTO, String username){
        SaveFeedback returnState = SaveFeedback.AddedNewItem;
        Account account = accountServiceImpl.getAccount(username);
        Optional<Item> itemFromDatabase = Optional.ofNullable(itemDAO.findByIdItemAndroidAndAccount(itemDTO.getIdItemAndroid(),account));

        Item item = itemDTO.toItem();
        item.setAccount(accountServiceImpl.getAccount(username));

        if(itemFromDatabase.isPresent()) {
            item.setIdItem(itemFromDatabase.get().getIdItem());
            returnState = SaveFeedback.UpdatedExistingItem;
        }
        itemDAO.save(item);
        return  returnState;
    }

    /**
     * {@inheritDoc}
     * @param barcodeDTO barcode data
     * @param username to bind Barcode with Account
     * @return SaveFeedback.ItemForBarcodeNotExist - item describen in Barcode doesn't exist in database, SaveFeedback.BarcodeAlreadyExist - there are the same barcode for this item in database, SaveFeedback.AddedNewBarcode - barcode added correctly
     */
    public SaveFeedback saveBarcode(BarcodeDTO barcodeDTO, String username){
        Account account = accountServiceImpl.getAccount(username);

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
     * {@inheritDoc}
     * @param entityDTO item with barcodes object
     * @param username to bind Barcode with Account
     * @return SaveFeedback.EntityAddedOrUpdated - entity has been saved in database, SaveFeedback.EntityAddingFail - entity hasn't been saved
     */
    public SaveFeedback saveEntity(EntityDTO entityDTO, String username) {

        //TODO Extract this method as separated class, too many inside dependencies for divide to smaller methods
        Account account = accountServiceImpl.getAccount(username);

        //create Item and List of Barcodes objects
        Item item = entityDTO.toItem();
        item.setAccount(account);
        List<Barcode> existingBarcodes = new LinkedList<>();

        //check has item exist already exist in database and if yes, add item's Id to Item object
        Optional<Item> itemFromDatabase = Optional.ofNullable(itemDAO.findByIdItemAndroidAndAccount(item.getIdItemAndroid(),account));
        //itemFromDatabase.ifPresent(item1 -> item.setIdItem(item1.getIdItem()));

        if(itemFromDatabase.isPresent()){
            Item actualItem = itemFromDatabase.get();
            item.setIdItem(actualItem.getIdItem());
            existingBarcodes = actualItem.getBarcodes();
        }

        itemDAO.save(item);

        for(String barcode : entityDTO.getBarcodes()){
            Barcode tempBarcode = new Barcode();
            tempBarcode.setBarcodeValue(barcode);
            tempBarcode.setItem(item);

            if(!existingBarcodes.contains(tempBarcode)) { //prevent add twice the same barcode to item
                barcodeDAO.save(tempBarcode);
            }
        }

        return SaveFeedback.EntityAddedOrUpdated;
    }


    /**
     * {@inheritDoc}
     * @param username specifying Account for Barcodes extract
     * @return list of all Barcodes connected with one Account
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
     * {@inheritDoc}
     * @param username specifying Account for Items extract
     * @return list of all Items connected with one Account
     */
    public List<ItemDTO> getAllItems(String username){
        List<Item> items = getItemListByUser(username);
        List<ItemDTO> itemDTOs = items.stream()
                .map(x -> x.toItemDTO())
                .collect(Collectors.toList());

        return itemDTOs;
    }

    /**
     * {@inheritDoc}
     * @param username specifying Account
     * @return list of Items with quantity lower than minimum
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
        Account account = accountServiceImpl.getAccount(username);
        return itemDAO.findByAccount(account);
    }




}
