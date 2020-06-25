package com.bringo.home.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "https://bringo.biz/api/";
    //sdasdasd

    public  static RetrofitClient mInstance;
    private Retrofit retrofit;

    public RetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1 , TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized  RetrofitClient getmInstance(){
        if (mInstance == null){

            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public Interface getApi(){
        return retrofit.create(Interface.class);

    }
}