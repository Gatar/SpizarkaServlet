package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test all functionalties from DataService with use of database connected with application.
 *
 * Adding to database data using DTO objects:
 * 1. Clean all database (once, at the begining)
 * 2. Create two accounts
 * 3. Add items for accounts (both: unique and the same for both accounts)
 * 4. Add barcodes for each items (as above and sigle/multiple barcodes for each item)
 *
 * Getting data from database as DTO objects:
 * 1. Get all items for each user
 * 2. Get all barcodes for each user
 * 3. Get shopping list (as Item objects, not DTO)
 *
 * Except that tests confirms:
 * - items are actualizing, not created one more time in database
 * - duplicates of barcode (for the same item and account) are not creating in database
 * - correctness of entity dependencies
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceIntegrationTest {

    public static boolean firstTest = true;

    @Autowired
    DataService dataService;

    @Autowired
    AccountService accountService;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    BarcodeDAO barcodeDAO;

    @Autowired
    AccountDAO accountDAO;

    private String username1 = "user1";
    private String username2 = "user2";
    private AccountDTO accountDTOuser1 = new AccountDTO();
    private AccountDTO accountDTOuser2 = new AccountDTO();
    private ItemDTO sampleItem = new ItemDTO();
    private ItemDTO sampleItem2 = new ItemDTO();
    private ItemDTO sampleItem3 = new ItemDTO();
    private ItemDTO sampleItem4 = new ItemDTO();
    private ItemDTO sampleItem5 = new ItemDTO();
    private BarcodeDTO sampleBarcode  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode2  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode3  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode4  = new BarcodeDTO();
    private BarcodeDTO sampleBarcodeFakeItem  = new BarcodeDTO();


    @Before
    public void fillSamplesWithData(){

            if(firstTest){
                barcodeDAO.deleteAll();
                itemDAO.deleteAll();
                accountDAO.deleteAll();
                firstTest = false;
            }

            accountDTOuser1.setUsername(username1);
            accountDTOuser1.setEmail("email@email.com");
            accountDTOuser1.setPassword("pass1");

            accountDTOuser2.setUsername(username2);
            accountDTOuser2.setEmail("email2@email.com");
            accountDTOuser2.setPassword("pass2");

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

            sampleItem4.setQuantity(5);
            sampleItem4.setIdItemAndroid(3L);
            sampleItem4.setMinimumQuantity(10);
            sampleItem4.setDescription("Text 4");
            sampleItem4.setTitle("Title 4");
            sampleItem4.setCategory("Category 4");

            sampleItem5.setQuantity(5);
            sampleItem5.setIdItemAndroid(4L);
            sampleItem5.setMinimumQuantity(10);
            sampleItem5.setDescription("Text 5");
            sampleItem5.setTitle("Title 5");
            sampleItem5.setCategory("Category 5");

            sampleBarcode.setIdItemAndroid(1L);
            sampleBarcode.setBarcodeValue("1abcd");

            sampleBarcode2.setIdItemAndroid(1L);
            sampleBarcode2.setBarcodeValue("2abcd");

            sampleBarcode3.setIdItemAndroid(2L);
            sampleBarcode3.setBarcodeValue("3abcd");

            sampleBarcode4.setIdItemAndroid(2L);
            sampleBarcode4.setBarcodeValue("4abcd");

            sampleBarcodeFakeItem.setIdItemAndroid(8L);
            sampleBarcodeFakeItem.setBarcodeValue("5abcd");

            accountService.saveAccount(accountDTOuser1);
            accountService.saveAccount(accountDTOuser2);
    }

    @Test
    public void saveItem() throws Exception {
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem,username1));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem2,username1));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem3,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem4,username1));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem4,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewItem,dataService.saveItem(sampleItem5,username2));

        Assert.assertEquals(DataService.SaveFeedback.UpdatedExistingItem,dataService.saveItem(sampleItem,username1));
        Assert.assertEquals(DataService.SaveFeedback.UpdatedExistingItem,dataService.saveItem(sampleItem,username2));

        Assert.assertEquals(sampleItem,itemDAO.findByIdItemAndroidAndAccount(1L,accountDAO.findByUsername(username1)).toItemDTO());
        Assert.assertEquals(sampleItem2,itemDAO.findByIdItemAndroidAndAccount(2L,accountDAO.findByUsername(username1)).toItemDTO());
        Assert.assertEquals(sampleItem,itemDAO.findByIdItemAndroidAndAccount(1L,accountDAO.findByUsername(username2)).toItemDTO());
        Assert.assertEquals(sampleItem3,itemDAO.findByIdItemAndroidAndAccount(2L,accountDAO.findByUsername(username2)).toItemDTO());
        Assert.assertEquals(sampleItem4,itemDAO.findByIdItemAndroidAndAccount(3L,accountDAO.findByUsername(username1)).toItemDTO());
        Assert.assertEquals(sampleItem4,itemDAO.findByIdItemAndroidAndAccount(3L,accountDAO.findByUsername(username2)).toItemDTO());
        Assert.assertEquals(sampleItem5,itemDAO.findByIdItemAndroidAndAccount(4L,accountDAO.findByUsername(username2)).toItemDTO());
    }

    @Test
    public void saveBarcode() throws Exception {

        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode,username1));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode3,username1));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode2,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode3,username2));
        Assert.assertEquals(DataService.SaveFeedback.AddedNewBarcode,dataService.saveBarcode(sampleBarcode4,username2));

        Assert.assertEquals(DataService.SaveFeedback.BarcodeAlreadyExist,dataService.saveBarcode(sampleBarcode,username1));
        Assert.assertEquals(DataService.SaveFeedback.BarcodeAlreadyExist,dataService.saveBarcode(sampleBarcode,username2));

        Assert.assertEquals(DataService.SaveFeedback.ItemForBarcodeNotExist,dataService.saveBarcode(sampleBarcodeFakeItem,username1));
        Assert.assertEquals(DataService.SaveFeedback.ItemForBarcodeNotExist,dataService.saveBarcode(sampleBarcodeFakeItem,username2));

    }

    @Test
    public void getAllBarcodes() throws Exception {
        List<BarcodeDTO> user1Barcodes = Arrays.asList(sampleBarcode, sampleBarcode3);
        List<BarcodeDTO> user2Barcodes = Arrays.asList(sampleBarcode, sampleBarcode2, sampleBarcode3, sampleBarcode4);

        Assert.assertEquals(user1Barcodes,dataService.getAllBarcodes(username1));
        Assert.assertEquals(user2Barcodes,dataService.getAllBarcodes(username2));
    }

    @Test
    public void getAllItems() throws Exception {
        List<ItemDTO> user1Items = Arrays.asList(sampleItem,sampleItem2,sampleItem4);
        List<ItemDTO> user2Items = Arrays.asList(sampleItem,sampleItem3,sampleItem4,sampleItem5);

        Assert.assertEquals(user1Items,dataService.getAllItems(username1));
        Assert.assertEquals(user2Items,dataService.getAllItems(username2));
    }

    @Test
    public void getShoppingList() throws Exception {
        List<ItemDTO> user1Items = Arrays.asList(sampleItem4);
        List<ItemDTO> user2Items = Arrays.asList(sampleItem4,sampleItem5);

        Assert.assertEquals(user1Items,dataService.getShoppingList(username1).stream().map(s->s.toItemDTO()).collect(Collectors.toList()));
        Assert.assertEquals(user2Items,dataService.getShoppingList(username2).stream().map(s->s.toItemDTO()).collect(Collectors.toList()));
    }

}