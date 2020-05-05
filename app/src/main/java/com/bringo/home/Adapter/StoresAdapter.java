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
import com.bringo.home.Model.ShowStores;
import com.bringo.home.R;
import com.bringo.home.StoreInfoActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {


    private List<ShowStores> storeList;
    private Context mcontext;
    private boolean displayAll;
    private Activity activity;

    public StoresAdapter(List<ShowStores> storeList, Context mcontext,boolean displayAll) {
        this.storeList = storeList;
        this.displayAll=displayAll;
        this.mcontext = mcontext;
        activity = (Activity)mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allstoreitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txt_storename.setText(storeList.get(position).getStore_name());
        holder.txt_storedistance.setText(storeList.get(position).getDistance()+"Km");
        Glide.with(mcontext).asBitmap().load(storeList.get(position).getStore_image()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mimg_store);


        holder.storecardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, StoreInfoActivity.class);
                intent.putExtra("storeid", storeList.get(position).getId());
                intent.putExtra("stname",storeList.get(position).getStore_name());
                intent.putExtra("ownerID",storeList.get(position).getUid());
                intent.putExtra("address",storeList.get(position).getAddress());
                intent.putExtra("ownerImage",storeList.get(position).getStore_image());
                mcontext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(displayAll){
            return storeList.size();
        }else {
            if(storeList.size()>6){
                return 6;
            }else {
                return storeList.size();
            }
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView storecardId;
        private ImageView mimg_store;
        private TextView txt_storedistance,txt_storename;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storecardId = itemView.findViewById(R.id.storecardId);
            mimg_store = itemView.findViewById(R.id.img_store);
            txt_storedistance = itemView.findViewById(R.id.txt_storedistance);
            txt_storename = itemView.findViewById(R.id.txt_storename);
        }
    }
}
