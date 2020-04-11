package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.ProductDetailActivity;
import com.example.mystore.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    List<CatLvlItemList> OrderList;
    boolean check,wcheck;
    Context context;

    public OrderAdapter(List<CatLvlItemList> item, Context context,boolean check, boolean wcheck) {
        this.OrderList= item;
        this.check = check;
        this.context = context;
        this.wcheck = wcheck;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, final int position) {
        Glide.with(context).load(OrderList.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mitem_image);
        holder.mtxt_item_name.setText(OrderList.get(position).getP_name());
        holder.mtxt_item_price.setText("Rs."+OrderList.get(position).getP_price()+"/-");

        if (wcheck){
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



        holder.mcdv_buttomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("image",OrderList.get(position).getP_img());
                intent.putExtra("name",OrderList.get(position).getP_name());
                intent.putExtra("price",OrderList.get(position).getP_price());
                context.startActivity(intent);
            }
        });

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
}
