package com.bringo.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.ProductDetailActivity;
import com.bringo.home.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<CatLvlItemList> OrderList;
    private boolean check,wcheck;
    private Context context;
    private List<String> myWishList;
    private List<CatLvlItemList> myFavList;

    public OrderAdapter(List<CatLvlItemList> item, Context context,boolean check, boolean wcheck) {
        this.OrderList= item;
        this.check = check;
        this.context = context;
        this.wcheck = wcheck;
        GetFavData();
        GetWishData();
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, final int position) {
        Glide.with(context).asBitmap().load(OrderList.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mitem_image);
        holder.mtxt_item_name.setText(OrderList.get(position).getP_name());
        holder.mtxt_item_price.setText("Rs."+OrderList.get(position).getP_price()+"/-");

        if (wcheck){
            ImageViewCompat.setImageTintList(holder.mwish,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)));
            holder.mwish.setVisibility(View.VISIBLE);
        }
        else {
            holder.mwish.setVisibility(View.GONE);
        }

        if(check){
            holder.mtxt_item_quantity.setText("Qty:"+OrderList.get(position).getP_quantity());
            holder.mtxt_item_quantity.setVisibility(View.VISIBLE);
        }else {
            holder.mtxt_item_quantity.setVisibility(View.GONE);
        }

        holder.mwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myWishList.contains(OrderList.get(position).getSimplePID())) {
                    int a = myWishList.indexOf(OrderList.get(position).getSimplePID());
                    myWishList.remove(a);
                    SaveWishData();
                    myFavList.remove(a);
                    SaveFavData();
                    OrderList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, OrderList.size());
                }


            }
        });

//        holder.mcdv_buttomsheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ProductDetailActivity.class);
//                intent.putExtra("image",OrderList.get(position).getP_img());
//                intent.putExtra("name",OrderList.get(position).getP_name());
//                intent.putExtra("price",OrderList.get(position).getP_price());
//                context.startActivity(intent);
//            }
//        });

    }



    @Override
    public int getItemCount() {
        return OrderList.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder {

        ImageView mitem_image;
        CardView mcdv_buttomsheet;
        ImageView mwish;
        TextView mtxt_item_name, mtxt_item_price, mtxt_item_quantity;



        public OrderHolder(@NonNull View itemView) {
            super(itemView);
         mcdv_buttomsheet =itemView.findViewById(R.id.cdv_buttomsheet);
            mitem_image = itemView.findViewById(R.id.item_image);
            mtxt_item_name = itemView.findViewById(R.id.txt_item_name);
            mtxt_item_price = itemView.findViewById(R.id.txt_item_price);
            mtxt_item_quantity = itemView.findViewById(R.id.txt_item_quantity);
          mwish = itemView.findViewById(R.id.wis);
        }
    }

    private void SaveWishData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Mywish", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myWishList);
        editor.putString("wishlist", json);
        editor.apply();
    }


    private void GetWishData() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Mywish", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("wishlist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            myWishList = gson.fromJson(json, type);

            if (myWishList == null) {
                myWishList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (context != null) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void SaveFavData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Myfav", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myFavList);
        editor.putString("favlist", json);
        editor.apply();
    }


    private void GetFavData() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Myfav", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("favlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            myFavList = gson.fromJson(json, type);

            if (myFavList == null) {
                myFavList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (context != null) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
