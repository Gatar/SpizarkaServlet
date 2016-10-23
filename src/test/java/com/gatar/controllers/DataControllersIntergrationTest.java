package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import org.apache.tomcat.util.codec.binary.Base64;
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

import java.util.Arrays;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataControllersIntergrationTest {

    private String testUsername = "userDataTest";
    private AccountDTO testAccountDTO = new AccountDTO();
    private HttpHeaders httpHeadersTestAccount;

    @Autowired
    private TestRestTemplate restTemplate;


    @Before
    public void setUp() throws Exception {
        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("gatarpl@gmail.com");
        testAccountDTO.setPassword("password");

        httpHeadersTestAccount = getHeaders(testAccountDTO.getUsername(),testAccountDTO.getPassword());
    }

    private HttpHeaders getHeaders(String username, String pass){
        String plainCredentials=username+":"+pass;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Test
    public void A_addNewAccount() throws Exception {
        final String URI = "/addNewAccount";

        HttpEntity<AccountDTO> request = new HttpEntity<>(testAccountDTO,httpHeadersTestAccount);
        ResponseEntity<Void> addNewAccount = restTemplate.postForEntity(URI,request,Void.class);
        ResponseEntity<Void> addExistingAccount = restTemplate.postForEntity(URI,request,Void.class);

        assertEquals(HttpStatus.CREATED, addNewAccount.getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE,addExistingAccount.getStatusCode());
    }


    //TODO Wypełnić
    @Test
    public void B_saveItem() throws Exception {

    }

    @Test
    public void C_saveBarcode() throws Exception {

    }

    @Test
    public void D_getAllItems() throws Exception {

    }

    @Test
    public void E_getAllBarcodes() throws Exception {

    }

    @Test
    public void F_sendShopingListToEmail() throws Exception {

    }

    @Test
    public void G_deleteAccount() throws Exception{
        final String URI = "/"+testAccountDTO.getUsername()+"/delete";
        HttpEntity<Void> requestDelete = new HttpEntity<>(httpHeadersTestAccount);
        ResponseEntity<Void> delete = restTemplate.postForEntity(URI,requestDelete,Void.class);
        assertEquals(HttpStatus.OK,delete.getStatusCode());
    }

}