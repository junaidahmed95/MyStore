package com.bringo.home.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fruit {

//    @SerializedName("fruits")
//    @Expose
//    private Data data;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("des")
    @Expose
    private String des;

    @SerializedName("cat_a")
    @Expose
    private String cat_a;


    @SerializedName("cat_b")
    @Expose
    private String cat_b;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("manual")
    @Expose
    private String manual;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }
//sdasdasd
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCat_a() {
        return cat_a;
    }

    public void setCat_a(String cat_a) {
        this.cat_a = cat_a;
    }

    public String getCat_b() {
        return cat_b;
    }

    public void setCat_b(String cat_b) {
        this.cat_b = cat_b;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
