package com.gatar.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class ItemDTO {

    /**
     * Item id number assigned in internal database in phone of user.
     */
    private Long idItemAndroid;

    private String title;

    private String category;

    private Integer quantity;

    private Integer minimumQuantity;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
