package com.gatar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ACCOUNTS")
public class Account implements Serializable {

    @Id
    @Column(name = "ID_USER")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer userID;

    @Column(name = "LOGIN", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    /**
     * Version of data in database. After each data modification done by user in phone app,
     * internal phone database increase data version by one and send it here.
     * Each phone app before any internal database modification check is their version actual with this.
     * If not, internal database is actualized from cloud and then modified.
     *
     */
    @Column(name = "DATA_VERSION")
    private Long dataVersion;

    /**
     * Contains always "USER", implement for authorities purpose.
     */

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountDTO toAccountDTO(){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setEmail(email);
        return accountDTO;
    }

    /**
     * As ROLE for each user there is used its unique username. Example: when someone tries to access with useename Fred to path /George/getAllItems,
     * Fred account ROLE will be also 'Fred' and access to /George/** path require ROLE 'George', so access will be denited.
     * @return username as role.
     */
    public String getRole() {
        return username;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


}
