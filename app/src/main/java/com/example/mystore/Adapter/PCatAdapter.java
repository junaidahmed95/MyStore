package com.example.mystore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.HelpingMethods;
import com.example.mystore.ProductDetailActivity;
import com.example.mystore.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mystore.SubCatActivity.setupBadge;

public class PCatAdapter extends RecyclerView.Adapter<PCatAdapter.ViewHolder> {

    private List<CatLvlItemList> proLists;
    private Context mContext;
    private boolean flag = true;
    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private HelpingMethods helpingMethods;

    public PCatAdapter(List<CatLvlItemList> proLists, Context mContext) {
        this.proLists = proLists;
        this.mContext = mContext;
        GetCheckData();
        GetCartData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Activity activity = (Activity) mContext;
        helpingMethods = new HelpingMethods(activity);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext).asBitmap().load(proLists.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.pImage);
        holder.pName.setText(proLists.get(position).getP_name());
        holder.pPrice.setText("" + proLists.get(position).getP_price());
        try {
            if (mycheckList.size() > 0) {
                if (mycheckList.contains(proLists.get(position).getProductid())) {
                    holder.mbtn_add_cart.setVisibility(View.GONE);
                    holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
                } else {
                    holder.mbtn_remove_cart.setVisibility(View.GONE);
                    holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("image", proLists.get(position).getP_img());
                intent.putExtra("proLists", position);
                intent.putExtra("name", proLists.get(position).getP_name());
                intent.putExtra("price", proLists.get(position).getP_price());
                mContext.startActivity(intent);
            }
        });

        holder.mbtn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mycheckList.contains(proLists.get(position).getProductid())){
                    holder.mbtn_add_cart.setVisibility(View.GONE);
                    holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
                    int finalCount = helpingMethods.GetCartCount() + 1;
                    helpingMethods.SaveCartCount(finalCount);
                    setupBadge();
                    mycheckList.add(proLists.get(position).getProductid());
                    SaveCheckData();
                    preferenceList.add(new CatLvlItemList(proLists.get(position).getP_name(), proLists.get(position).getP_price(), proLists.get(position).getP_quantity(), proLists.get(position).getP_img(), position, proLists.get(position).getP_price(), proLists.get(position).getProductid()));
                    SaveCartData();
                    notifyDataSetChanged();
                }

            }
        });

        holder.mbtn_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mycheckList.contains(proLists.get(position).getProductid())){
                    holder.mbtn_remove_cart.setVisibility(View.GONE);
                    holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                    int a = mycheckList.indexOf(proLists.get(position).getProductid());
                    mycheckList.remove(a);
                    SaveCheckData();
                    preferenceList.remove(a);
                    SaveCartData();
                    notifyDataSetChanged();
                    int finalCount = helpingMethods.GetCartCount() - 1;
                    helpingMethods.SaveCartCount(finalCount);
                    setupBadge();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return proLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImage, pWish;
        private TextView pName, pPrice;
        private Button mbtn_add_cart, mbtn_remove_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.product_image);
            pName = itemView.findViewById(R.id.product_category);
            pPrice = itemView.findViewById(R.id.product_price);
            pWish = itemView.findViewById(R.id.wis);
            mbtn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            mbtn_remove_cart = itemView.findViewById(R.id.btn_remove_cart);


        }
    }
    private void SaveCheckData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Checkcart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("checklist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            mycheckList = gson.fromJson(json, type);

            if (mycheckList == null) {
                mycheckList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void SaveCartData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mycart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }


    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mycart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}


