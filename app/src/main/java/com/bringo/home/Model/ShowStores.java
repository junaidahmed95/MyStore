package com.bringo.home.Model;

public class ShowStores {
    String store_name;
    String id,distance;
    private String address;
    String uid;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public ShowStores(String store_name, String id, String uid, String store_image,String distance) {
        this.store_name = store_name;
        this.id = id;
        this.uid = uid;
        this.distance = distance;
        this.store_image = store_image;
    }

    public ShowStores(String store_name, String id, String uid, String store_image,String distance,String address) {
        this.store_name = store_name;
        this.address=address;
        this.id = id;
        this.uid = uid;
        this.distance = distance;
        this.store_image = store_image;
    }

    String store_image;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ShowStores(String store_name, String id, String uid) {
        this.store_name = store_name;
        this.id = id;
        this.uid = uid;
    }
}
