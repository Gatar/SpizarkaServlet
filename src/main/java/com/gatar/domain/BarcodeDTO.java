package com.gatar.domain;

import java.util.Objects;

/**
 * Data Transfer Object used to receive/send one barcodeValue from/to phone.
 */
public class BarcodeDTO {

    private String barcodeValue;

    /**
     * Item id number assigned in internal database in phone of user.
     */
    private Long idItemAndroid;

    public String getBarcodeValue() {
        return barcodeValue;
    }

    public void setBarcodeValue(String barcodeValue) {
        this.barcodeValue = barcodeValue;
    }

    public Long getIdItemAndroid() {
        return idItemAndroid;
    }

    public void setIdItemAndroid(Long idItemAndroid) {
        this.idItemAndroid = idItemAndroid;
    }

    public BarcodeDTO() {
    }

    public Barcode toBarcode(){
        Barcode barcode = new Barcode();
        barcode.setBarcodeValue(this.barcodeValue);
        return barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarcodeDTO)) return false;
        BarcodeDTO that = (BarcodeDTO) o;
        return Objects.equals(getBarcodeValue(), that.getBarcodeValue()) &&
                Objects.equals(getIdItemAndroid(), that.getIdItemAndroid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBarcodeValue(), getIdItemAndroid());
    }
}
