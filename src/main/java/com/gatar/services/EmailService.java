package com.gatar.services;

import com.gatar.domain.Account;
import com.gatar.domain.Item;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service for sending an emails from predefined, outer email account (settings are in {@see EmailServiceSettings}).
 * Everything is set for GMail email, but can be easily changed in interface.
 */
@Service
public class EmailService implements EmailServiceSettings{

    @Autowired
    DataService dataService;

    @Autowired
    AccountService accountService;

    /**
     * Sending shopping list (items with quantity below minimum).
     * @param recipietEmail email on which will be send shopping list.
     * @param username username of list
     * @return return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    public HttpStatus sendShoppingListEmail(String recipietEmail, String username){
        String subject = "lista zakupów";
        StringBuilder content = new StringBuilder("\n\n\tLista zakupów użytkownika " + username + "\n\n");

        Optional<List<Item>> shoppingListFromDatabase = Optional.ofNullable(dataService.getShoppingList(username));
        if(!shoppingListFromDatabase.isPresent()) return HttpStatus.NOT_ACCEPTABLE;

        List<Item> shoppingList = shoppingListFromDatabase.get();
        for(Item item : shoppingList){
            int quantityToBuy = item.getMinimumQuantity()-item.getQuantity();
            content.append(String.format("\t%d szt. \t%s, \t %s\n",quantityToBuy,item.getTitle(),item.getCategory()));
        }

        return (sendEmail(recipietEmail,subject,content.toString()))? HttpStatus.OK : HttpStatus.CONFLICT;

    }
    /**
     * Sending shopping list (items with quantity below minimum) to email input by user in phone app.
     * @param username username of list
     * @return return HttpStatus.OK when everything was OK and HttpStatus.CONFLICT if there were Exceptions in sendEmail method.
     */
    public HttpStatus sendAccountDataRemember(String username){
        String subject = "Dane konta " + username + ", SpiżarkaApp";
        StringBuilder content = new StringBuilder("\n\tDane konta użytkownika " + username + " dla programu Spiżarka \n\n");

        Optional<Account> accountFromDatabase = Optional.ofNullable(accountService.getAccount(username));
        if(!accountFromDatabase.isPresent()) return HttpStatus.NOT_ACCEPTABLE;

        Account account = accountFromDatabase.get();
        content.append("\t Login: \t\t" + username + "\n");
        content.append("\t Hasło: \t\t" + account.getPassword() + "\n");
        content.append("\t Wersja danych w bazie: \t" + account.getDataVersion().toString() + "\n");

        return (sendEmail(account.getEmail(),subject,content.toString()))? HttpStatus.OK : HttpStatus.CONFLICT;
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
        }

        return false;
    }


}
