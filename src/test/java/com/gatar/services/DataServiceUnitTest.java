package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DataServiceUnitTest {

    @InjectMocks
    private DataService dataService;

    @Spy
    private AccountService accountService;

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private ItemDTO itemDTO;

    @Mock
    private BarcodeDAO barcodeDAO;

    @Mock
    private BarcodeDTO barcodeDTO;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private Account account;

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
    public void setupMock() {
        dataService = new DataService();
        MockitoAnnotations.initMocks(this);

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
        sampleBarcode.setBarcode("1abcd");

        sampleBarcode2.setIdItemAndroid(1L);
        sampleBarcode2.setBarcode("2abcd");

        sampleBarcode3.setIdItemAndroid(2L);
        sampleBarcode3.setBarcode("3abcd");

        sampleBarcode4.setIdItemAndroid(2L);
        sampleBarcode4.setBarcode("4abcd");

        sampleBarcodeFakeItem.setIdItemAndroid(8L);
        sampleBarcodeFakeItem.setBarcode("5abcd");
    }

    @Test
    public void testMockCreation(){
        assertNotNull(itemDAO);
        assertNotNull(itemDTO);
        assertNotNull(barcodeDAO);
        assertNotNull(barcodeDTO);
    }

    @Test
    public void getAllItems() throws Exception {
        List<ItemDTO> user1ItemsDTO = Arrays.asList(sampleItem,sampleItem2,sampleItem4);
        System.out.println(user1ItemsDTO.toString() + "\n");
        List<Item> user1Items = user1ItemsDTO.stream().map(i -> i.toItem()).collect(Collectors.toList());
        System.out.println(user1Items.toString() + "\n");
        Account user1Account = accountDTOuser1.toAccount();
        System.out.println(user1Account.toString() + "\n");
        user1Account.setItems(user1Items);

        Mockito.doReturn(user1Account).when(accountService).getAccount(username1);
        Mockito.when(itemDAO.findByAccount(user1Account)).thenReturn(user1Items);

        List<ItemDTO> user1AllItems = dataService.getAllItems(username1);

        Assert.assertEquals(user1ItemsDTO,user1AllItems);
        System.out.println(user1AllItems.toString() + "\n");
    }

    @Test
    public void saveBarcode() throws Exception{
        Account user1Account = accountDTOuser1.toAccount();
        Item itemWithBarcodes = sampleItem2.toItem();
        itemWithBarcodes.setBarcodes(Arrays.asList(sampleBarcode2.toBarcode()));

        Mockito.doReturn(user1Account).when(accountService).getAccount(username1);

        Mockito.when(itemDAO.findByIdItemAndroidAndAccount(sampleBarcode.getIdItemAndroid(),user1Account)).thenReturn(sampleItem.toItem());
        Mockito.when(itemDAO.findByIdItemAndroidAndAccount(sampleBarcode2.getIdItemAndroid(),user1Account)).thenReturn(itemWithBarcodes);
        Mockito.when(itemDAO.findByIdItemAndroidAndAccount(sampleBarcodeFakeItem.getIdItemAndroid(),user1Account)).thenReturn(null);

        boolean saveToExistingItem = dataService.saveBarcode(sampleBarcode,username1);
        boolean saveToExistingItemWhichContainsThisBarcode = dataService.saveBarcode(sampleBarcode2,username1);
        boolean saveToNonExistingItem = dataService.saveBarcode(sampleBarcodeFakeItem,username1);

        Assert.assertTrue(saveToExistingItem);
        Assert.assertFalse(saveToExistingItemWhichContainsThisBarcode);
        Assert.assertFalse(saveToNonExistingItem);
    }

}
