package com.example.mystore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.AllStoresActivity;
import com.example.mystore.Model.AllStore;
import com.example.mystore.Model.ShowStores;
import com.example.mystore.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AllStoreAdapter extends BaseAdapter {

    List<ShowStores> storeList;
    private Sprite doubleBounce;
    Context mcontext;
    private Activity activity;

    public AllStoreAdapter(List<ShowStores> storeList, Context mcontext) {
        this.storeList = storeList;
        this.mcontext = mcontext;
        activity = (Activity)mcontext;
    }

    @Override
    public int getCount() {
        return storeList.size();
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
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allstoreitem, parent, false);
        CardView storecardId = convertView.findViewById(R.id.storecardId);
        ImageView mimg_store = convertView.findViewById(R.id.img_store);
        TextView txt_storedistance = convertView.findViewById(R.id.txt_storedistance);
        TextView txt_storename = convertView.findViewById(R.id.txt_storename);

        final ProgressBar progressBar = convertView.findViewById(R.id.spin_kit);
        txt_storename.setText(storeList.get(position).getStore_name());
        txt_storedistance.setText(storeList.get(position).getDistance()+"Km");
        doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        Glide.with(mcontext)
                .load(storeList.get(position).getStore_image())
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
                .into(mimg_store);

        storecardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, AllStoresActivity.class);
                intent.putExtra("storeid", storeList.get(position).getId());
                intent.putExtra("stname",storeList.get(position).getStore_name());
                intent.putExtra("ownerID",storeList.get(position).getUid());
                intent.putExtra("ownerImage",storeList.get(position).getStore_image());
                mcontext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




        return convertView;
    }
}
