package com.example.mystore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Adapter.OrderAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.OrderHistory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WishActivity extends AppCompatActivity {
    private List<CatLvlItemList> myFavList;
    RecyclerView mwishrecyclerView;
    Toolbar mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        mwishrecyclerView = findViewById(R.id.wishrecyclerView);
        mtoolbar = findViewById(R.id.appBar);
        mtoolbar.setTitle("My Wishlist");
        //toolbar.setTitleMargin(0,0,5,0);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GetFavData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mwishrecyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(myFavList, this, false, true);
        mwishrecyclerView.setAdapter(orderAdapter);


    }
    private void GetFavData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Myfav", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("favlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            myFavList = gson.fromJson(json, type);

            if (myFavList == null) {
                myFavList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(WishActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
