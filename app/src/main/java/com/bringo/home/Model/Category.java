package com.bringo.home.Model;

public class Category {

    private String catName;
    private String catImage,cat_id;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public Category(String catImage, String catName, String cat_id) {
        this.catName = catName;
        this.catImage = catImage;
        this.cat_id = cat_id;
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

