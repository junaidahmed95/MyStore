package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bringo.home.Adapter.PCatAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.HelpingMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.bringo.home.Adapter.PCatAdapter.subcatproLists;
import static com.bringo.home.MainActivity.MainsetupBadge;


public class ProductDetailActivity extends AppCompatActivity {

    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private String storeID;
    private String spID;
    private RecyclerView mpRecyclerView;
    private TextView mName, mprice, mp_desc;
    private ImageView mpImage;
    private Button mremoveToCart, maddToCart, mcheckout;
    private HelpingMethods helpingMethods;
    private int position;
    private String pID, pImage, pPrice, pName, StID, catName, ownerName, ownerID, ownerImage, mdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.viewBar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mpImage = findViewById(R.id.pImage);
        mcheckout = findViewById(R.id.checkout);
        mpRecyclerView = findViewById(R.id.relatePList);
        mpRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        storeID = getIntent().getStringExtra("sID");
        spID = getIntent().getStringExtra("spID");
        mdesc = getIntent().getStringExtra("desc");
        mremoveToCart = findViewById(R.id.removeToCart);
        mp_desc = findViewById(R.id.p_desc);
        mp_desc.setText(mdesc);
        maddToCart = findViewById(R.id.addToCart);
        position = getIntent().getIntExtra("pos", 0);
        pName = getIntent().getStringExtra("name");
        pPrice = getIntent().getStringExtra("price");
        pImage = getIntent().getStringExtra("image");
        pID = getIntent().getStringExtra("pID");
        ownerName = getIntent().getStringExtra("oName");
        ownerID = getIntent().getStringExtra("oID");
        ownerImage = getIntent().getStringExtra("oImage");
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
                GetCheckData();
                if (mycheckList.size() > 0) {
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    intent.putExtra("for", "detail");
                    intent.putExtra("image", pImage);
                    intent.putExtra("proLists", position);
                    intent.putExtra("from", "subcart");
                    intent.putExtra("StID", storeID);
                    intent.putExtra("pID", pID);
                    intent.putExtra("spID", spID);
                    intent.putExtra("desc", mdesc);
                    intent.putExtra("oName", ownerName);
                    intent.putExtra("catName", catName);
                    intent.putExtra("oImage", ownerImage);
                    intent.putExtra("oID", ownerID);
                    intent.putExtra("name", pName);
                    intent.putExtra("price", pPrice);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });


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

                    MainsetupBadge();
                    int total = helpingMethods.GetCartTotal(storeID) + Integer.parseInt(pPrice);
                    helpingMethods.SaveCartTotal(total, storeID);
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
                    preferenceList.add(new CatLvlItemList(pName, pPrice, "1", pImage, position, pID, storeID, pPrice, spID));
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
                    MainsetupBadge();
                    int total = helpingMethods.GetCartTotal(storeID) - Integer.parseInt(pPrice);
                    helpingMethods.SaveCartTotal(total, storeID);
                    int a = mycheckList.indexOf(spID);
                    mycheckList.remove(a);
                    SaveCheckData();
                    preferenceList.remove(a);
                    SaveCartData();
                }


            }
        });

        if (getIntent().getStringExtra("from").equals("subcart")) {

            PCatAdapter proAdapter;
            if (getIntent().getBooleanExtra("isSearch", false) == true) {
                proAdapter = new PCatAdapter(subcatproLists, this, storeID, ownerID, ownerImage, ownerName, catName, true);
            } else {
                proAdapter = new PCatAdapter(subcatproLists, this, storeID, ownerID, ownerImage, ownerName, catName, false);
            }
            mpRecyclerView.setAdapter(proAdapter);
            proAdapter.notifyDataSetChanged();
        }

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
        if (catName.equals("")) {
            Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
            intent.putExtra("stID", storeID);
            intent.putExtra("catName", catName);
            intent.putExtra("stname", ownerName);
            intent.putExtra("ownerID", ownerID);
            intent.putExtra("ownerImage", ownerImage);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ProductDetailActivity.this, SubCatActivity.class);
            intent.putExtra("storeid", storeID);
            intent.putExtra("catName", catName);
            intent.putExtra("stname", ownerName);
            intent.putExtra("ownerID", ownerID);
            intent.putExtra("ownerImage", ownerImage);
            startActivity(intent);
        }


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

}
