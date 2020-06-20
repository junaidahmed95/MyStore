package com.bringo.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.Model.Category;
import com.bringo.home.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mtxt.setText(list.get(position).getCatName());
        Glide.with(mcontext).asBitmap().load(list.get(position).getCatImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mimg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtxt;
        ImageView mimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mimg = itemView.findViewById(R.id.img);
            mtxt = itemView.findViewById(R.id.txt);
        }
    }
}
