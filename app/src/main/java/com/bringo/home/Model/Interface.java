package com.bringo.home.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Interface {



    @GET("collection")
    Call<sample> editprice();
}
