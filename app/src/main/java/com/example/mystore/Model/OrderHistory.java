package com.example.mystore.Model;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory {
    String mtxt_price, mtxt_qty, mtxt_totalproducts, mtxt_address, mtxt_day, mtxt_time, p_id, image, title;
    String id, userid, uaddress, day, orderid;
    int qtyplus;



    public void setGetorderbykeylist(List<OrderHistory> getorderbykeylist) {
        this.getorderbykeylist = getorderbykeylist;
    }

    public OrderHistory(String mtxt_price, String mtxt_qty, String mtxt_totalproducts, String mtxt_day, String image, String title, String userid, String uaddress, String orderid, String ptotalprice, String act_price) {
        this.mtxt_price = mtxt_price;
        this.mtxt_qty = mtxt_qty;
        this.mtxt_totalproducts = mtxt_totalproducts;
        this.mtxt_day = mtxt_day;
        this.image = image;
        this.title = title;
        this.userid = userid;
        this.uaddress = uaddress;
        this.orderid = orderid;
        this.ptotalprice = ptotalprice;
        this.act_price = act_price;
    }

    String ptotalprice, act_price;


    List<OrderHistory> productslist;

    public OrderHistory(String id, String userid, String uaddress, String day, String orderid, List<OrderHistory> productslist) {
        this.id = id;
        this.userid = userid;
        this.uaddress = uaddress;
        this.day = day;
        this.orderid = orderid;
        this.productslist = productslist;
    }


    public OrderHistory(String pid, String qty, String price, String image, String title) {
        this.p_id = pid;
        this.mtxt_qty = qty;
        this.mtxt_price = price;
        this.image = image;
        this.title = title;


    }


    public OrderHistory(String mtxt_price, int mtxt_qty, String mtxt_totalproducts, String mtxt_address, String mtxt_day, String mtxt_time, String title, List<OrderHistory> productslist) {
        this.mtxt_price = mtxt_price;
        this.qtyplus = mtxt_qty;
        this.mtxt_totalproducts = mtxt_totalproducts;
        this.mtxt_address = mtxt_address;
        this.mtxt_day = mtxt_day;
        this.mtxt_time = mtxt_time;
        this.title = title;
        this.productslist = productslist;
    }

    List<OrderHistory> getorderbykeylist = new ArrayList<>();


    public OrderHistory(String orderid, List<OrderHistory> getorderbykeylist) {
        this.orderid = orderid;
        this.getorderbykeylist = getorderbykeylist;
    }

    public int getQtyplus() {
        return qtyplus;
    }

    public List<OrderHistory> getProductslist() {
        return productslist;
    }

    public void setProductslist(List<OrderHistory> productslist) {
        this.productslist = productslist;
    }

    public void setQtyplus(int qtyplus) {
        this.qtyplus = qtyplus;
    }

    public String getMtxt_price() {
        return mtxt_price;
    }

    public void setMtxt_price(String mtxt_price) {
        this.mtxt_price = mtxt_price;
    }

    public String getMtxt_qty() {
        return mtxt_qty;
    }

    public void setMtxt_qty(String mtxt_qty) {
        this.mtxt_qty = mtxt_qty;
    }

    public String getMtxt_totalproducts() {
        return mtxt_totalproducts;
    }

    public void setMtxt_totalproducts(String mtxt_totalproducts) {
        this.mtxt_totalproducts = mtxt_totalproducts;
    }

    public String getMtxt_address() {
        return mtxt_address;
    }

    public void setMtxt_address(String mtxt_address) {
        this.mtxt_address = mtxt_address;
    }

    public String getMtxt_day() {
        return mtxt_day;
    }

    public void setMtxt_day(String mtxt_day) {
        this.mtxt_day = mtxt_day;
    }

    public String getMtxt_time() {
        return mtxt_time;
    }

    public void setMtxt_time(String mtxt_time) {
        this.mtxt_time = mtxt_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPtotalprice() {
        return ptotalprice;
    }

    public void setPtotalprice(String ptotalprice) {
        this.ptotalprice = ptotalprice;
    }

    public String getAct_price() {
        return act_price;
    }

    public void setAct_price(String act_price) {
        this.act_price = act_price;
    }

    public List<OrderHistory> getGetorderbykeylist() {
        return getorderbykeylist;
    }


}
