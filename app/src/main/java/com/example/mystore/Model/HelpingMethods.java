package com.example.mystore.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.MainActivity;
import com.example.mystore.MessagingActivity;
import com.example.mystore.R;
import com.example.mystore.videoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HelpingMethods {

    private Activity activity;

    public HelpingMethods(Activity activity) {
        this.activity = activity;
    }

    public void SnackBar(String msg, View v) {
        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        sb.show();
    }

    public void saveuser(String name, String photo, String address,String phone) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("photo", photo);
        editor.putString("address", address);
        editor.putString("phone", phone);
        editor.apply();
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

}
