package com.gatar.domain;

import java.util.Objects;

/**
 * Data Transfer Object used to receive/send one item from/to phone.
 */
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

    public ItemDTO() {
    }

    public Item toItem(){
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setQuantity(quantity);
        item.setMinimumQuantity(minimumQuantity);
        item.setIdItemAndroid(idItemAndroid);
        item.setCategory(category);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDTO)) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(getIdItemAndroid(), itemDTO.getIdItemAndroid()) &&
                Objects.equals(getTitle(), itemDTO.getTitle()) &&
                Objects.equals(getCategory(), itemDTO.getCategory()) &&
                Objects.equals(getQuantity(), itemDTO.getQuantity()) &&
                Objects.equals(getMinimumQuantity(), itemDTO.getMinimumQuantity()) &&
                Objects.equals(getDescription(), itemDTO.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdItemAndroid(), getTitle(), getCategory(), getQuantity(), getMinimumQuantity(), getDescription());
    }
}
