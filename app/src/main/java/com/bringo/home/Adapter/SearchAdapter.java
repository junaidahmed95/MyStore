package com.bringo.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.holder> {
    @NonNull
    ArrayList<CatLvlItemList> list;
    Context mcontext;

    public SearchAdapter(@NonNull ArrayList<CatLvlItemList> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @Override
    public SearchAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitem, parent, false);
        holder evh = new holder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.holder holder, int position) {
        CatLvlItemList currentItem = list.get(position);


        Glide.with(mcontext).asBitmap().load(currentItem.getP_img()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(holder.mImageView);
        //holder.mImageView.setImageResource(Integer.parseInt(currentItem.getP_img()));
        holder.mTextView1.setText(currentItem.getP_price());
        holder.mTextView2.setText(currentItem.getP_name());
    }

    public void filterList(ArrayList<CatLvlItemList> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class holder extends RecyclerView.ViewHolder {
        public CircleImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public holder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }
}
