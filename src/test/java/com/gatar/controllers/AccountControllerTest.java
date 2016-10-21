package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest{

    private String testUsername = "userTest";
    private AccountDTO testAccountDTO = new AccountDTO();
    private HttpHeaders httpHeadersTestAccount;
    private HttpHeaders httpHeadersWrongPassword;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setupMock(){
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("email@email.com");
        testAccountDTO.setPassword("pass1");

        httpHeadersTestAccount = getHeaders(testAccountDTO.getUsername(),testAccountDTO.getPassword());
        httpHeadersWrongPassword = getHeaders(testAccountDTO.getUsername(),"badPassword");
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
    public void count1addNewAccount() throws Exception {
        final String URIaddNewAccount = "/addNewAccount";

        HttpEntity<AccountDTO> request = new HttpEntity<>(testAccountDTO,httpHeadersTestAccount);
        ResponseEntity<AccountDTO> responseEntity = restTemplate.postForEntity(URIaddNewAccount,request,AccountDTO.class);
        AccountDTO receivedAccount = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(receivedAccount,testAccountDTO);

        responseEntity = restTemplate.postForEntity(URIaddNewAccount,request,AccountDTO.class);
        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseEntity.getStatusCode());
    }

    @Test
    public void count2putDataVersion() throws Exception {
        final String URIputDataVersion = "/"+testAccountDTO.getUsername()+"/putDataVersion";
        Long firstVersion = 5L;
        Long correctNewVersion = 6L;
        Long previousVersion = 1L;
        Long toHighVersion = 100L;

        HttpEntity<Long> requestFirstVersion = new HttpEntity<>(firstVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestCorrectNewVersion = new HttpEntity<>(correctNewVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestTooHighVersion = new HttpEntity<>(toHighVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestTooLowVersion = new HttpEntity<>(previousVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestWrongPassword = new HttpEntity<>(previousVersion,httpHeadersWrongPassword);

        ResponseEntity<Long> responseFirstVersion = restTemplate.postForEntity(URIputDataVersion,requestFirstVersion,Long.class);
        ResponseEntity<Long> responseCorrectNewVersion = restTemplate.postForEntity(URIputDataVersion,requestCorrectNewVersion,Long.class);
        ResponseEntity<Long> responseTooHighVersion = restTemplate.postForEntity(URIputDataVersion,requestTooHighVersion,Long.class);
        ResponseEntity<Long> responseTooLowVersion = restTemplate.postForEntity(URIputDataVersion,requestTooLowVersion,Long.class);
        ResponseEntity<Long> responseWrongPassword = restTemplate.postForEntity(URIputDataVersion,requestWrongPassword,Long.class);


        Long receivedDatabaseVersion = responseFirstVersion.getBody();
        assertEquals(HttpStatus.OK,responseFirstVersion.getStatusCode());
        assertEquals(firstVersion,receivedDatabaseVersion);

        receivedDatabaseVersion = responseCorrectNewVersion.getBody();
        assertEquals(HttpStatus.OK,responseCorrectNewVersion.getStatusCode());
        assertEquals(correctNewVersion,receivedDatabaseVersion);

        receivedDatabaseVersion = responseTooHighVersion.getBody();
        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseTooHighVersion.getStatusCode());
        assertEquals(correctNewVersion,receivedDatabaseVersion);

        receivedDatabaseVersion = responseTooLowVersion.getBody();
        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseTooLowVersion.getStatusCode());
        assertEquals(correctNewVersion,receivedDatabaseVersion);

        receivedDatabaseVersion = responseWrongPassword.getBody();
        assertEquals(HttpStatus.FORBIDDEN,responseWrongPassword.getStatusCode());
        assertEquals(null,receivedDatabaseVersion);


    }

    @Test
    public void count3getDataVersion() throws Exception {

    }

    @Test
    public void count4sendEmailWithAccountData() throws Exception {

    }

    @Test
    public void count5deleteAccount() throws Exception{
        HttpEntity<Object> requestDelete = new HttpEntity<>(HttpStatus.OK,httpHeadersTestAccount);
        ResponseEntity<HttpStatus> delete = restTemplate.postForEntity("/"+testAccountDTO.getUsername()+"/delete",requestDelete,HttpStatus.class);
        assertEquals(HttpStatus.OK,delete.getStatusCode());
    }

}