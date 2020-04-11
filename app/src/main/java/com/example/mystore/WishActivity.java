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
    private List<CatLvlItemList> wishlist;
    RecyclerView mwishrecyclerView;
    Toolbar mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        mwishrecyclerView = findViewById(R.id.wishrecyclerView );
        mtoolbar = findViewById(R.id.appBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mwishrecyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
        }.getType();
        wishlist = gson.fromJson(json, type);

        if (wishlist == null) {
            wishlist = new ArrayList<>();
        } else {

            OrderAdapter orderAdapter = new OrderAdapter( wishlist, this,false,true);
            mwishrecyclerView.setAdapter(orderAdapter);
            setSupportActionBar(mtoolbar);
            getSupportActionBar().setTitle("Wish List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
