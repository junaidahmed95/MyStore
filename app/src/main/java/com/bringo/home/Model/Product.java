package com.bringo.home.Model;

public class Product {

    private int productImage, quantity = 1,position,productPrice,prototal;

    private String productCategory;

    public Product(int productImage, int productPrice, String productCategory,int pos) {
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.position = pos;

    }


    public Product(int productImage, int productPrice, String productCategory) {
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
    }

    public Product(int productImage, int quantity, int productPrice, String productCategory) {
        this.productImage = productImage;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
    }

    public Product() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProductImage() {
        return productImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
