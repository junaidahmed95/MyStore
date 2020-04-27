package com.bringo.home.Model;

public class AllStore {

    String Image;
    String name;
    String distance;
    String str_id;

    public AllStore(String image, String name, String distance, String str_id) {
        Image = image;
        this.name = name;
        this.distance = distance;
        this.str_id = str_id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }


}
