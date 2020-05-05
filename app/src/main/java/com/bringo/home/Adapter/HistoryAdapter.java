package com.bringo.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.Model.OrderHistory;
import com.bringo.home.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    String chkstuts;


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
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new Hisholder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Hisholder holder, final int position) {

        //holder.mtxt_address.setText(historylist.get(position).getGetorderbykeylist().get(o).getUaddress());


        for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
            chkstuts = historylist.get(position).getGetorderbykeylist().get(a).getStatus();

            holder.mstorename.setText(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_totalproducts());
            holder.mcreated.setText(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_day());
            holder.mordid.setText(historylist.get(position).getOrderid());
            holder.mtotalprice.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
            Glide.with(mContext).load(historylist.get(position).getStrimg().replaceAll("^\"|\"$", "")).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mstrimg);

        }

        if(chkstuts.equals("null")){
            holder.mstatus.setText("Pending");
        }else if(chkstuts.equals("2")){
            holder.mstatus.setText("Assembled");
        }else if(chkstuts.equals("3")){
            holder.mstatus.setText("OnRoute");
        }else if(chkstuts.equals("4")){
            holder.mstatus.setText("Delivered");
        }else if(chkstuts.equals("1")){
            holder.mstatus.setText("Accepted");
        }








//        holder.mtxt_price.setText("Rs."+tprice);

//        tprice += Integer.parseInt(historylist.get(position).getGetorderbykeylist().get(pos).getPtotalprice());
//
//


        //holder.mtxt_address.setText(historylist.get(position).getMtxt_address());
        //holder.mtxt_qty.setText("Qty: "+historylist.get(position).getQtyplus());
        //holder.mtxt_totalproducts.setText("Total Products: "+historylist.get(position).getMtxt_totalproducts());
        //holder.mtxt_day.setText(historylist.get(position).getMtxt_day());
        // holder.mtxt_time.setText(historylist.get(position).getMtxt_time());



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
                TextView mstatus = mView.findViewById(R.id.status);


                for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
                    mtxt_addresss.setText(historylist.get(position).getGetorderbykeylist().get(a).getUaddress());
                    mtxt_prices.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
                    chkstuts = historylist.get(position).getGetorderbykeylist().get(a).getStatus();
                    list.add(new OrderHistory(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_price(), historylist.get(position).getGetorderbykeylist().get(a).getMtxt_qty(), historylist.get(position).getGetorderbykeylist().get(a).getAct_price(), historylist.get(position).getGetorderbykeylist().get(a).getImage(), historylist.get(position).getGetorderbykeylist().get(a).getTitle()));

                }
                mtxt_totalproductss.setText(""+ historylist.get(position).getGetorderbykeylist().size());
                mtxt_qtys.setText(""+historylist.get(position).getOrderid());
                if(chkstuts.equals("1")){
                    mstatus.setText("Accepted");
                }else if(chkstuts.equals("2")){
                    mstatus.setText("Assembled");
                }else if(chkstuts.equals("3")){
                    mstatus.setText("OnRoute");
                }else if(chkstuts.equals("4")){
                    mstatus.setText("Delivered");
                }


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
        TextView mstorename,mstatus,mcreated,mordid,mtotalprice;
        LinearLayout mbtn_detailhistory;
        ImageView mstrimg;


        public Hisholder(@NonNull View itemView) {
            super(itemView);

            mstrimg = itemView.findViewById(R.id.strimg);

            mtotalprice = itemView.findViewById(R.id.totalprice);
            mstorename = itemView.findViewById(R.id.storename);
            mstatus = itemView.findViewById(R.id.status);
            mcreated = itemView.findViewById(R.id.created);
            mordid = itemView.findViewById(R.id.ordid);
            mbtn_detailhistory = itemView.findViewById(R.id.btn_orderhistoy);


        }
    }
}
