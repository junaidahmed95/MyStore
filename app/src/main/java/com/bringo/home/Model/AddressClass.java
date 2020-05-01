package com.bringo.home.Model;

public class AddressClass {

    String userAddress;
    boolean currentAddress;

    public AddressClass(String userAddress, boolean currentAddress) {
        this.userAddress = userAddress;
        this.currentAddress = currentAddress;
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
