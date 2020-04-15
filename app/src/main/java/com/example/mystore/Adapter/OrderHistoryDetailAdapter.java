package com.example.mystore.Adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Model.OrderHistory;
import com.example.mystore.ProductDetailActivity;
import com.example.mystore.R;

import java.util.List;

public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.holder> {

    List<OrderHistory> OrderList;

    boolean check;
    Context context;

    public OrderHistoryDetailAdapter(List<OrderHistory> orderList, Context context) {
        OrderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryDetailAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new holder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailAdapter.holder holder, final int position) {







            Glide.with(context).load(OrderList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mitem_image);
            holder.mtxt_item_name.setText(OrderList.get(position).getTitle());
           holder.mtxt_item_price.setText("Rs." + OrderList.get(position).getMtxt_price() + "/-");
            holder.mtxt_item_quantity.setText("Qty:" + OrderList.get(position).getMtxt_qty());





//        holder.mcdv_buttomsheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ProductDetailActivity.class);
//                intent.putExtra("image",OrderList.get(position).getImage());
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

    public class holder extends RecyclerView.ViewHolder {
        ImageView mitem_image;
        CardView mcdv_buttomsheet;
        TextView mtxt_item_name, mtxt_item_price, mtxt_item_quantity;


        public holder(@NonNull View itemView) {
            super(itemView);
            mcdv_buttomsheet =itemView.findViewById(R.id.cdv_buttomsheet);
            mitem_image = itemView.findViewById(R.id.item_image);
            mtxt_item_name = itemView.findViewById(R.id.txt_item_name);
            mtxt_item_price = itemView.findViewById(R.id.txt_item_price);
            mtxt_item_quantity = itemView.findViewById(R.id.txt_item_quantity);

        }
    }
}
