package com.gatar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Barcode entity class. For one item could exists many barcodes, so each barcode are connected by @ManyToOne with Item entity.
 * If table "BARCODES" doesn't exist, application creates it automaticaly.
 */

@Entity(name="BARCODES")
public class Barcode implements Serializable{

    @Id
    @Column(name="BARCODE")
    @NotNull
    private String barcode;

    /**
     * Used ONLY for receive id of connected item from POST methods. Base on this id we could create connection with correct item.
     */
    private Long id;

    @ManyToOne
    @JoinColumn(name="ID_ITEM")
    @JsonIgnore
    private Item item;

    public Barcode() {
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Barcode)) return false;
        Barcode barcode1 = (Barcode) o;
        return Objects.equals(getBarcode(), barcode1.getBarcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBarcode());
    }
}
