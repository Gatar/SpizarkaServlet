package com.gatar.services;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceTest {

    @Autowired
    DataService dataService;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    BarcodeDAO barcodeDAO;

    @Autowired
    AccountService accountService;

    private String username1 = "user1";
    private String username2 = "user2";
    private AccountDTO accountDTO = new AccountDTO();
    private AccountDTO accountDTO2 = new AccountDTO();
    private ItemDTO sampleItem = new ItemDTO();
    private ItemDTO sampleItem2 = new ItemDTO();
    private ItemDTO sampleItem3 = new ItemDTO();
    private BarcodeDTO sampleBarcode  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode2  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode3  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode4  = new BarcodeDTO();

    @Before
    public void fillSamplesWithData(){
        accountDTO.setUsername(username1);
        accountDTO.setEmail("email@email.com");
        accountDTO.setPassword("pass1");

        accountDTO2.setUsername(username2);
        accountDTO2.setEmail("email2@email.com");
        accountDTO2.setPassword("pass2");

        sampleItem.setQuantity(10);
        sampleItem.setIdItemAndroid(1L);
        sampleItem.setMinimumQuantity(5);
        sampleItem.setDescription("Text 1");
        sampleItem.setTitle("Title 1");
        sampleItem.setCategory("Category 1");

        sampleItem2.setQuantity(10);
        sampleItem2.setIdItemAndroid(2L);
        sampleItem2.setMinimumQuantity(5);
        sampleItem2.setDescription("Text 2");
        sampleItem2.setTitle("Title 2");
        sampleItem2.setCategory("Category 2");

        sampleItem3.setQuantity(10);
        sampleItem3.setIdItemAndroid(2L);
        sampleItem3.setMinimumQuantity(5);
        sampleItem3.setDescription("Text 3");
        sampleItem3.setTitle("Title 3");
        sampleItem3.setCategory("Category 3");

        sampleBarcode.setIdItemAndroid(1L);
        sampleBarcode.setBarcode("1abcd");

        sampleBarcode2.setIdItemAndroid(1L);
        sampleBarcode2.setBarcode("2abcd");

        sampleBarcode3.setIdItemAndroid(2L);
        sampleBarcode3.setBarcode("3abcd");

        sampleBarcode4.setIdItemAndroid(2L);
        sampleBarcode4.setBarcode("4abcd");

        accountService.saveAccount(accountDTO);
        accountService.saveAccount(accountDTO2);
    }

    //TODO Not ready now
    @Test
    public void saveItem() throws Exception {
        dataService.saveItem(sampleItem,username1);
        dataService.saveItem(sampleItem,username2);
        dataService.saveItem(sampleItem2,username1);
        dataService.saveItem(sampleItem3,username2);

        Assert.assertEquals(sampleItem,itemDAO.findByIdItemAndroidAndAccount(1L,accountService.getAccount(username1)).toItemDTO());
        Assert.assertEquals(sampleItem2,itemDAO.findByIdItemAndroidAndAccount(2L,accountService.getAccount(username1)).toItemDTO());
        Assert.assertEquals(sampleItem,itemDAO.findByIdItemAndroidAndAccount(1L,accountService.getAccount(username2)).toItemDTO());
        Assert.assertEquals(sampleItem3,itemDAO.findByIdItemAndroidAndAccount(2L,accountService.getAccount(username2)).toItemDTO());
    }

    @Test
    public void saveBarcode() throws Exception {

    }

    @Test
    public void getAllBarcodes() throws Exception {

    }

    @Test
    public void getAllItems() throws Exception {

    }

    @Test
    public void getShoppingList() throws Exception {

    }

}