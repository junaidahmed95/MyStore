package com.example.mystore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.OrderSummary;
import com.example.mystore.Model.Product;
import com.example.mystore.R;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private List<CatLvlItemList> orderSummaryList;
    private Context context;

    public SummaryAdapter(List<CatLvlItemList> orderSummaryList, Context context) {
        this.orderSummaryList = orderSummaryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordersummary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mproName.setText(orderSummaryList.get(position).getP_name());
        holder.mproQty.setText(""+orderSummaryList.get(position).getP_quantity());
        holder.mproTag.setText(""+orderSummaryList.get(position).getActual_price());

    }

    @Override
    public int getItemCount() {
        return orderSummaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mproName,mproQty,mproTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mproName = itemView.findViewById(R.id.proName);
            mproQty = itemView.findViewById(R.id.proQty);
            mproTag = itemView.findViewById(R.id.proTag);

        }


    }
}
