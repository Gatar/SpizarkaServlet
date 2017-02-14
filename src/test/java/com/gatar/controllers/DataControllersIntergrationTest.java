package com.gatar.controllers;

import com.gatar.database.BarcodeDAO;
import com.gatar.database.ItemDAO;
import com.gatar.domain.AccountDTO;
import com.gatar.domain.BarcodeDTO;
import com.gatar.domain.EntityDTO;
import com.gatar.domain.ItemDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataControllersIntergrationTest {

    private AccountDTO firstTestAccountDTO = new AccountDTO();
    private AccountDTO secondTestAccountDTO = new AccountDTO();
    private AccountDTO emptyItemTestAccountDTO = new AccountDTO();
    private AccountDTO fourthTestAccountDTO = new AccountDTO();

    private HttpHeaders firstAccountHttpHeaders;
    private HttpHeaders secondAccountHttpHeaders;
    private HttpHeaders emptyAccountHttpHeaders;
    private HttpHeaders fourthAccountHttpHeaders;

    private ItemDTO sampleItem1 = new ItemDTO();
    private ItemDTO sampleItem2 = new ItemDTO();
    private ItemDTO sampleItem3 = new ItemDTO();
    private ItemDTO sampleItem4 = new ItemDTO();
    private ItemDTO sampleItem5 = new ItemDTO();
    private BarcodeDTO sampleBarcode1 = new BarcodeDTO();
    private BarcodeDTO sampleBarcode2  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode3  = new BarcodeDTO();
    private BarcodeDTO sampleBarcode4  = new BarcodeDTO();
    private BarcodeDTO sampleBarcodeFakeItem  = new BarcodeDTO();
    private EntityDTO sampleEntity1 = new EntityDTO();
    private EntityDTO sampleEntity2 = new EntityDTO();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BarcodeDAO barcodeDAO;

    @Autowired
    private ItemDAO itemDAO;


    @Before
    public void setUp() throws Exception {
        String firstTestUsername = "firstUserDataTest";
        firstTestAccountDTO.setUsername(firstTestUsername);
        firstTestAccountDTO.setEmail("gatarpl@gmail.com");
        firstTestAccountDTO.setPassword("password");

        String secondTestUsername = "secondUserDataTest";
        secondTestAccountDTO.setUsername(secondTestUsername);
        secondTestAccountDTO.setEmail("gatarpl@gmail.com");
        secondTestAccountDTO.setPassword("password2");

        String thirdTestUsername = "emptyItemUserdataTest";
        emptyItemTestAccountDTO.setUsername(thirdTestUsername);
        emptyItemTestAccountDTO.setEmail("gatarpl@gmail.com");
        emptyItemTestAccountDTO.setPassword("emptyPassword");

        String fourthTestUsername = "fourthUserDataTest";
        fourthTestAccountDTO.setUsername(fourthTestUsername);
        fourthTestAccountDTO.setEmail("gatarpl@gmail.com");
        fourthTestAccountDTO.setPassword("password4");

        firstAccountHttpHeaders = getHeaders(firstTestAccountDTO.getUsername(), firstTestAccountDTO.getPassword());
        secondAccountHttpHeaders = getHeaders(secondTestAccountDTO.getUsername(), secondTestAccountDTO.getPassword());
        emptyAccountHttpHeaders = getHeaders(emptyItemTestAccountDTO.getUsername(), emptyItemTestAccountDTO.getPassword());
        fourthAccountHttpHeaders = getHeaders(fourthTestAccountDTO.getUsername(), fourthTestAccountDTO.getPassword());

        sampleItem1.setQuantity(10);
        sampleItem1.setIdItemAndroid(1L);
        sampleItem1.setMinimumQuantity(5);
        sampleItem1.setDescription("Text 1");
        sampleItem1.setTitle("Title 1");
        sampleItem1.setCategory("Category 1");

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

        sampleBarcode1.setIdItemAndroid(1L);
        sampleBarcode1.setBarcodeValue("1abcd");

        sampleBarcode2.setIdItemAndroid(1L);
        sampleBarcode2.setBarcodeValue("2abcd");

        sampleBarcode3.setIdItemAndroid(2L);
        sampleBarcode3.setBarcodeValue("3abcd");

        sampleBarcode4.setIdItemAndroid(2L);
        sampleBarcode4.setBarcodeValue("4abcd");

        sampleBarcodeFakeItem.setIdItemAndroid(8L);
        sampleBarcodeFakeItem.setBarcodeValue("5abcd");

        sampleEntity1.setBarcodes(new ArrayList<>());
        sampleEntity1.getBarcodes().add(sampleBarcode1.getBarcodeValue());
        sampleEntity1.getBarcodes().add(sampleBarcode2.getBarcodeValue());
        sampleEntity1.setCategory("category1");
        sampleEntity1.setDescription("desct");
        sampleEntity1.setIdItemAndroid(1L);
        sampleEntity1.setMinimumQuantity(12);
        sampleEntity1.setQuantity(3);
        sampleEntity1.setDatabaseVersion(2L);
        sampleEntity1.setTitle("SampleEntity1");

        sampleEntity2.setBarcodes(new ArrayList<>());
        sampleEntity2.getBarcodes().add(sampleBarcode3.getBarcodeValue());
        sampleEntity2.getBarcodes().add(sampleBarcode4.getBarcodeValue());
        sampleEntity2.setCategory("category2");
        sampleEntity2.setDescription("description");
        sampleEntity2.setIdItemAndroid(2L);
        sampleEntity2.setMinimumQuantity(5);
        sampleEntity2.setQuantity(30);
        sampleEntity2.setDatabaseVersion(3L);
        sampleEntity2.setTitle("SampleEntity2");

    }

    private HttpHeaders getHeaders(String username, String pass){
        String plainCredentials=username+":"+pass;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String generateURI(AccountDTO accountDTO, String functionality){
        return "/"+accountDTO.getUsername()+"/"+functionality;
    }

    @Test
    public void A_addNewAccount() throws Exception {
        final String URI = "/addNewAccount";

        HttpEntity<AccountDTO> requestFirst = new HttpEntity<>(firstTestAccountDTO, firstAccountHttpHeaders);
        HttpEntity<AccountDTO> requestSecond = new HttpEntity<>(secondTestAccountDTO, secondAccountHttpHeaders);
        HttpEntity<AccountDTO> requestEmpty = new HttpEntity<>(emptyItemTestAccountDTO, emptyAccountHttpHeaders);
        HttpEntity<AccountDTO> requestFourth = new HttpEntity<>(fourthTestAccountDTO, fourthAccountHttpHeaders);

        ResponseEntity<Void> addFirstNewAccount = restTemplate.postForEntity(URI,requestFirst,Void.class);
        ResponseEntity<Void> addSecondNewAccount = restTemplate.postForEntity(URI,requestSecond,Void.class);
        ResponseEntity<Void> addEmptyNewAccount = restTemplate.postForEntity(URI,requestEmpty,Void.class);
        ResponseEntity<Void> addExistingAccount = restTemplate.postForEntity(URI,requestFirst,Void.class);
        ResponseEntity<Void> addFourthNewAccount = restTemplate.postForEntity(URI,requestFourth,Void.class);

        assertEquals(HttpStatus.CREATED, addFirstNewAccount.getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE,addExistingAccount.getStatusCode());
        assertEquals(HttpStatus.CREATED, addSecondNewAccount.getStatusCode());
        assertEquals(HttpStatus.CREATED, addFourthNewAccount.getStatusCode());
    }


    @Test
    public void B_saveItem() throws Exception {
        final String firstURI = generateURI(firstTestAccountDTO,"saveItem");
        final String secondURI = generateURI(secondTestAccountDTO,"saveItem");

        //----------------Adding new Items for each Account-----------------------------------------
        HttpEntity<ItemDTO> item1_firstUser = new HttpEntity<>(sampleItem1, firstAccountHttpHeaders);
        HttpEntity<ItemDTO> item2_firstUser = new HttpEntity<>(sampleItem2, firstAccountHttpHeaders);
        HttpEntity<ItemDTO> item4_firstUser = new HttpEntity<>(sampleItem4, firstAccountHttpHeaders);
        HttpEntity<ItemDTO> item1_secondUser = new HttpEntity<>(sampleItem1, secondAccountHttpHeaders);
        HttpEntity<ItemDTO> item3_secondUser = new HttpEntity<>(sampleItem3, secondAccountHttpHeaders);
        HttpEntity<ItemDTO> item4_secondUser = new HttpEntity<>(sampleItem4, secondAccountHttpHeaders);
        HttpEntity<ItemDTO> item5_secondUser = new HttpEntity<>(sampleItem5, secondAccountHttpHeaders);

        ResponseEntity<Void> addItem1_firstUser = restTemplate.postForEntity(firstURI,item1_firstUser,Void.class);
        ResponseEntity<Void> addItem2_firstUser = restTemplate.postForEntity(firstURI,item2_firstUser,Void.class);
        ResponseEntity<Void> addItem4_firstUser = restTemplate.postForEntity(firstURI,item4_firstUser,Void.class);
        ResponseEntity<Void> addItem1_secondUser = restTemplate.postForEntity(secondURI,item1_secondUser,Void.class);
        ResponseEntity<Void> addItem3_secondUser = restTemplate.postForEntity(secondURI,item3_secondUser,Void.class);
        ResponseEntity<Void> addItem4_secondUser = restTemplate.postForEntity(secondURI,item4_secondUser,Void.class);
        ResponseEntity<Void> addItem5_secondUser = restTemplate.postForEntity(secondURI,item5_secondUser,Void.class);

        Assert.assertEquals(HttpStatus.CREATED,addItem1_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem2_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem4_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem3_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem1_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem4_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addItem5_secondUser.getStatusCode());


        //----------------Adding Item existing already in database, should be updated---------------------
        addItem4_firstUser = restTemplate.postForEntity(firstURI,item4_firstUser,Void.class);
        addItem1_secondUser = restTemplate.postForEntity(secondURI,item1_secondUser,Void.class);


        Assert.assertEquals(HttpStatus.ACCEPTED,addItem4_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.ACCEPTED,addItem1_secondUser.getStatusCode());

    }

    @Test
    public void Bb_saveEntity() throws Exception{
        final String URI = generateURI(fourthTestAccountDTO,"saveEntity");


        //----------------Adding new Entities for Account-----------------------------------------
        HttpEntity<EntityDTO> entity1 = new HttpEntity<>(sampleEntity1,fourthAccountHttpHeaders);
        HttpEntity<EntityDTO> entity2 = new HttpEntity<>(sampleEntity2,fourthAccountHttpHeaders);

        ResponseEntity<Void> addEntity1 = restTemplate.postForEntity(URI,entity1,Void.class);
        ResponseEntity<Void> addEntity2 = restTemplate.postForEntity(URI,entity2,Void.class);

        Assert.assertEquals(HttpStatus.CREATED,addEntity1.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addEntity2.getStatusCode());

        //------------- Add existing Entity, shouldn't be duplicated in results----------
        restTemplate.postForEntity(URI,entity1,Void.class);

        //------------- Add existing, bu modified Entity, should be updated and shown in results----------
        sampleEntity1.setTitle("Modificeted Entity");
        sampleEntity1.setDatabaseVersion(4L);
        HttpEntity<EntityDTO> entityMofified = new HttpEntity<>(sampleEntity1,fourthAccountHttpHeaders);
        restTemplate.postForEntity(URI,entityMofified,Void.class);
    }

    @Test
    public void C_saveBarcode() throws Exception {
        final String firstURI = generateURI(firstTestAccountDTO,"saveBarcode");
        final String secondURI = generateURI(secondTestAccountDTO,"saveBarcode");

        //----------------Adding new Barcodes for each Account-----------------------------------------
        HttpEntity<BarcodeDTO> barcode1_firstUser = new HttpEntity<>(sampleBarcode1,firstAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcode3_firstUser = new HttpEntity<>(sampleBarcode3,firstAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcode1_secondUser = new HttpEntity<>(sampleBarcode1,secondAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcode2_secondUser = new HttpEntity<>(sampleBarcode2,secondAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcode3_secondUser = new HttpEntity<>(sampleBarcode3,secondAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcode4_secondUser = new HttpEntity<>(sampleBarcode4,secondAccountHttpHeaders);

        ResponseEntity<Void> addBarcode1_firstUser = restTemplate.postForEntity(firstURI,barcode1_firstUser,Void.class);
        ResponseEntity<Void> addBarcode3_firstUser = restTemplate.postForEntity(firstURI,barcode3_firstUser,Void.class);
        ResponseEntity<Void> addBarcode1_secondUser = restTemplate.postForEntity(secondURI,barcode1_secondUser,Void.class);
        ResponseEntity<Void> addBarcode2_secondUser = restTemplate.postForEntity(secondURI,barcode2_secondUser,Void.class);
        ResponseEntity<Void> addBarcode3_secondUser = restTemplate.postForEntity(secondURI,barcode3_secondUser,Void.class);
        ResponseEntity<Void> addBarcode4_secondUser = restTemplate.postForEntity(secondURI,barcode4_secondUser,Void.class);

        Assert.assertEquals(HttpStatus.CREATED,addBarcode1_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addBarcode3_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addBarcode1_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addBarcode2_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addBarcode3_secondUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addBarcode4_secondUser.getStatusCode());

        //-------------Adding barcodes, which already exist in database------------------------------
        addBarcode3_firstUser = restTemplate.postForEntity(firstURI,barcode3_firstUser,Void.class);
        addBarcode1_secondUser = restTemplate.postForEntity(secondURI,barcode1_secondUser,Void.class);

        Assert.assertEquals(HttpStatus.CONFLICT,addBarcode3_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CONFLICT,addBarcode1_secondUser.getStatusCode());


        //------------Adding barcodes for items which doesn't exist---------------------------------
        HttpEntity<BarcodeDTO> barcodeFakeItem_firstUser = new HttpEntity<>(sampleBarcodeFakeItem,firstAccountHttpHeaders);
        HttpEntity<BarcodeDTO> barcodeFakeItem_secondUser = new HttpEntity<>(sampleBarcodeFakeItem,secondAccountHttpHeaders);

        ResponseEntity<Void> addBarcodeFakeItem_firstUser = restTemplate.postForEntity(firstURI,barcodeFakeItem_firstUser,Void.class);
        ResponseEntity<Void> addBarcodeFakeItem_secondUser = restTemplate.postForEntity(firstURI,barcodeFakeItem_secondUser,Void.class);

        Assert.assertEquals(HttpStatus.CONFLICT,addBarcodeFakeItem_firstUser.getStatusCode());
        Assert.assertEquals(HttpStatus.CONFLICT,addBarcodeFakeItem_secondUser.getStatusCode());

    }

    @Test
    public void D_getAllItems() throws Exception {
        final String firstURI = generateURI(firstTestAccountDTO,"getAllItems");
        final String secondURI = generateURI(secondTestAccountDTO,"getAllItems");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getAllItems");
        final String fourthURI = generateURI(fourthTestAccountDTO,"getAllItems");

        List<ItemDTO> expectedFirstUserItems = Arrays.asList(sampleItem1,sampleItem2,sampleItem4);
        List<ItemDTO> expectedSecondUserItems = Arrays.asList(sampleItem1,sampleItem3,sampleItem4,sampleItem5);

        sampleEntity1.setTitle("Modificeted Entity"); //prepare modification in sample Entity
        List<ItemDTO> expectedFourthUserItems = Arrays.asList(sampleEntity1.toItem().toItemDTO(),sampleEntity2.toItem().toItemDTO());

        //-------------------get All Items which were add before------------------------------------
        HttpEntity<Void> firstUserRequest = new HttpEntity<>(firstAccountHttpHeaders);
        HttpEntity<Void> secondUserRequest = new HttpEntity<>(secondAccountHttpHeaders);

        ResponseEntity<ItemDTO[]> firstUserItemsResponse = restTemplate.exchange(firstURI,HttpMethod.GET,firstUserRequest,ItemDTO[].class);
        ResponseEntity<ItemDTO[]> secondUserItemsResponse = restTemplate.exchange(secondURI,HttpMethod.GET,secondUserRequest,ItemDTO[].class);

        List<ItemDTO> receivedFirstUserItems = Arrays.asList(firstUserItemsResponse.getBody());
        List<ItemDTO> receivedSecondUserItems = Arrays.asList(secondUserItemsResponse.getBody());

        Assert.assertEquals(expectedFirstUserItems,receivedFirstUserItems);
        Assert.assertEquals(expectedSecondUserItems,receivedSecondUserItems);
        Assert.assertEquals(HttpStatus.OK,firstUserItemsResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.OK,secondUserItemsResponse.getStatusCode());


        //--------------------get all Items for Account without any Items--------------------------
        HttpEntity<Void> emptyUserRequest = new HttpEntity<>(emptyAccountHttpHeaders);

        ResponseEntity<ItemDTO[]> emptyUserItemsResponse = restTemplate.exchange(emptyURI,HttpMethod.GET,emptyUserRequest,ItemDTO[].class);

        Assert.assertEquals(0, emptyUserItemsResponse.getBody().length);
        Assert.assertEquals(HttpStatus.CONFLICT,emptyUserItemsResponse.getStatusCode());

        //------------------ get all Items for Account which has been added by EntityDTO--------------
        HttpEntity<Void> fourthUserRequest = new HttpEntity<>(fourthAccountHttpHeaders);

        ResponseEntity<ItemDTO[]> fourthUserItemResponse = restTemplate.exchange(fourthURI,HttpMethod.GET,fourthUserRequest,ItemDTO[].class);

        List<ItemDTO> receivedFourthUserItems = Arrays.asList(fourthUserItemResponse.getBody());

        Assert.assertEquals(expectedFourthUserItems,receivedFourthUserItems);
        Assert.assertEquals(HttpStatus.OK,fourthUserItemResponse.getStatusCode());
    }

    @Test
    public void E_getAllBarcodes() throws Exception {
        final String firstURI = generateURI(firstTestAccountDTO,"getAllBarcodes");
        final String secondURI = generateURI(secondTestAccountDTO,"getAllBarcodes");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getAllBarcodes");
        final String fourthURI = generateURI(fourthTestAccountDTO,"getAllBarcodes");

        List<BarcodeDTO> expectedFirstUserBarcodes = Arrays.asList(sampleBarcode1, sampleBarcode3);
        List<BarcodeDTO> expectedSecondUserBarcodes = Arrays.asList(sampleBarcode1, sampleBarcode2, sampleBarcode3, sampleBarcode4);

        //-------------------get All Items which were add before------------------------------------
        HttpEntity<Void> firstUserRequest = new HttpEntity<>(firstAccountHttpHeaders);
        HttpEntity<Void> secondUserRequest = new HttpEntity<>(secondAccountHttpHeaders);
        HttpEntity<Void> fourthUserRequest = new HttpEntity<>(fourthAccountHttpHeaders);


        ResponseEntity<BarcodeDTO[]> firstUserBarcodesResponse = restTemplate.exchange(firstURI,HttpMethod.GET,firstUserRequest,BarcodeDTO[].class);
        ResponseEntity<BarcodeDTO[]> secondUserBarcodesResponse = restTemplate.exchange(secondURI,HttpMethod.GET,secondUserRequest,BarcodeDTO[].class);
        ResponseEntity<BarcodeDTO[]> fourthUserBarcodesResponse = restTemplate.exchange(fourthURI,HttpMethod.GET,fourthUserRequest,BarcodeDTO[].class);


        List<BarcodeDTO> receivedFirstUserBarcodes = Arrays.asList(firstUserBarcodesResponse.getBody());
        List<BarcodeDTO> receivedSecondUserBarcodes = Arrays.asList(secondUserBarcodesResponse.getBody());
        List<BarcodeDTO> receivedFourthUserBarcodes = Arrays.asList(fourthUserBarcodesResponse.getBody());

        Assert.assertEquals(expectedFirstUserBarcodes,receivedFirstUserBarcodes);
        Assert.assertEquals(expectedSecondUserBarcodes,receivedSecondUserBarcodes);
        Assert.assertEquals(expectedSecondUserBarcodes,receivedFourthUserBarcodes);
        Assert.assertEquals(HttpStatus.OK,firstUserBarcodesResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.OK,secondUserBarcodesResponse.getStatusCode());
        Assert.assertEquals(HttpStatus.OK,fourthUserBarcodesResponse.getStatusCode());


        //--------------------get all Items for Account without any Items--------------------------
        HttpEntity<Void> emptyUserRequest = new HttpEntity<>(emptyAccountHttpHeaders);

        ResponseEntity<BarcodeDTO[]> emptyUserBarcodesResponse = restTemplate.exchange(emptyURI,HttpMethod.GET,emptyUserRequest,BarcodeDTO[].class);

        Assert.assertEquals(0,emptyUserBarcodesResponse.getBody().length);
        Assert.assertEquals(HttpStatus.CONFLICT,emptyUserBarcodesResponse.getStatusCode());
    }

    @Test
    public void F_sendShopingListToEmail() throws Exception {
        final String secondURI = generateURI(secondTestAccountDTO,"getShopping");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getShopping");

        final String testCorrectEmail = "gatarpl@gmail.com";
        final String testIncorrectEmail = "gatarplgmai";

        HttpEntity<String> requestShoppingListCorrectEmail = new HttpEntity<>(testCorrectEmail,secondAccountHttpHeaders);
        HttpEntity<String> requestShoppingListIncorrectEmail = new HttpEntity<>(testIncorrectEmail,secondAccountHttpHeaders);
        HttpEntity<String> requestShoppingListEmpty = new HttpEntity<>(testCorrectEmail,emptyAccountHttpHeaders);

        ResponseEntity<Void> responseShoppingListIncorrectEmail = restTemplate.postForEntity(secondURI,requestShoppingListIncorrectEmail,Void.class);
        ResponseEntity<Void> responseShoppingListEmpty = restTemplate.postForEntity(emptyURI,requestShoppingListEmpty,Void.class);

        assertEquals(HttpStatus.CONFLICT,responseShoppingListIncorrectEmail.getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseShoppingListEmpty.getStatusCode());

        //--------------------------Test with correctly send an email------------------------------------------
        //ResponseEntity<Void> responseShoppingListCorrectEmail = restTemplate.postForEntity(secondURI,requestShoppingListCorrectEmail,Void.class);
        //assertEquals(HttpStatus.OK,responseShoppingListCorrectEmail.getStatusCode());
    }

    @Test
    public void G_deleteAccount() throws Exception{
        final String firstURI = generateURI(firstTestAccountDTO,"delete");
        final String secondURI = generateURI(secondTestAccountDTO,"delete");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"delete");
        final String fourthURI = generateURI(fourthTestAccountDTO,"delete");

        HttpEntity<Void> requestDeleteFirst = new HttpEntity<>(firstAccountHttpHeaders);
        HttpEntity<Void> requestDeleteSecond = new HttpEntity<>(secondAccountHttpHeaders);
        HttpEntity<Void> requestDeleteEmpty = new HttpEntity<>(emptyAccountHttpHeaders);
        HttpEntity<Void> requestDeleteFourth = new HttpEntity<>(fourthAccountHttpHeaders);

        ResponseEntity<Void> deleteFirst = restTemplate.postForEntity(firstURI,requestDeleteFirst,Void.class);
        ResponseEntity<Void> deleteSecond = restTemplate.postForEntity(secondURI,requestDeleteSecond,Void.class);
        ResponseEntity<Void> deleteEmpty = restTemplate.postForEntity(emptyURI,requestDeleteEmpty,Void.class);
        ResponseEntity<Void> deleteFourth = restTemplate.postForEntity(fourthURI,requestDeleteFourth,Void.class);

        assertEquals(HttpStatus.OK,deleteFirst.getStatusCode());
        assertEquals(HttpStatus.OK,deleteSecond.getStatusCode());
        assertEquals(HttpStatus.OK,deleteEmpty.getStatusCode());
        assertEquals(HttpStatus.OK,deleteFourth.getStatusCode());
    }


    @Test
    public void H_cleanItemsAndBarcodes() throws Exception{

        //------------Check does deleting account erased all connected with it data---------------
        assertEquals(Collections.emptyList(),barcodeDAO.findAll());
        assertEquals(Collections.emptyList(),itemDAO.findAll());
    }

}