package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Adapter.SliderAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.HelpingMethods;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;
import static com.example.mystore.MainActivity.checklist;
import static com.example.mystore.SubCatActivity.setupBadge;
import static com.example.mystore.ui.home.HomeFragment.forWhat;

public class ProductDetailActivity extends AppCompatActivity {

    private SliderView sliderView;
    private TextView mName, mprice;
    private ImageView mpImage;
    private Button mremoveToCart, maddToCart, mcheckout;
    private HelpingMethods helpingMethods;

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
        Glide.with(this).asBitmap().load(getIntent().getStringExtra("image")).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mpImage);
        mremoveToCart = findViewById(R.id.removeToCart);
        maddToCart = findViewById(R.id.addToCart);
        final int position = getIntent().getIntExtra("pos", 0);
        if (CatLvlAdapter.list.get(position).isClicked()) {
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
                if (selectedProducts.size() > 0) {
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Please add atleast one product.", Toast.LENGTH_LONG).show();
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
        mName.setText(getIntent().getStringExtra("name"));
        mprice.setText("Rs." + getIntent().getStringExtra("price") + "/-");


        maddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maddToCart.setVisibility(View.GONE);
                mremoveToCart.setVisibility(View.VISIBLE);
                CatLvlAdapter.list.get(position).setClicked(true);
                checklist.add(CatLvlAdapter.list.get(position).getProductid());
                selectedProducts.add(new CatLvlItemList(CatLvlAdapter.list.get(position).getP_name(), CatLvlAdapter.list.get(position).getP_price(), CatLvlAdapter.list.get(position).getP_quantity(), CatLvlAdapter.list.get(position).getP_img(), position, CatLvlAdapter.list.get(position).getP_price(), CatLvlAdapter.list.get(position).getProductid()));
                int finalCount = helpingMethods.GetCartCount() + 1;
                helpingMethods.SaveCartCount(finalCount);
                setupBadge();


            }
        });

        mremoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mremoveToCart.setVisibility(View.GONE);
                maddToCart.setVisibility(View.VISIBLE);
                CatLvlAdapter.list.get(position).setClicked(false);
                int finalCount = helpingMethods.GetCartCount() + 1;
                helpingMethods.SaveCartCount(finalCount);
                setupBadge();
                if (checklist.contains(CatLvlAdapter.list.get(position).getProductid())) {
                    int a = checklist.indexOf(CatLvlAdapter.list.get(position).getProductid());
                    checklist.remove(a);
                    selectedProducts.remove(a);

                }

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

}
