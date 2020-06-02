package com.bringo.home.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class HelpingMethods {

    private Activity activity;

    public HelpingMethods(Activity activity) {
        this.activity = activity;
    }

    public void SnackBar(String msg, View v) {
        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        sb.show();
    }

    public void saveuser(String name, String photo, String phone,String email) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("photo", photo);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.apply();
    }

    public String GetUEmail() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String sid = sharedPreferences.getString("email", null);
        return sid;
    }

    public String GetUPhone() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String sid = sharedPreferences.getString("phone", null);
        return sid;
    }

    public String GetUImage() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String sid = sharedPreferences.getString("photo", null);
        return sid;
    }

    public String GetUName() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String sid = sharedPreferences.getString("name", null);
        return sid;
    }


    public void SaveCartCount(int mCount,String dBnAme){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dBnAme, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count",mCount);
        editor.apply();
    }

    public int GetCartCount(String dBnAme) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dBnAme, Context.MODE_PRIVATE);
        int cartCount = sharedPreferences.getInt("count", 0);
        return cartCount;
    }

//For multiple stores use DBName as parameter take input store_ID and save in DBNAme
    public void SaveStoreData(String sid,String sname,String simage,String suid){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("RecentStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sid",sid);
        editor.putString("name",sname);
        editor.putString("image",simage);
        editor.putString("uid",suid);
        editor.apply();
    }

    public String GetStoreID() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("RecentStore", Context.MODE_PRIVATE);
        String sid = sharedPreferences.getString("sid", null);
        return sid;
    }

    public String GetStoreName() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("RecentStore", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        return name;
    }

    public String GetStoreImage() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("RecentStore", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("image", null);
        return image;
    }

    public String GetStoreUID() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("RecentStore", Context.MODE_PRIVATE);
        String uiD = sharedPreferences.getString("uid", null);
        return uiD;
    }

    public int GetCartTotal(String Dbname) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Dbname, Context.MODE_PRIVATE);
        int sid = sharedPreferences.getInt("amount", 0);
        return sid;
    }


    public void SaveCartTotal(int total,String Dbname){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Dbname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("amount",total);
        editor.apply();
    }

}
