package com.example.mystore;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Adapter.CartAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;

public class CartActivity extends AppCompatActivity {

    private CartAdapter cartAdapter;
    private RecyclerView mCartRecyclerView;
    public static TextView mTxtView_TotalPrice;
    public static CardView mcardview2;
    private Button mcheckBtn;
    private List<CatLvlItemList> preferenceList;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mCartRecyclerView = findViewById(R.id.cartRecyclerView);
        mcheckBtn = findViewById(R.id.checkBtn);

        mTxtView_TotalPrice = findViewById(R.id.totalPrice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mcardview2 = findViewById(R.id.cardVew1);
        mCartRecyclerView.setLayoutManager(layoutManager);
        mActionBarToolbar = findViewById(R.id.bar);
        mActionBarToolbar.setTitle("My Cart");
        GetCartData();

        cartAdapter = new CartAdapter(preferenceList, CartActivity.this,"activity");
        mCartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (selectedProducts.size() == 0) {
            mcardview2.setVisibility(View.GONE);
        }


        mcheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sumInt = new Intent(CartActivity.this, OrderSummaryActivity.class);
                sumInt.putExtra("from","activity");
                startActivity(sumInt);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
        }


        return true;
    }

    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Mycart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }
        } catch (Exception e) {
            Toast.makeText(CartActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
