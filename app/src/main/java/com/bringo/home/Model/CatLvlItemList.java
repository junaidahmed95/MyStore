package com.bringo.home.Model;

public class CatLvlItemList {

    private String simplePID,desc,p_name,p_price,storeId,p_quantity = "1",actual_price,catName,productid,cat_id;
    int pos;
    private boolean isClicked = false;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getP_quantity() {
        return p_quantity;
    }

    public void setP_quantity(String p_quantity) {
        this.p_quantity = p_quantity;
    }


    String p_img;
    int position;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }


    public CatLvlItemList(String p_name, String p_price, String p_quantity, String p_img, String storeId) {
        this.p_name = p_name;
        this.p_price = p_price;
        this.storeId = storeId;
        this.p_quantity = p_quantity;
        this.p_img = p_img;
    }

    public CatLvlItemList(String p_name, String p_price, String p_quantity, String p_img, int pos, String productid, String storeId,String actual_price,String simplePID,String desc,String cat_id) {
        this.p_name = p_name;
        this.simplePID=simplePID;
        this.actual_price=actual_price;
        this.desc=desc;
        this.pos=pos;
        this.p_quantity = p_quantity;
        this.p_price = p_price;
        this.productid = productid;
        this.p_img = p_img;
        this.storeId = storeId;
        this.cat_id = cat_id;
    }



    public CatLvlItemList(String p_name, String p_price, String p_img, String productid, String storeId,String catName,String simplePID,String actual_price,String desc,String cat_id) {
        this.p_name = p_name;
        this.desc=desc;
        this.actual_price=actual_price;
        this.catName = catName;
        this.p_price = p_price;
        this.simplePID=simplePID;
        this.productid = productid;
        this.p_img = p_img;
        this.storeId = storeId;
        this.cat_id = cat_id;
    }


    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSimplePID() {
        return simplePID;
    }

    public void setSimplePID(String simplePID) {
        this.simplePID = simplePID;
    }

    public String getCatName() {
        return catName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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
