package com.example.mystore.Model;

public class Cart {

    private int proImage,quantity = 1,proPrice,proTotal;
    private String proName;

    public Cart(int proImage, int proPrice, int proTotal, String proName) {
        this.proImage = proImage;
        this.proPrice = proPrice;
        this.proTotal = proTotal;
        this.proName = proName;
    }

    public int getProImage() {
        return proImage;
    }

    public void setProImage(int proImage) {
        this.proImage = proImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProPrice() {
        return proPrice;
    }

    public void setProPrice(int proPrice) {
        this.proPrice = proPrice;
    }

    public int getProTotal() {
        return proTotal;
    }

    public void setProTotal(int proTotal) {
        this.proTotal = proTotal;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
