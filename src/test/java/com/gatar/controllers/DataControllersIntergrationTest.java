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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

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
    private AccountDTO emptyItemTestAccountDTO = new AccountDTO();

    private HttpHeaders emptyAccountHttpHeaders;
    private HttpHeaders firstAccountHttpHeaders;

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
        firstTestAccountDTO.setPassword("password4");

        String emptyItemUsername = "emptyItemUserdataTest";
        emptyItemTestAccountDTO.setUsername(emptyItemUsername);
        emptyItemTestAccountDTO.setEmail("gatarpl@gmail.com");
        emptyItemTestAccountDTO.setPassword("emptyPassword");


        emptyAccountHttpHeaders = getHeaders(emptyItemTestAccountDTO.getUsername(), emptyItemTestAccountDTO.getPassword());
        firstAccountHttpHeaders = getHeaders(firstTestAccountDTO.getUsername(), firstTestAccountDTO.getPassword());

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

        HttpEntity<AccountDTO> requestEmpty = new HttpEntity<>(emptyItemTestAccountDTO, emptyAccountHttpHeaders);
        HttpEntity<AccountDTO> requestfirst = new HttpEntity<>(firstTestAccountDTO, firstAccountHttpHeaders);

        ResponseEntity<Void> addEmptyNewAccount = restTemplate.postForEntity(URI,requestEmpty,Void.class);
        ResponseEntity<Void> addfirstNewAccount = restTemplate.postForEntity(URI,requestfirst,Void.class);
        ResponseEntity<Void> addExistingAccount = restTemplate.postForEntity(URI,requestfirst,Void.class);

        assertEquals(HttpStatus.CREATED, addfirstNewAccount.getStatusCode());
        assertEquals(HttpStatus.CREATED, addEmptyNewAccount.getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE,addExistingAccount.getStatusCode());
    }


    @Test
    public void B_saveEntity() throws Exception{
        final String URI = generateURI(firstTestAccountDTO,"saveEntity");


        //----------------Adding new Entities for Account-----------------------------------------
        HttpEntity<EntityDTO> entity1 = new HttpEntity<>(sampleEntity1, firstAccountHttpHeaders);
        HttpEntity<EntityDTO> entity2 = new HttpEntity<>(sampleEntity2, firstAccountHttpHeaders);

        ResponseEntity<Void> addEntity1 = restTemplate.postForEntity(URI,entity1,Void.class);
        ResponseEntity<Void> addEntity2 = restTemplate.postForEntity(URI,entity2,Void.class);

        Assert.assertEquals(HttpStatus.CREATED,addEntity1.getStatusCode());
        Assert.assertEquals(HttpStatus.CREATED,addEntity2.getStatusCode());

        //------------- Add existing Entity, shouldn't be duplicated in results----------
        restTemplate.postForEntity(URI,entity1,Void.class);

        //------------- Add existing, bu modified Entity, should be updated and shown in results----------
        sampleEntity1.setTitle("Modificeted Entity");
        sampleEntity1.setDatabaseVersion(4L);
        HttpEntity<EntityDTO> entityMofified = new HttpEntity<>(sampleEntity1, firstAccountHttpHeaders);
        restTemplate.postForEntity(URI,entityMofified,Void.class);
    }

    @Test
    public void C_getAllItems() throws Exception {
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getAllItems");
        final String firstURI = generateURI(firstTestAccountDTO,"getAllItems");

        //--------------------prepare expected values of Items------------------------------------
        sampleEntity1.setTitle("Modificeted Entity");
        List<ItemDTO> expectedfirstUserItems = Arrays.asList(sampleEntity1.toItem().toItemDTO(),sampleEntity2.toItem().toItemDTO());


        //--------------------get all Items for Account without any Items--------------------------
        HttpEntity<Void> emptyUserRequest = new HttpEntity<>(emptyAccountHttpHeaders);
        ResponseEntity<ItemDTO[]> emptyUserItemsResponse = restTemplate.exchange(emptyURI,HttpMethod.GET,emptyUserRequest,ItemDTO[].class);

        Assert.assertEquals(0, emptyUserItemsResponse.getBody().length);
        Assert.assertEquals(HttpStatus.CONFLICT,emptyUserItemsResponse.getStatusCode());

        //------------------ get all Items for Account which has been added by EntityDTO--------------
        HttpEntity<Void> firstUserRequest = new HttpEntity<>(firstAccountHttpHeaders);
        ResponseEntity<ItemDTO[]> firstUserItemResponse = restTemplate.exchange(firstURI,HttpMethod.GET,firstUserRequest,ItemDTO[].class);
        List<ItemDTO> receivedfirstUserItems = Arrays.asList(firstUserItemResponse.getBody());

        Assert.assertEquals(expectedfirstUserItems,receivedfirstUserItems);
        Assert.assertEquals(HttpStatus.OK,firstUserItemResponse.getStatusCode());
    }

    @Test
    public void D_getAllBarcodes() throws Exception {
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getAllBarcodes");
        final String firstURI = generateURI(firstTestAccountDTO,"getAllBarcodes");

        //-------------------prepare expected values of Barcodes------------------------------------
        List<BarcodeDTO> expectedfirstUserBarcodes = Arrays.asList(sampleBarcode1, sampleBarcode2, sampleBarcode3, sampleBarcode4);

        //-------------------get All Items which were add before------------------------------------
        HttpEntity<Void> firstUserRequest = new HttpEntity<>(firstAccountHttpHeaders);
        ResponseEntity<BarcodeDTO[]> firstUserBarcodesResponse = restTemplate.exchange(firstURI,HttpMethod.GET,firstUserRequest,BarcodeDTO[].class);
        List<BarcodeDTO> receivedfirstUserBarcodes = Arrays.asList(firstUserBarcodesResponse.getBody());


        Assert.assertEquals(expectedfirstUserBarcodes,receivedfirstUserBarcodes);
        Assert.assertEquals(HttpStatus.OK,firstUserBarcodesResponse.getStatusCode());


        //--------------------get all Items for Account without any Items--------------------------
        HttpEntity<Void> emptyUserRequest = new HttpEntity<>(emptyAccountHttpHeaders);

        ResponseEntity<BarcodeDTO[]> emptyUserBarcodesResponse = restTemplate.exchange(emptyURI,HttpMethod.GET,emptyUserRequest,BarcodeDTO[].class);

        Assert.assertEquals(0,emptyUserBarcodesResponse.getBody().length);
        Assert.assertEquals(HttpStatus.CONFLICT,emptyUserBarcodesResponse.getStatusCode());
    }

    @Test
    public void E_sendShopingListToEmail() throws Exception {
        final String secondURI = generateURI(firstTestAccountDTO,"getShopping");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"getShopping");

        final String testCorrectEmail = "gatarpl@gmail.com";
        final String testIncorrectEmail = "gatarplgmai";

        HttpEntity<String> requestShoppingListCorrectEmail = new HttpEntity<>(testCorrectEmail, firstAccountHttpHeaders);
        HttpEntity<String> requestShoppingListIncorrectEmail = new HttpEntity<>(testIncorrectEmail, firstAccountHttpHeaders);
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
    public void F_deleteAccount() throws Exception{
        final String firstURI = generateURI(firstTestAccountDTO,"delete");
        final String emptyURI = generateURI(emptyItemTestAccountDTO,"delete");

        HttpEntity<Void> requestDeleteFirst = new HttpEntity<>(firstAccountHttpHeaders);
        HttpEntity<Void> requestDeleteEmpty = new HttpEntity<>(emptyAccountHttpHeaders);

        ResponseEntity<Void> deleteFirst = restTemplate.postForEntity(firstURI,requestDeleteFirst,Void.class);
        ResponseEntity<Void> deleteEmpty = restTemplate.postForEntity(emptyURI,requestDeleteEmpty,Void.class);

        assertEquals(HttpStatus.OK,deleteFirst.getStatusCode());
        assertEquals(HttpStatus.OK,deleteEmpty.getStatusCode());
    }


    @Test
    public void G_cleanItemsAndBarcodes() throws Exception{

        //------------Check does deleting account erased all connected with it data---------------
        assertEquals(Collections.emptyList(),barcodeDAO.findAll());
        assertEquals(Collections.emptyList(),itemDAO.findAll());
    }

}