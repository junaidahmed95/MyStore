package com.bringo.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.Model.FeaturedStoreList;
import com.bringo.home.R;
import com.bringo.home.StoreInfoActivity;
import com.bringo.home.SubCatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class FeaturedStoreAdapter extends RecyclerView.Adapter<FeaturedStoreAdapter.ViewHolder> {

    private Context mContext;
    private List<FeaturedStoreList> featuredStoreLists;

    public FeaturedStoreAdapter(Context mContext, List<FeaturedStoreList> featuredStoreLists) {
        this.mContext = mContext;
        this.featuredStoreLists = featuredStoreLists;
    }

    @NonNull
    @Override
    public FeaturedStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedStoreAdapter.ViewHolder holder, final int position) {

        Glide.with(mContext).asBitmap().load(featuredStoreLists.get(position).getThumbnail()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mimg_newway);

        holder.mstore_name.setText(featuredStoreLists.get(position).getStr_name());
        holder.mcard_newway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreInfoActivity.class);
                intent.putExtra("storeid", featuredStoreLists.get(position).getId());
                intent.putExtra("stname", featuredStoreLists.get(position).getStr_name());
                intent.putExtra("ownerID", featuredStoreLists.get(position).getU_id());
                intent.putExtra("address", featuredStoreLists.get(position).getAddress());
                intent.putExtra("ownerImage", featuredStoreLists.get(position).getThumbnail());


                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return featuredStoreLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mstore_name;
        ImageView mimg_newway;
        CardView mcard_newway;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            mimg_newway = itemView.findViewById(R.id.img_newway);
            mstore_name = itemView.findViewById(R.id.store_name);
            mcard_newway = itemView.findViewById(R.id.card_newway);
        }
    }
}
