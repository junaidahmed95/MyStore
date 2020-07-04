package com.bringo.home.Model;

public class AddressClass {

    String userAddress;
    private String lat,lng;
    boolean currentAddress;

    public AddressClass(String userAddress, boolean currentAddress,String lat,String lng) {
        this.userAddress = userAddress;
        this.lat=lat;
        this.lng=lng;
        this.currentAddress = currentAddress;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(boolean currentAddress) {
        this.currentAddress = currentAddress;
    }
}
