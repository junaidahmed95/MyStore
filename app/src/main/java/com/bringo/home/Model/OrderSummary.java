package com.bringo.home.Model;

public class OrderSummary {

    private String proName, proQty, proPrice;

    public OrderSummary(String proName, String proQty, String proPrice) {
        this.proName = proName;
        this.proQty = proQty;
        this.proPrice = proPrice;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProQty() {
        return proQty;
    }

    public void setProQty(String proQty) {
        this.proQty = proQty;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }
}
