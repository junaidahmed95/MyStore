package com.example.mystore.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.R;
import com.example.mystore.videoView;

import java.util.List;

public class multiviewpageradapter extends PagerAdapter {
    private List<Uri> imageList;

    public multiviewpageradapter(List<Uri> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView prodImageView = new ImageView(container.getContext());
        if (imageList.size() > 0) {
            Glide.with(container.getContext()).asBitmap().load(imageList.get(position)).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(prodImageView);
        }
        container.addView(prodImageView, 0);
        prodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(prodImageView.getContext(), videoView.class);
                intent.putExtra("imageview", imageList.get(position));
                prodImageView.getContext().startActivity(intent);
            }
        });
        return prodImageView;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }


}
