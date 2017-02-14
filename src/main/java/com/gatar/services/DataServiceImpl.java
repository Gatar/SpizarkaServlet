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

    SaveEntity entity;

    /**
     * {@inheritDoc}
     * @param entityDTO item with barcodes object
     * @param username to bind Barcode with Account
     * @return SaveFeedback.EntityAddedOrUpdated - entity has been saved in database, SaveFeedback.EntityAddingFail - entity hasn't been saved
     */
    public SaveFeedback saveEntity(EntityDTO entityDTO, String username) {
        if(entity == null) entity = new SaveEntity();
        return entity.save(entityDTO,username);
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

    private class SaveEntity{

        private EntityDTO entityDTO;
        private String username;
        private Account account;
        private Item item;
        private List<Barcode> existingBarcodes;


        SaveFeedback save(EntityDTO entityDTO, String username){
            this.entityDTO = entityDTO;
            this.username = username;

            loadAccountFromDatabase();
            prepareItemObject();
            prepareExisitingBarcodesList();
            saveItem();
            saveBarcodes();
            increaseDatabaseVersion();

            return SaveFeedback.EntityAddedOrUpdated;
        }

        private void loadAccountFromDatabase() {
            account = accountServiceImpl.getAccount(username);
        }

        private void prepareItemObject() {
            item = entityDTO.toItem();
            item.setAccount(account);
        }

        private void prepareExisitingBarcodesList(){
            existingBarcodes = new LinkedList<>();

            Optional<Item> itemFromDatabase = Optional.ofNullable(itemDAO.findByIdItemAndroidAndAccount(item.getIdItemAndroid(), account));

            if (itemFromDatabase.isPresent()) {
                Item actualItem = itemFromDatabase.get();
                item.setIdItem(actualItem.getIdItem());
                existingBarcodes = actualItem.getBarcodes();
            }
        }

        private void saveItem(){
            itemDAO.save(item);
        }

        private void saveBarcodes(){
            for (String barcode : entityDTO.getBarcodes()) {
                Barcode tempBarcode = new Barcode();
                tempBarcode.setBarcodeValue(barcode);
                tempBarcode.setItem(item);

                if (isNotContainBarcode(tempBarcode)) barcodeDAO.save(tempBarcode);
            }
        }

        private boolean isNotContainBarcode(Barcode barcode){
            return !existingBarcodes.contains(barcode);
        }

        private void increaseDatabaseVersion(){
            accountServiceImpl.putDataVersion(entityDTO.getDatabaseVersion(), account.getUsername());
        }
    }


}
