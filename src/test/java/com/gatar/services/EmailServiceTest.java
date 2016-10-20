package com.gatar.services;

import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import com.gatar.domain.Item;
import com.gatar.domain.ItemDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Testing only failure mail send properties, ex. wrong account.
 */
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Spy
    DataService dataService;

    @Spy
    AccountService accountService;

    private String testUsername = "user1";
    private AccountDTO testAccountDTO = new AccountDTO();
    private Account testUserAccount;
    private ItemDTO sampleItemDTO2 = new ItemDTO();
    private ItemDTO sampleItemDTO3 = new ItemDTO();

    @Before
    public void setupMock(){
        emailService = new EmailService();
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("test@test.pl");
        testAccountDTO.setPassword("pass1");

        testUserAccount = testAccountDTO.toAccount();
        testUserAccount.setDataVersion(123L);

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

    }

    @Test
    public void sendShoppingListEmail() throws Exception {
        List<Item> itemsWithQuantityBelowMinimum = Arrays.asList(sampleItemDTO2.toItem(),sampleItemDTO3.toItem());
        String correctEmail = "test@test.pl";
        String incorrectEmail = "aaa.pl";
        String incorrectUser = "nothing";

        Mockito.doReturn(null).when(dataService).getShoppingList(Matchers.anyString());
        Mockito.doReturn(itemsWithQuantityBelowMinimum).when(dataService).getShoppingList(testUsername);

        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE,emailService.sendShoppingListEmail(correctEmail,incorrectUser));
        Assert.assertEquals(HttpStatus.CONFLICT,emailService.sendShoppingListEmail(incorrectEmail,testUsername));
    }

    @Test
    public void sendAccountDataRemember() throws Exception {
        String incorrectUser = "nothing";
        Mockito.doReturn(null).when(accountService).getAccount(Matchers.anyString());
        Mockito.doReturn(testUserAccount).when(accountService).getAccount(testUsername);

        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE,emailService.sendAccountDataRemember(incorrectUser));
    }

}