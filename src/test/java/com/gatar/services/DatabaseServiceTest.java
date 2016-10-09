package com.gatar.services;

import com.gatar.domain.Barcode;
import com.gatar.domain.Item;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.Assert.*;

public class DatabaseServiceTest {

    @Autowired
    DatabaseService databaseService;

    Item item1 = new Item();
    Item item2 = new Item();
    Barcode bar1 = new Barcode();
    Barcode bar2 = new Barcode();
    Barcode bar3 = new Barcode();


    @Before
    public void createSomeItemsAndBarcodes(){

        item1.setCategory("dupa");
        item1.setDescription("descr");
        item1.setMinimumQuantity(10);
        item1.setQuantity(5);
        item1.setTitle("dupa");

        item2.setCategory("dupa");
        item2.setDescription("descr");
        item2.setMinimumQuantity(10);
        item2.setQuantity(5);
        item2.setTitle("dupa2");

        bar1.setBarcode("1234");
        bar1.setItem(item1);

        bar2.setBarcode("12345");
        bar2.setItem(item2);

        bar3.setBarcode("123456");
        bar3.setItem(item1);

        databaseService.addNewItem(item1,bar1);
        databaseService.addNewItem(item2,bar2);
        databaseService.addNewBarcode(bar3);
        System.out.println("DONE!!!!!!!!!!!!!!!");
    }

}