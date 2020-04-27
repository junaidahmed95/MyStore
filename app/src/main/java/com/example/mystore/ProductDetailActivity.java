package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.HelpingMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.SubCatActivity.setupBadge;

public class ProductDetailActivity extends AppCompatActivity {

    private SliderView sliderView;
    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private String storeID;
    private String spID;
    private TextView mName, mprice;
    private ImageView mpImage;
    private Button mremoveToCart, maddToCart, mcheckout;
    private HelpingMethods helpingMethods;
    private int position;
    private String pID, pImage, pPrice, pName, StID, catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.viewBar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sliderView = findViewById(R.id.imageSlider);
        mpImage = findViewById(R.id.pImage);
        mcheckout = findViewById(R.id.checkout);
        storeID = getIntent().getStringExtra("sID");
        spID = getIntent().getStringExtra("spID");
        mremoveToCart = findViewById(R.id.removeToCart);
        maddToCart = findViewById(R.id.addToCart);
        position = getIntent().getIntExtra("pos", 0);
        pName = getIntent().getStringExtra("name");
        pPrice = getIntent().getStringExtra("price");
        pImage = getIntent().getStringExtra("image");
        pID = getIntent().getStringExtra("pID");

        StID = getIntent().getStringExtra("StID");
        catName = getIntent().getStringExtra("catName");

        Glide.with(this).asBitmap().load(pImage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mpImage);
        GetCartData();
        GetCheckData();

        if (mycheckList.contains(spID)) {
            maddToCart.setVisibility(View.GONE);
            mremoveToCart.setVisibility(View.VISIBLE);
        } else {
            mremoveToCart.setVisibility(View.GONE);
            maddToCart.setVisibility(View.VISIBLE);
        }
        helpingMethods = new HelpingMethods(ProductDetailActivity.this);
        mcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mycheckList.size() > 0) {
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });


//
//        final SliderAdapter adapter = new SliderAdapter(this);
//        adapter.setCount(6);
//
//        sliderView.setSliderAdapter(adapter);
//
//        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
//            @Override
//            public void onIndicatorClicked(int position) {
//                sliderView.setCurrentPagePosition(position);
//            }
//        });

        mprice = findViewById(R.id.p_price);
        mName = findViewById(R.id.p_name);
        mName.setText(pName);
        mprice.setText("Rs." + pPrice + "/-");


        maddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mycheckList.contains(spID)) {
                    maddToCart.setVisibility(View.GONE);
                    mremoveToCart.setVisibility(View.VISIBLE);

                    int finalCount = helpingMethods.GetCartCount(storeID) + 1;
                    helpingMethods.SaveCartCount(finalCount, storeID);
                    setupBadge();

                    if (helpingMethods.GetStoreID() == null) {
                        helpingMethods.SaveStoreData(storeID, getIntent().getStringExtra("oName"), getIntent().getStringExtra("oImage"), getIntent().getStringExtra("oID"));
                    }

                    if (!helpingMethods.GetStoreID().equals(storeID)) {
                        mycheckList.clear();
                        SaveCheckData();
                        preferenceList.clear();
                        SaveCartData();
                    }


                    mycheckList.add(spID);
                    SaveCheckData();
                    //String p_name, String p_price, String p_quantity, String p_img, int pos, String productid, String storeId,String actual_price
                    preferenceList.add(new CatLvlItemList(pName, pPrice, "1", pImage, position, pID, storeID,pPrice,spID));
                    SaveCartData();
                }

            }
        });

        mremoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mycheckList.contains(spID)) {
                    mremoveToCart.setVisibility(View.GONE);
                    maddToCart.setVisibility(View.VISIBLE);
                    int finalCount = helpingMethods.GetCartCount(storeID) - 1;
                    helpingMethods.SaveCartCount(finalCount, storeID);
                    setupBadge();
                    int a = mycheckList.indexOf(spID);
                    mycheckList.remove(a);
                    SaveCheckData();
                    preferenceList.remove(a);
                    SaveCartData();
                }


            }
        });
    }

    private void SaveCheckData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Checkcart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("checklist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            mycheckList = gson.fromJson(json, type);

            if (mycheckList == null) {
                mycheckList = new ArrayList<>();
            }


        } catch (Exception e) {

            Toast.makeText(ProductDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GoBack();
    }

    private void SaveCartData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Mycart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
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

            Toast.makeText(ProductDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    private void GoBack() {
        Intent intent = new Intent(ProductDetailActivity.this, SubCatActivity.class);
        intent.putExtra("storeid", StID);
        intent.putExtra("catName", catName);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
