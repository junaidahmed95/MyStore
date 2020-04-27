package com.bringo.home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bringo.home.Model.Category;
import com.bringo.home.R;
import com.bringo.home.SubCatActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;


import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        doubleBounce = new Circle();
        holder.progressBar.setIndeterminateDrawable(doubleBounce);
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
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(withCrossFade())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.categoryImage);
        holder.categoryName.setText(productList.get(position).getCatName());


        holder.mcv_cat.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mcv_cat;
        private ImageView categoryImage;
        private TextView categoryName;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcv_cat = itemView.findViewById(R.id.card_cardview);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
            progressBar = itemView.findViewById(R.id.spin_kit);
        }
    }
}
