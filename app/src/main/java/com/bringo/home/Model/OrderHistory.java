package com.bringo.home.Model;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory {

    String storeName,storeID,orderID,orderPrice,orderDate,address,storeImage;

    public OrderHistory(String storeName, String storeID, String orderID, String orderPrice, String orderDate, String address, String storeImage) {
        this.storeName = storeName;
        this.storeID = storeID;
        this.orderID = orderID;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.address = address;
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }
}
