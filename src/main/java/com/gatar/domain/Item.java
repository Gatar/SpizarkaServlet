package com.gatar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Item entity class. For one item could exists many barcodes, so each bitem are connected by @OneToMany with barcodes entities.
 * If table "ITEMS" doesn't exist, application creates it automaticaly.
 */

@Entity(name="ITEMS")
public class Item implements Serializable {


    @Id
    @Column(name = "ID_ITEM")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idItem;

    /**
     * Item id number assigned in internal database in phone of user.
     */
    @Column(name = "ID_ITEM_ANDROID")
    private Long idItemAndroid;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "MINIMUM_QUANTITY")
    private Integer minimumQuantity;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private Account account;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Barcode> barcodes;

    public Item() {
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Long getIdItemAndroid() {
        return idItemAndroid;
    }

    public void setIdItemAndroid(Long idItemAndroid) {
        this.idItemAndroid = idItemAndroid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Barcode> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<Barcode> barcodes) {
        this.barcodes = barcodes;
    }

    public ItemDTO toItemDTO(){
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setTitle(title);
        itemDTO.setCategory(category);
        itemDTO.setIdItemAndroid(idItemAndroid);
        itemDTO.setDescription(description);
        itemDTO.setMinimumQuantity(minimumQuantity);
        itemDTO.setQuantity(quantity);
        return itemDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getIdItem(), item.getIdItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdItem());
    }
}
