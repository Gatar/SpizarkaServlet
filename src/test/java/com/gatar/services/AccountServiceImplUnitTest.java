package com.gatar.services;

import com.gatar.database.AccountDAO;
import com.gatar.domain.Account;
import com.gatar.domain.AccountDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AccountServiceImplUnitTest {

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Mock
    AccountDAO accountDAO;

    @Mock
    PasswordEncoder passwordEncoder;

    private String testUsername = "user1";
    private String testUsername2 = "user2";
    private AccountDTO testAccountDTO = new AccountDTO();
    private AccountDTO testAccountDTO2 = new AccountDTO();
    private Account testUserAccount = new Account();


    @Before
    public void setupMock(){
        accountServiceImpl = new AccountServiceImpl();
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
    public void putDataVersion() throws Exception {
        Long correctNewVersion = 6L;
        Long previousVersion = 1L;
        Long toHighVersion = 100L;

        AccountServiceImpl.AccountFeedback correct = accountServiceImpl.putDataVersion(correctNewVersion,testUsername);
        AccountServiceImpl.AccountFeedback toLow = accountServiceImpl.putDataVersion(previousVersion,testUsername);
        AccountServiceImpl.AccountFeedback toHigh = accountServiceImpl.putDataVersion(toHighVersion,testUsername);

        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountDatabaseNumberActualized,correct);
        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountDatabaseNumberIncorrect,toLow);
        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountDatabaseNumberIncorrect,toHigh);
    }

    @Test
    public void getDataVersion() throws Exception {
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
        Mockito.when(accountDAO.findByUsername(testUsername2)).thenReturn(null);
        Long correctAccount = 5L;
        Long incorrectAccount = -1L;

        Assert.assertEquals(correctAccount,accountServiceImpl.getDataVersion(testUsername));
        Assert.assertEquals(incorrectAccount,accountServiceImpl.getDataVersion(testUsername2));
    }

    @Test
    public void changePassword() throws Exception {
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
        Mockito.when(accountDAO.findByUsername(testUsername2)).thenReturn(null);

        Mockito.when(accountDAO.save(Matchers.any(Account.class))).thenReturn(null);

        AccountServiceImpl.AccountFeedback correctAccount = accountServiceImpl.changePassword("newPassword",testUsername);
        AccountServiceImpl.AccountFeedback incorrectAccount = accountServiceImpl.changePassword("newPassword",testUsername2);

        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountPasswordActualized,correctAccount);
        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountDoesntExist,incorrectAccount);
    }

    @Test
    public void saveAccount() throws Exception {
        Mockito.when(accountDAO.findByUsername(testUsername)).thenReturn(testUserAccount);
        Mockito.when(accountDAO.findByUsername(testUsername2)).thenReturn(null);
        Mockito.when(accountDAO.save(Matchers.any(Account.class))).thenReturn(null);

        AccountServiceImpl.AccountFeedback newAccount = accountServiceImpl.saveAccount(testAccountDTO2);
        AccountServiceImpl.AccountFeedback existingAccount = accountServiceImpl.saveAccount(testAccountDTO);

        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountCreated,newAccount);
        Assert.assertEquals(AccountServiceImpl.AccountFeedback.AccountAlreadyExist,existingAccount);
    }

}