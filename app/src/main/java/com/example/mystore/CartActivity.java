package com.example.mystore;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Adapter.CartAdapter;

import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;

public class CartActivity extends AppCompatActivity {

    public static CartAdapter cartAdapter;
    private RecyclerView mCartRecyclerView;
    public static TextView mTxtView_TotalPrice;
    public static CardView mcardview2;
    private Button mcheckBtn;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mCartRecyclerView = findViewById(R.id.cartRecyclerView);
        mcheckBtn = findViewById(R.id.checkBtn);
        //okay
        mTxtView_TotalPrice = findViewById(R.id.totalPrice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mcardview2 = findViewById(R.id.cardVew1);
        mCartRecyclerView.setLayoutManager(layoutManager);
        mActionBarToolbar = findViewById(R.id.bar);
        mActionBarToolbar.setTitle("My Cart");
        cartAdapter = new CartAdapter(selectedProducts, CartActivity.this,"activity");
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


                startActivity(new Intent(CartActivity.this, OrderSummaryActivity.class));
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
        }
        else{

        }

        return true;
    }

}
