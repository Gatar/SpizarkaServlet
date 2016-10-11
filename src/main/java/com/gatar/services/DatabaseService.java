package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.Barcode;
import com.gatar.domain.Item;
import com.gatar.domain.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class ensuring service of main databases BarcodeDAO and ItemDAO.
 */
@Service
public class DatabaseService {

    @Autowired
    BarcodeDAO barcodeDAO;

    @Autowired
    ItemDAO itemDAO;

    /**
     * Save new/update existing item in database.
     * @param item object
     */
    public void saveItem(Item item){
        itemDAO.save(item);
    }

    /**
     * Save new/update existing barcode.
     * @param barcode object
     */
    public void saveBarcode(Barcode barcode){
        Item connectedItem = itemDAO.findOne(barcode.getIdBarcode());
        barcode.setItem(connectedItem);
        barcodeDAO.save(barcode);
    }

    /**
     * Get list of all barcodes in database
     * @return
     */
    public List<Barcode> getAllBarcodes(){
        return barcodeDAO.findAll();
    }

    /**
     * Get list of all items in database
     * @return
     */
    public List<Item> getAllItems(){
        return itemDAO.findAll();
    }

    /**
     * Get list of all items with quantity lower than minimum.
     * @return
     */
    public List<Item> getShoppingList(){
        List<Item> list = itemDAO.findAll();
        list = list.stream()
                .filter(s -> (s.getQuantity() < s.getMinimumQuantity()))
                .sorted((o1, o2) -> o1.getCategory().compareToIgnoreCase(o2.getCategory()))
                .collect(Collectors.toList());

        return list;
    }
}
