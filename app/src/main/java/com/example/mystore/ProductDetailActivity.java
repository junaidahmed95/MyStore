package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import static com.example.mystore.ui.home.HomeFragment.forWhat;

public class ProductDetailActivity extends AppCompatActivity {

    private SliderView sliderView;
    private TextView mName, mprice;
    private ImageView mpImage;

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

        Glide.with(this).asBitmap().load(getIntent().getStringExtra("image")).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mpImage);
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
