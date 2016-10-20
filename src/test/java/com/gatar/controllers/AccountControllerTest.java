package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    private String testUsername = "userTest";
    private AccountDTO testAccountDTO = new AccountDTO();

    @Before
    public void setupMock(){
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("email@email.com");
        testAccountDTO.setPassword("pass1");
    }

    private HttpHeaders getHeaders(String username, String pass){
        String plainCredentials=username+":"+pass;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getDataVersion() throws Exception {

    }

    @Test
    public void putDataVersion() throws Exception {

    }

    @Test
    public void addNewAccount() throws Exception {
        final String URI = "/addNewAccount";
        HttpEntity<Object> request = new HttpEntity<>(testAccountDTO,getHeaders(testAccountDTO.getUsername(),testAccountDTO.getPassword()));
        ResponseEntity<AccountDTO> responseEntity = restTemplate.postForEntity(URI,request,AccountDTO.class);
        AccountDTO receivedAccount = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(receivedAccount,testAccountDTO);

        responseEntity = restTemplate.postForEntity(URI,request,AccountDTO.class);

        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseEntity.getStatusCode());

        //TODO Cos zrobić z tym deletem
        HttpEntity<Object> requestDelete = new HttpEntity<>(HttpStatus.OK,getHeaders("gatar","password"));
        ResponseEntity<HttpStatus> delete = restTemplate.postForEntity("/"+testAccountDTO.getUsername()+"/delete",requestDelete,HttpStatus.class);
        assertEquals(HttpStatus.OK,delete.getStatusCode());
    }

    @Test
    public void sendEmailWithAccountData() throws Exception {

    }

}