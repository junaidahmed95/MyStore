package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Model.Category;
import com.example.mystore.R;
import com.example.mystore.SubCatActivity;




import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends BaseAdapter {
    private List<Category> productList;

    private String getcatprod;
    private Context mContext;
    private TextView categoryName;
    private ImageView categoryImage;
    private String std;


    public CategoryAdapter(List<Category> productList, Context mContext,String std) {
        this.productList = productList;
        this.std=std;
        this.mContext = mContext;
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
        categoryImage = convertView.findViewById(R.id.category_image);
        categoryName = convertView.findViewById(R.id.category_name);


        setData(productList.get(position).getCatImage(), productList.get(position).getCatName());

        mcv_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubCatActivity.class);
                intent.putExtra("storeid",std);
                intent.putExtra("catName",productList.get(position).getCatName());
                mContext.startActivity(intent);


            }
        });


        return convertView;

    }

    private void setData(String image, String name) {
        Glide.with(mContext).asBitmap().load(image).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(categoryImage);
        categoryName.setText(name);
    }



}
