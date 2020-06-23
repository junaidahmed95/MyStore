package com.bringo.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.MainCatActivity;
import com.bringo.home.Model.Category;
import com.bringo.home.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;


import java.util.List;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    @NonNull

    private List<Category> list;

    public MainCategoryAdapter(@NonNull List<Category> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    Context mcontext;

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maincatitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mtxt.setText(list.get(position).getCatName());
        //Picasso.get().load("http://bringo.biz/public/img/cat_a/electronic3.jpg").into(holder.mimg);
        Glide.with(mcontext).asBitmap().load(list.get(position).getCatImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).dontAnimate().into(holder.mimg);
        holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MainCatActivity.class);
                intent.putExtra("cat_id",list.get(position).getCat_id());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtxt;
        ImageView mimg;
        LinearLayout mcat_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mimg = itemView.findViewById(R.id.img);
            mtxt = itemView.findViewById(R.id.txt);
            mcat_layout = itemView.findViewById(R.id.cat_layout);
        }
    }
}
