package com.example.mystore.Model;

public class Category {

    private String catName;
    private String catImage;

    public Category(String catImage,String catName) {
        this.catName = catName;
        this.catImage = catImage;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}

