package com.bringo.home.Model;

import android.media.MediaPlayer;

public class Chats {
    private  String chatid,message,type,delivery,sender;
    private long time;
    private boolean seen;
    private Object timestamp;
    private MediaPlayer chatMediaPlayer;
    private boolean upload = true;
    private String bath,bed,price,prop,unit,forWhat,status,delete,userprofile,getfreetext,orderID,storeID;
    private String phone,allimg,propertyname,locationltlng,location,propid;
    private boolean isPause;
    String address,totalPrice;
    int totalProduct;
    public MediaPlayer getChatMediaPlayer() {
        return chatMediaPlayer;
    }
    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
    public void setChatMediaPlayer(MediaPlayer chatMediaPlayer) {
        this.chatMediaPlayer = chatMediaPlayer;
    }
    public String getAddress() {
        return address;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public Chats(String chatid, String message, String type, long time, String delivery, String sender, boolean seen) {
        this.chatid = chatid;
        this.message = message;
        this.type = type;
        this.time = time;
        this.delivery = delivery;
        this.sender = sender;
        this.seen = seen;
    }

    public Chats(String type, Object time, String sender, boolean upload,String message) {
        this.type = type;
        this.timestamp = time;
        this.sender = sender;
        this.upload = upload;
        this.message = message;
    }

    public Chats(String type, long time, String sender, boolean upload,String message) {
        this.type = type;
        this.time = time;
        this.sender = sender;
        this.upload = upload;
        this.message = message;
    }

    public Chats() {
    }

    public String getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(String userprofile) {
        this.userprofile = userprofile;
    }

    public String getGetfreetext() {
        return getfreetext;
    }

    public void setGetfreetext(String getfreetext) {
        this.getfreetext = getfreetext;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getForWhat() {
        return forWhat;
    }

    public void setForWhat(String forWhat) {
        this.forWhat = forWhat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
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

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public String getLocationltlng() {
        return locationltlng;
    }

    public void setLocationltlng(String locationltlng) {
        this.locationltlng = locationltlng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPropid() {
        return propid;
    }

    public void setPropid(String propid) {
        this.propid = propid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}