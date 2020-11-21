package com.bringo.home.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Interface {



    ////asdasdsadsadsdasdsd
    @GET("main_catogory/collection")
    Call<sample> editprice();


    @GET("featured/stores")
    Call<List<FeaturedStoreList>> featuredlist();

}
