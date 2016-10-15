package com.gatar.domain;

import java.util.Objects;

/**
 * Data Transfer Object used to receive/send one barcode from/to phone.
 */
public class BarcodeDTO {

    private String barcode;

    /**
     * Item id number assigned in internal database in phone of user.
     */
    private Long idItemAndroid;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getIdItemAndroid() {
        return idItemAndroid;
    }

    public void setIdItemAndroid(Long idItemAndroid) {
        this.idItemAndroid = idItemAndroid;
    }

    public Barcode toBarcode(){
        Barcode barcode = new Barcode();
        barcode.setBarcode(this.barcode);
        return barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarcodeDTO)) return false;
        BarcodeDTO that = (BarcodeDTO) o;
        return Objects.equals(getBarcode(), that.getBarcode()) &&
                Objects.equals(getIdItemAndroid(), that.getIdItemAndroid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBarcode(), getIdItemAndroid());
    }
}
