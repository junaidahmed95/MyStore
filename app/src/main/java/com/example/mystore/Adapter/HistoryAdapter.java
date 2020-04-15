package com.example.mystore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.OrderHistory;
import com.example.mystore.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Hisholder> {

    List<OrderHistory> historylist;

    List<OrderHistory> list;
    Context mContext;
    BottomSheetDialog orderdetailSheetDialog;
    private int qtycount = 0;
    private int tprice = 0;


    public HistoryAdapter(List<OrderHistory> historylist, Context mContext) {
        this.historylist = historylist;
        list = new ArrayList<>();
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public Hisholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history, parent, false);
        list = new ArrayList<>();

        return new Hisholder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Hisholder holder, final int position) {

        //holder.mtxt_address.setText(historylist.get(position).getGetorderbykeylist().get(o).getUaddress());

        for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
            holder.mtxt_address.setText(historylist.get(position).getGetorderbykeylist().get(a).getUaddress());
            holder.mtxt_totalproducts.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
            qtycount += Integer.parseInt(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_qty());
            holder.mtxt_day.setText(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_day());
            holder.mtxt_price.setText(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_totalproducts());


        }


        holder.mtxt_qty.setText("Total Qty: " + qtycount);
        qtycount = 0;


//        holder.mtxt_price.setText("Rs."+tprice);

//        tprice += Integer.parseInt(historylist.get(position).getGetorderbykeylist().get(pos).getPtotalprice());
//
//


        //holder.mtxt_address.setText(historylist.get(position).getMtxt_address());
        //holder.mtxt_qty.setText("Qty: "+historylist.get(position).getQtyplus());
        //holder.mtxt_totalproducts.setText("Total Products: "+historylist.get(position).getMtxt_totalproducts());
        //holder.mtxt_day.setText(historylist.get(position).getMtxt_day());
        // holder.mtxt_time.setText(historylist.get(position).getMtxt_time());
        holder.marrow.setImageResource(R.drawable.ic_navigate_next_black_24dp);


        holder.mbtn_detailhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mView = LayoutInflater.from(mContext).inflate(R.layout.orderdetail_sheet, null);
                Toolbar mappBar = mView.findViewById(R.id.appBar);

                mappBar.setTitle("Order Detail");
                final RecyclerView recyclerView = mView.findViewById(R.id.oorderdetail_gd);

                TextView mtxt_qtys = mView.findViewById(R.id.txt_total_qtys);
                TextView mtxt_totalproductss = mView.findViewById(R.id.txt_totalproductss);
                TextView mtxt_addresss = mView.findViewById(R.id.txt_addresss);
                TextView mtxt_prices = mView.findViewById(R.id.totalitemprice);


                for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
                    mtxt_addresss.setText(historylist.get(position).getGetorderbykeylist().get(a).getUaddress());
                    mtxt_prices.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
                    qtycount += Integer.parseInt(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_qty());
                    list.add(new OrderHistory(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_price(), historylist.get(position).getGetorderbykeylist().get(a).getMtxt_qty(), historylist.get(position).getGetorderbykeylist().get(a).getAct_price(), historylist.get(position).getGetorderbykeylist().get(a).getImage(), historylist.get(position).getGetorderbykeylist().get(a).getTitle()));

                }
                mtxt_totalproductss.setText(""+ historylist.get(position).getGetorderbykeylist().size());


                mtxt_qtys.setText("Qty: "+qtycount);
                qtycount = 0;


                FloatingActionButton mfabClose = mView.findViewById(R.id.fabClose);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);





                OrderHistoryDetailAdapter orderAdapter = new OrderHistoryDetailAdapter(new ArrayList<OrderHistory>(list), mContext);
                recyclerView.setAdapter(orderAdapter);
                notifyDataSetChanged();
                list.clear();



                orderdetailSheetDialog = new BottomSheetDialog(mView.getContext());
                orderdetailSheetDialog.setContentView(mView);
                orderdetailSheetDialog.show();

                mfabClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderdetailSheetDialog.cancel();
                    }
                });


            }

        });
    }

    @Override
    public int getItemCount() {
        return historylist.size();
    }


    public class Hisholder extends RecyclerView.ViewHolder {
        TextView mtxt_price, mtxt_qty, mtxt_totalproducts, mtxt_address, mtxt_day, mtxt_time;
        Button mbtn_detailhistory;
        ImageView marrow;


        public Hisholder(@NonNull View itemView) {
            super(itemView);
            marrow = itemView.findViewById(R.id.arrow);
            mtxt_price = itemView.findViewById(R.id.txt_price);
            mtxt_qty = itemView.findViewById(R.id.txt_qty);
            mtxt_totalproducts = itemView.findViewById(R.id.txt_totalproducts);
            mtxt_address = itemView.findViewById(R.id.txt_address);
            mtxt_day = itemView.findViewById(R.id.txt_day);
            mtxt_time = itemView.findViewById(R.id.txt_time);
            mbtn_detailhistory = itemView.findViewById(R.id.btn_orderhistoy);


        }
    }
}