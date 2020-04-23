package com.example.mystore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.Model.Category;
import com.example.mystore.R;
import com.example.mystore.SubCatActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;


import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class CategoryAdapter extends BaseAdapter {
    private List<Category> productList;
    private Sprite doubleBounce;
    private String getcatprod;
    private Context mContext;
    private Activity activity;
    private String std,ownerID, ownerImage,ownerName;


    public CategoryAdapter(List<Category> productList, Context mContext, String std,String ownerID,String ownerImage,String ownerName) {
        this.productList = productList;
        this.std = std;
        this.ownerID = ownerID;
        this.ownerImage=ownerImage;
        this.ownerName = ownerName;
        this.mContext = mContext;
        activity = (Activity)mContext;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        CardView mcv_cat = convertView.findViewById(R.id.card_cardview);

        ImageView categoryImage = convertView.findViewById(R.id.category_image);
        TextView categoryName = convertView.findViewById(R.id.category_name);
        final ProgressBar progressBar = convertView.findViewById(R.id.spin_kit);

        doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        Glide.with(mContext)
                .load(productList.get(position).getCatImage())
                .apply(
                        new RequestOptions()
                                .error(R.drawable.placeholder)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(withCrossFade())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(categoryImage);
        categoryName.setText(productList.get(position).getCatName());


        mcv_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubCatActivity.class);
                intent.putExtra("storeid", std);
                intent.putExtra("catName", productList.get(position).getCatName());
                intent.putExtra("stname",ownerName);
                intent.putExtra("ownerID",ownerID);
                intent.putExtra("ownerImage",ownerImage);
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        return convertView;

    }


}
