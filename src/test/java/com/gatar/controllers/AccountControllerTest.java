package com.gatar.controllers;

import com.gatar.domain.AccountDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
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
    private HttpHeaders httpHeadersWrongUser;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setupMock(){
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("gatarpl@gmail.com");
        testAccountDTO.setPassword("oldPass");

        httpHeadersTestAccount = getHeaders(testAccountDTO.getUsername(),testAccountDTO.getPassword());
        httpHeadersWrongPassword = getHeaders(testAccountDTO.getUsername(),"badPassword");
        httpHeadersWrongUser = getHeaders("badUser",testAccountDTO.getPassword());
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

    @Test
    public void B_putDataVersion() throws Exception {
        final String URI = "/"+testAccountDTO.getUsername()+"/putDataVersion";
        Long firstVersion = 5L;
        Long correctNewVersion = 6L;
        Long previousVersion = 1L;
        Long toHighVersion = 100L;

        HttpEntity<Long> requestFirstVersion = new HttpEntity<>(firstVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestCorrectNewVersion = new HttpEntity<>(correctNewVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestTooHighVersion = new HttpEntity<>(previousVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestTooLowVersion = new HttpEntity<>(toHighVersion,httpHeadersTestAccount);
        HttpEntity<Long> requestWrongUser = new HttpEntity<>(toHighVersion,httpHeadersWrongUser);
        HttpEntity<Long> requestWrongPassword = new HttpEntity<>(toHighVersion,httpHeadersWrongPassword);

        ResponseEntity<Void> responseFirstVersion = restTemplate.postForEntity(URI,requestFirstVersion,Void.class);
        ResponseEntity<Void> responseCorrectNewVersion = restTemplate.postForEntity(URI,requestCorrectNewVersion,Void.class);
        ResponseEntity<Void> responseTooHighVersion = restTemplate.postForEntity(URI,requestTooHighVersion,Void.class);
        ResponseEntity<Void> responseTooLowVersion = restTemplate.postForEntity(URI,requestTooLowVersion,Void.class);
        ResponseEntity<Void> responseWrongUser = restTemplate.postForEntity(URI,requestWrongUser,Void.class);
        ResponseEntity<Void> responseWrongPassword = restTemplate.postForEntity(URI,requestWrongPassword,Void.class);


        assertEquals(HttpStatus.OK,responseFirstVersion.getStatusCode());
        assertEquals(HttpStatus.OK,responseCorrectNewVersion.getStatusCode());

        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseTooHighVersion.getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseTooLowVersion.getStatusCode());

        assertEquals(HttpStatus.FORBIDDEN,responseWrongPassword.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN,responseWrongUser.getStatusCode());


    }

    @Test
    public void C_getDataVersion() throws Exception {
        final String URI = "/"+testAccountDTO.getUsername()+"/getDataVersion";
        final String WrongURI = "/wrong/getDataVersion";
        Long correctNewVersion = 6L;
        Long badUsernameVersion = -1L;

        HttpEntity<Void> requestDataVersion = new HttpEntity<>(httpHeadersTestAccount);
        HttpEntity<Void> requestDataVersionOtherUsername = new HttpEntity<>(httpHeadersWrongUser);
        HttpEntity<Void> requestDataVersionBadURI = new HttpEntity<>(httpHeadersTestAccount);

        ResponseEntity<Long> responseDataVersion = restTemplate.exchange(URI,HttpMethod.GET,requestDataVersion,Long.class);
        ResponseEntity<Long> responseDataVersionOtherUsername = restTemplate.exchange(URI,HttpMethod.GET,requestDataVersionOtherUsername,Long.class);
        ResponseEntity<Long> responseDataVersionBadUri = restTemplate.exchange(WrongURI,HttpMethod.GET,requestDataVersionBadURI,Long.class);

        Assert.assertEquals(HttpStatus.OK,responseDataVersion.getStatusCode());
        Assert.assertEquals(correctNewVersion,responseDataVersion.getBody());

        Assert.assertEquals(HttpStatus.FORBIDDEN,responseDataVersionOtherUsername.getStatusCode());
        Assert.assertEquals(null,responseDataVersionOtherUsername.getBody());

        Assert.assertEquals(HttpStatus.BAD_REQUEST,responseDataVersionBadUri.getStatusCode());
        Assert.assertEquals(badUsernameVersion,responseDataVersionBadUri.getBody());
    }

    @Test
    public void D_changePassword() throws Exception {
        final String URI = "/"+testAccountDTO.getUsername()+"/changePassword";
        String newPassword = "newPass";
        String oldPassword = testAccountDTO.getPassword();

        HttpEntity<String> requestPasswordChange = new HttpEntity<>(newPassword,httpHeadersTestAccount);
        ResponseEntity<Void> responsePasswordChange = restTemplate.postForEntity(URI,requestPasswordChange,Void.class);

        HttpEntity<String> requestTryAccessWithOldPassword = new HttpEntity<>(newPassword,httpHeadersTestAccount);
        ResponseEntity<Void> responseTryAccessWithOldPassword = restTemplate.postForEntity(URI,requestTryAccessWithOldPassword,Void.class);

        testAccountDTO.setPassword(newPassword);
        httpHeadersTestAccount = getHeaders(testAccountDTO.getUsername(),testAccountDTO.getPassword());

        HttpEntity<String> requestTryAccessWithNewPassword = new HttpEntity<>(newPassword,httpHeadersTestAccount);
        ResponseEntity<Void> responseTryAccessWithNewPassword = restTemplate.postForEntity(URI,requestTryAccessWithNewPassword,Void.class);

        HttpEntity<String> requestSetBeginingPassword = new HttpEntity<>(oldPassword,httpHeadersTestAccount);
        ResponseEntity<Void> responseSetBeginingPassword = restTemplate.postForEntity(URI,requestSetBeginingPassword,Void.class);

        Assert.assertEquals(HttpStatus.OK,responsePasswordChange.getStatusCode());
        Assert.assertEquals(HttpStatus.FORBIDDEN,responseTryAccessWithOldPassword.getStatusCode());
        Assert.assertEquals(HttpStatus.OK,responseTryAccessWithNewPassword.getStatusCode());
        Assert.assertEquals(HttpStatus.OK,responseSetBeginingPassword.getStatusCode());
    }

//---------------------Test Turn Off because there is not need to random change password each time-----------------------
    public void E_sendEmailWithResetedPassword() throws Exception {
        final String URI = "/"+testAccountDTO.getUsername()+"/rememberAccountData";
        HttpEntity<Void> requestRememberEmail = new HttpEntity<>(httpHeadersTestAccount);
        ResponseEntity<Void> rememberEmail = restTemplate.exchange(URI,HttpMethod.HEAD,requestRememberEmail,Void.class);

        assertEquals(HttpStatus.OK,rememberEmail.getStatusCode());
    }


    @Test
    public void F_deleteAccount() throws Exception{
        final String URI = "/"+testAccountDTO.getUsername()+"/delete";
        HttpEntity<Void> requestDelete = new HttpEntity<>(httpHeadersTestAccount);
        ResponseEntity<Void> delete = restTemplate.postForEntity(URI,requestDelete,Void.class);
        assertEquals(HttpStatus.OK,delete.getStatusCode());
    }

}