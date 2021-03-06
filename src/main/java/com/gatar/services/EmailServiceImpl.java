package com.gatar.services;

import com.gatar.domain.Account;
import com.gatar.domain.Item;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service for sending an emails from predefined, outer email account.
 * Everything is set for GMail email, but can be easily changed in interface.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    DataServiceImpl dataServiceImpl;

    @Autowired
    AccountServiceImpl accountServiceImpl;


    /**
     * {@inheritDoc}
     * @param recipietEmail email on which will be send shopping list.
     * @param username username of list
     * @return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    public HttpStatus sendShoppingListEmail(String recipietEmail, String username){
        String subject = "lista zakupów";
        StringBuilder content = new StringBuilder("\n\n\tLista zakupów użytkownika " + username + "\n\n");

        Optional<List<Item>> shoppingList = Optional.ofNullable(dataServiceImpl.getShoppingList(username));
        if(!shoppingList.isPresent() || shoppingList.get().isEmpty()) return HttpStatus.NOT_ACCEPTABLE;

        for(Item item : shoppingList.get()){
            int quantityToBuy = item.getMinimumQuantity()-item.getQuantity();
            content.append(String.format("\t%d szt. \t%s, \t %s\n",quantityToBuy,item.getTitle(),item.getCategory()));
        }

        boolean isEmailSendSuccessful = sendEmail(recipietEmail,subject,content.toString());

        return (isEmailSendSuccessful)? HttpStatus.OK : HttpStatus.CONFLICT;
    }

    /**
     * {@inheritDoc}
     * Password is reset to random 6-digit string by private method resetAccountPassword in this class.
     * @param username username for reset password
     * @return return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    public HttpStatus sendAccountDataRemember(String username){
        final String subject = "Dane konta " + username + ", SpiżarkaApp";
        StringBuilder content = new StringBuilder("\n\tDane konta użytkownika " + username + " dla programu Spiżarka \n\n");

        Optional<Account> accountFromDatabase = Optional.ofNullable(accountServiceImpl.getAccount(username));
        if(!accountFromDatabase.isPresent()) return HttpStatus.NOT_ACCEPTABLE;

        Account account = accountFromDatabase.get();
        content.append("\t Wersja danych w bazie: \t" + account.getDataVersion().toString() + "\n\n");
        content.append("\t Login: \t\t" + username + "\n");
        content.append("\t Hasło: \t\t" + resetAccountPassword(username) + "\n");


        boolean isEmailSendSuccessful = sendEmail(account.getEmail(),subject,content.toString());

        return (isEmailSendSuccessful)? HttpStatus.OK : HttpStatus.CONFLICT;
    }

    private String resetAccountPassword(String username){
        String newPassword = generateRandomPassword();
        accountServiceImpl.changePassword(newPassword,username);
        return  newPassword;
    }

    private String generateRandomPassword(){
        Random randomGenereator = new Random();
        StringBuilder newPassword = new StringBuilder();

        for(int count = 0; count < 6; count++){
            int digit = randomGenereator.nextInt(10);
            newPassword.append(digit);
        }

        return newPassword.toString();
    }

    private boolean sendEmail(String recipietEmail, String subject, String content){

        Properties props = new Properties();
        props.put("mail.smtp.auth", AUTHENTICATION);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        switch (EMAIL_PROTOCOL_TYPE) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", true);
                break;
        }

        Authenticator authenticator = null;
        if (AUTHENTICATION) {
            props.put("mail.smtp.auth", AUTHENTICATION);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(EMAIL_ACCOUNT_LOGIN, EMAIL_ACCOUNT_PASSWORD);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(DEBUG);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(SENDING_FROM));
            InternetAddress[] address = {new InternetAddress(recipietEmail)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(content);
            Transport.send(message);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
