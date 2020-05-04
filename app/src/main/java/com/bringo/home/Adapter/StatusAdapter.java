package com.bringo.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Model.OrderHistory;
import com.bringo.home.OrderTrackActivity;
import com.bringo.home.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.statusholder> {
    public static List<OrderHistory> historylist;
    public static List<OrderHistory> list;
    Context mContext;
    String name, created;
    String image;

    public StatusAdapter(List<OrderHistory> historylist, Context mContext) {
        this.historylist = historylist;
        list = new ArrayList<>();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public statusholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status, parent, false);
        list = new ArrayList<>();
        return new statusholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final statusholder holder, final int position) {

        for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
            holder.mstatus.setText(historylist.get(position).getGetorderbykeylist().get(a).getStatus());
            created = historylist.get(position).getGetorderbykeylist().get(a).getMtxt_day();

            holder.mordid.setText(historylist.get(position).getOrderid());
            holder.mtotalprice.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
            image = Glide.with(mContext).load(historylist.get(position).getStrimg().replaceAll("^\"|\"$", "")).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mstrimg).toString();
            name = historylist.get(position).getGetorderbykeylist().get(a).getMtxt_totalproducts();
        }
        holder.mcreated.setText(created);
        holder.mstorename.setText(name);
        holder.mclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, OrderTrackActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("orderid", historylist.get(position).getOrderid());
                intent.putExtra("storename", name);
                intent.putExtra("created", created);
                intent.putExtra("storeimage", image);
                mContext.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return historylist.size();
    }

    public class statusholder extends RecyclerView.ViewHolder {
        TextView mstorename, mstatus, mcreated, mordid, mtotalprice;
        LinearLayout mclick;
        ImageView mstrimg;

        public statusholder(@NonNull View itemView) {
            super(itemView);
            mstrimg = itemView.findViewById(R.id.strimg);

            mtotalprice = itemView.findViewById(R.id.totalprice);
            mstorename = itemView.findViewById(R.id.storename);
            mstatus = itemView.findViewById(R.id.status);
            mcreated = itemView.findViewById(R.id.created);
            mordid = itemView.findViewById(R.id.ordid);
            mclick = itemView.findViewById(R.id.click);


        }
    }
}
