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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_BARCODE")
    private Long idBarcode;

    @Column(name="BARCODE")
    private String barcode;

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

    public Long getIdBarcode() {
        return idBarcode;
    }

    public void setIdBarcode(Long idBarcode) {
        this.idBarcode = idBarcode;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BarcodeDTO toBarcodeDTO(){
        BarcodeDTO barcodeDTO = new BarcodeDTO();
        barcodeDTO.setBarcode(barcode);
        barcodeDTO.setIdItemAndroid(item.getIdItemAndroid());
        return barcodeDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Barcode)) return false;
        Barcode barcode = (Barcode) o;
        return Objects.equals(getIdBarcode(), barcode.getIdBarcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdBarcode());
    }
}
