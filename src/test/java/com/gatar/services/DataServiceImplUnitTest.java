package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

public class DataServiceImplUnitTest {

    @InjectMocks
    private DataServiceImpl dataServiceImpl;

    @Spy
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private ItemDTO itemDTO;

    @Mock
    private BarcodeDAO barcodeDAO;

    @Mock
    private BarcodeDTO barcodeDTO;


    private String testUsername = "user1";
    private AccountDTO testAccountDTO = new AccountDTO();
    private ItemDTO sampleItemDTO1 = new ItemDTO();
    private ItemDTO sampleItemDTO2 = new ItemDTO();
    private ItemDTO sampleItemDTO3 = new ItemDTO();
    private BarcodeDTO sampleBarcode1 = new BarcodeDTO();
    private BarcodeDTO sampleBarcode2  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode3  = new BarcodeDTO();
    private BarcodeDTO sampleBarcodeFakeItem  = new BarcodeDTO();
    private EntityDTO sampleEntity1 = new EntityDTO();
    private Account testUserAccount;

    @Before
    public void setupMock() {
        dataServiceImpl = new DataServiceImpl();
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("gatar@interia.pl");
        testAccountDTO.setPassword("pass1");

        testUserAccount = testAccountDTO.toAccount();
        Mockito.doReturn(testUserAccount).when(accountServiceImpl).getAccount(testUsername);

        sampleItemDTO1.setQuantity(10);
        sampleItemDTO1.setIdItemAndroid(1L);
        sampleItemDTO1.setMinimumQuantity(5);
        sampleItemDTO1.setDescription("Text 1");
        sampleItemDTO1.setTitle("Title 1");
        sampleItemDTO1.setCategory("Category 1");

        sampleItemDTO2.setQuantity(2);
        sampleItemDTO2.setIdItemAndroid(2L);
        sampleItemDTO2.setMinimumQuantity(5);
        sampleItemDTO2.setDescription("Text 2");
        sampleItemDTO2.setTitle("Title 2");
        sampleItemDTO2.setCategory("Category 2");

        sampleItemDTO3.setQuantity(0);
        sampleItemDTO3.setIdItemAndroid(2L);
        sampleItemDTO3.setMinimumQuantity(5);
        sampleItemDTO3.setDescription("Text 3");
        sampleItemDTO3.setTitle("Title 3");
        sampleItemDTO3.setCategory("Category 3");

        sampleBarcode1.setIdItemAndroid(1L);
        sampleBarcode1.setBarcodeValue("1abcd");

        sampleBarcode2.setIdItemAndroid(1L);
        sampleBarcode2.setBarcodeValue("2abcd");

        sampleBarcode3.setIdItemAndroid(2L);
        sampleBarcode3.setBarcodeValue("3abcd");

        sampleBarcodeFakeItem.setIdItemAndroid(8L);
        sampleBarcodeFakeItem.setBarcodeValue("5abcd");

        sampleEntity1.setBarcodes(new ArrayList<>());
        sampleEntity1.getBarcodes().add("12345678");
        sampleEntity1.getBarcodes().add("12345345");
        sampleEntity1.setQuantity(5);
        sampleEntity1.setIdItemAndroid(2L);
        sampleEntity1.setMinimumQuantity(5);
        sampleEntity1.setDescription("Text 3");
        sampleEntity1.setTitle("Title 3");
        sampleEntity1.setCategory("Category 3");

    }

    @Test
    public void testMockCreation(){
        assertNotNull(itemDAO);
        assertNotNull(itemDTO);
        assertNotNull(barcodeDAO);
        assertNotNull(barcodeDTO);
    }

    @Test(expected = NullPointerException.class)
    public void saveEntity() throws Exception{
        //add existing EntityDTO
        DataService.SaveFeedback saveEntity = dataServiceImpl.saveEntity(sampleEntity1,testUsername);
        Assert.assertEquals(DataServiceImpl.SaveFeedback.EntityAddedOrUpdated,saveEntity);

        //add EntityDTO without barcodes
        DataService.SaveFeedback saveEntityWithoutBarcodes =dataServiceImpl.saveEntity(new EntityDTO(),testUsername);
    }


    @Test
    public void getAllBarcodes() throws Exception {
        Item sampleItemTwoBarcodes = sampleItemDTO1.toItem();
        Item sampleItemOneBarcode = sampleItemDTO2.toItem();
        Item sampleItemZeroBarcode = sampleItemDTO3.toItem();

        sampleItemTwoBarcodes.setBarcodes(Arrays.asList(sampleBarcode1.toBarcode(),sampleBarcode2.toBarcode()));
        sampleItemOneBarcode.setBarcodes(Arrays.asList(sampleBarcode3.toBarcode()));
        sampleItemZeroBarcode.setBarcodes(Collections.emptyList());

        List<Item> user1Items = Arrays.asList(sampleItemTwoBarcodes,sampleItemOneBarcode,sampleItemZeroBarcode);
        testUserAccount.setItems(user1Items);
        Mockito.when(itemDAO.findByAccount(testUserAccount)).thenReturn(user1Items);

        List<BarcodeDTO> user1BarcodeDTO = Arrays.asList(sampleBarcode1,sampleBarcode2,sampleBarcode3);
        List<BarcodeDTO> user1AllBarcodes = dataServiceImpl.getAllBarcodes(testUsername);

        Assert.assertEquals(user1BarcodeDTO,user1AllBarcodes);

    }

    @Test
    public void getAllItems() throws Exception {
        List<ItemDTO> user1ItemsDTO = Arrays.asList(sampleItemDTO1, sampleItemDTO2, sampleItemDTO3);
        List<Item> user1Items = user1ItemsDTO.stream().map(i -> i.toItem()).collect(Collectors.toList());
        testUserAccount.setItems(user1Items);
        Mockito.when(itemDAO.findByAccount(testUserAccount)).thenReturn(user1Items);

        List<ItemDTO> user1AllItems = dataServiceImpl.getAllItems(testUsername);

        Assert.assertEquals(user1ItemsDTO,user1AllItems);
    }


    @Test
    public void getShoppingList() throws Exception{
        List<Item> user1Items = Arrays.asList(sampleItemDTO1.toItem(), sampleItemDTO2.toItem(), sampleItemDTO3.toItem());

        Mockito.when(itemDAO.findByAccount(testUserAccount)).thenReturn(user1Items);
        List<Item> itemsWithQuantityBelowMinimum = Arrays.asList(sampleItemDTO2.toItem(),sampleItemDTO3.toItem());
        List<Item> shoppingList = dataServiceImpl.getShoppingList(testUsername);

        Assert.assertEquals(itemsWithQuantityBelowMinimum,shoppingList);
    }

}
