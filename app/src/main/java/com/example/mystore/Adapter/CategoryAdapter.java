package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.AllProductsActivity;
import com.example.mystore.Model.Category;
import com.example.mystore.Model.Product;
import com.example.mystore.R;
import com.example.mystore.SubCatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> productList;
    private Context mContext;

    public CategoryAdapter(List<Category> productList, Context mContext) {
        this.productList = productList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.setData(productList.get(i).getCatImage(), productList.get(i).getCatName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubCatActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryImage;
        private TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }

        private void setData(String image, String name) {
            Glide.with(mContext).asBitmap().load(image).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(categoryImage);

            categoryName.setText(name);
        }


    }

}

