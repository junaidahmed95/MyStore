package com.bringo.home.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class sample {

    @SerializedName("categories")
    @Expose
    private ArrayList<Categories> categories;
    //sdasdasd
    ////asdasdsadsadsdasdsd////asdasdsadsadsdasdsd////asdasdsadsadsdasdsd
    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }
//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - -
    @SerializedName("mmh_store")
    @Expose
    private ArrayList<Listmmh> mmh_store;

    public ArrayList<Listmmh> getMmh() {
        return mmh_store;
    }

    public void setMmh(ArrayList<Listmmh> mmh) {
        this.mmh_store = mmh;
    }


//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - -

    @SerializedName("FruitVeg")
    @Expose
    private ArrayList<Fruit> fruits;

    public ArrayList<Fruit> getFruits() {
        return fruits;
    }

    public void setFruits(ArrayList<Fruit> fruits) {
        this.fruits = fruits;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - - - - - - - - - - - - - -



}
