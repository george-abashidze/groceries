package com.example.groceries.data.model;

import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Product;

public class JoinProductCart{
    private long id;
    private long categoryId;
    private String name;
    private float unitPrice;
    private int quantity;
    private int inCartCount;
    private long cartId;
    private String categoryName;
    private boolean stateChanged;

    public boolean isStateChanged() {
        return stateChanged;
    }

    public void setStateChanged(boolean stateChanged) {
        this.stateChanged = stateChanged;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getInCartCount() {
        return inCartCount;
    }

    public void setInCartCount(int inCartCount) {
        this.inCartCount = inCartCount;
    }

    public Cart cartItemFromObject(){
        return new Cart(cartId,id,inCartCount);
    }

    public Product productFromObject(){
        return new Product(id,categoryId,name,unitPrice,quantity);
    }

}
