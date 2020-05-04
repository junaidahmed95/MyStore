package com.bringo.home.Model;

public class Data {
    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;

    private String phone,allimg,price,propertyname,bedroom,bathroom,user_id,appid,locationltlng,sqft,location,status,freetext;

    public Data(){}
    public Data(String user, int icon, String body, String title, String sented)
    {
        this.user=user;
        this.body=body;
        this.icon=icon;
        this.title=title;
        this.sented=sented;
    }

    public Data(String user, String phone, String allimg, String price, String propertyname, String bedroom, String bathroom, String user_id, String appid, String locationltlng, String sqft, String location, String status, String Sented, String title, int icon, String Body, String Freetext) {
        this.phone = phone;
        this.allimg = allimg;
        this.price = price;
        this.propertyname = propertyname;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.sented = Sented;
        this.user_id = user_id;
        this.appid = appid;
        this.locationltlng = locationltlng;
        this.sqft = sqft;
        this.location = location;
        this.status = status;
        this.title = title;
        this.user = user;
        this.icon = icon;
        this.body = Body;
        this.freetext = Freetext;
    }

    public String getFreetext() {
        return freetext;
    }

    public void setFreetext(String freetext) {
        this.freetext = freetext;
    }

    public String getUser() {
        return user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAllimg() {
        return allimg;
    }

    public void setAllimg(String allimg) {
        this.allimg = allimg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getLocationltlng() {
        return locationltlng;
    }

    public void setLocationltlng(String locationltlng) {
        this.locationltlng = locationltlng;
    }

    public String getSqft() {
        return sqft;
    }

    public void setSqft(String sqft) {
        this.sqft = sqft;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser1() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}