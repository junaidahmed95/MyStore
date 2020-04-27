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
import com.example.mystore.Model.ConnectionDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class CartActivity extends AppCompatActivity {

    private CartAdapter cartAdapter;
    private RecyclerView mCartRecyclerView;
    public static TextView mTxtView_TotalPrice;
    public static CardView mcardview2;
    private Button mcheckBtn;
    private List<CatLvlItemList> preferenceList;
    Toolbar mActionBarToolbar;
    private String cat_Name, store_ID,ownerID,ownerImage,ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mCartRecyclerView = findViewById(R.id.cartRecyclerView);
        mcheckBtn = findViewById(R.id.checkBtn);

        cat_Name = getIntent().getStringExtra("catName");
        store_ID = getIntent().getStringExtra("StID");
        ownerName = getIntent().getStringExtra("stname");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");
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




        mcheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectionDetector connectionDetector = new ConnectionDetector(CartActivity.this);
                if(connectionDetector.isConnected()){
                    Intent sumInt = new Intent(CartActivity.this, OrderSummaryActivity.class);
                    sumInt.putExtra("from","activity");
                    sumInt.putExtra("totalP",mTxtView_TotalPrice.getText().toString());
                    sumInt.putExtra("storeid", store_ID);
                    sumInt.putExtra("stname",ownerName);
                    sumInt.putExtra("ownerID",ownerID);
                    sumInt.putExtra("ownerImage",ownerImage);
                    startActivity(sumInt);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    Toast.makeText(CartActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void GoBack() {
        Intent intent = new Intent(CartActivity.this, SubCatActivity.class);
        intent.putExtra("storeid", store_ID);
        intent.putExtra("catName", cat_Name);
        intent.putExtra("stname",ownerName);
        intent.putExtra("ownerID",ownerID);
        intent.putExtra("ownerImage",ownerImage);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GoBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
GoBack();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
