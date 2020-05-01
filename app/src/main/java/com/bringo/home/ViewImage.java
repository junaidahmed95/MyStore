package com.bringo.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bringo.home.Adapter.multiviewpageradapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class ViewImage extends AppCompatActivity {

    private Intent intent;
    private String viewimage;
    private PhotoView image;
    private String forWhat;
    private LinearLayout mbackBtn;
    private TextView mtitle, mImageCount;
    private WebView wv;
    private LinearLayout mCountLayout;
    private ImageButton mLeftBtn, mRightBtn;
    DatabaseReference reference;
    private multiviewpageradapter viewpagerAdapter;
    private ViewPager myviewPager;
    private ProgressBar mimageLoader, mmapLoader;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        mbackBtn = findViewById(R.id.backBtn);
        image = findViewById(R.id.image);
//        mimageLoader = findViewById(R.id.imageLoader);
//        mmapLoader = findViewById(R.id.mapLoader);
//        mCountLayout = findViewById(R.id.countMain);
//        mImageCount = findViewById(R.id.textCount);
//
//        mLeftBtn = findViewById(R.id.leftBtn);
//        mRightBtn = findViewById(R.id.rightBtn);

        String mTitle = getIntent().getStringExtra("title");
        mtitle = findViewById(R.id.title);
        mtitle.setText(mTitle);
        mimageLoader.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);

        Glide.with(getApplicationContext()).load(viewimage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mimageLoader.setVisibility(View.GONE);
                return false;

            }
        }).into(image);


        mbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private int getNextItem(int i) {
        return myviewPager.getCurrentItem() + i;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mmapLoader.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getUid() != null ) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }


    }


}