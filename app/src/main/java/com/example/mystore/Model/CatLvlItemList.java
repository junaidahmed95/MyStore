package com.example.mystore.Model;

public class CatLvlItemList {

    String p_name;
    String p_price;
    String p_quantity = "1";
    int pos;
    String actual_price;
    String lol;
    String catName;
    String storeid;
    String productid;

    public String getP_quantity() {
        return p_quantity;
    }

    public void setP_quantity(String p_quantity) {
        this.p_quantity = p_quantity;
    }


    String p_img;
    int position;

    public CatLvlItemList(String p_name, String p_price, String productid, String p_img, String catName) {
        this.p_name = p_name;
        this.catName = catName;
        this.p_price = p_price;
        this.productid = productid;
        this.p_img = p_img;
    }


    public CatLvlItemList(String p_name, String p_price, String productid, String p_img) {
        this.p_name = p_name;
        this.p_price = p_price;
        this.productid = productid;
        this.p_img = p_img;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }





    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getP_img() {
        return p_img;
    }

    public void setP_img(String p_img) {
        this.p_img = p_img;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CatLvlItemList(String p_name, String p_price, String p_quantity, String p_img,int position,String actual_price,String productid) {
        this.p_name = p_name;
        this.p_price = p_price;
        this.p_quantity = p_quantity;
        this.p_img = p_img;
        this.pos = position;
        this.actual_price = actual_price;
        this.productid = productid;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
