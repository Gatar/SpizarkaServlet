package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;


public class AccountServiceUnitTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountDAO accountDAO;

    private String testUsername = "user1";
    private String testUsername2 = "user2";
    private AccountDTO testAccountDTO = new AccountDTO();
    private AccountDTO testAccountDTO2 = new AccountDTO();
    private Account testUserAccount = new Account();


    @Before
    public void setupMock(){
        accountService = new AccountService();
        MockitoAnnotations.initMocks(this);

        testAccountDTO.setUsername(testUsername);
        testAccountDTO.setEmail("email@email.com");
        testAccountDTO.setPassword("pass1");

        testAccountDTO2.setUsername(testUsername2);
        testAccountDTO2.setPassword("pass1");
        testAccountDTO2.setEmail("mail@mail.com");

        testUserAccount = testAccountDTO.toAccount();
        testUserAccount.setDataVersion(5L);
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
    }


    @Test
    public void actualizeDataVersion() throws Exception {
        Long correctNewVersion = 6L;
        Long previousVersion = 1L;
        Long toHighVersion = 100L;

        AccountService.AccountFeedback correct = accountService.actualizeDataVersion(correctNewVersion,testUsername);
        AccountService.AccountFeedback toLow = accountService.actualizeDataVersion(previousVersion,testUsername);
        AccountService.AccountFeedback toHigh = accountService.actualizeDataVersion(toHighVersion,testUsername);

        Assert.assertEquals(AccountService.AccountFeedback.AccountDatabaseNumberActualized,correct);
        Assert.assertEquals(AccountService.AccountFeedback.AccountDatabaseNumberIncorrect,toLow);
        Assert.assertEquals(AccountService.AccountFeedback.AccountDatabaseNumberIncorrect,toHigh);
    }

    @Test
    public void saveAccount() throws Exception {
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
        Mockito.when(accountDAO.findByUsername(testUsername2)).thenReturn(null);

        AccountService.AccountFeedback newAccount = accountService.saveAccount(testAccountDTO2);
        AccountService.AccountFeedback existingAccount = accountService.saveAccount(testAccountDTO);

        Assert.assertEquals(AccountService.AccountFeedback.AccountCreated,newAccount);
        Assert.assertEquals(AccountService.AccountFeedback.AccountAlreadyExist,existingAccount);
    }

    @Test
    public void isAccountExist() throws Exception {
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
        Mockito.when(accountDAO.findByUsername(testUsername2)).thenReturn(null);

        Assert.assertTrue(accountService.isAccountExist(testUsername));
        Assert.assertFalse(accountService.isAccountExist(testUsername2));
    }

}