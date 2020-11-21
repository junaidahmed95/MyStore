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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.statusholder> {
    private List<OrderHistory> historylist;
    Context mContext;
    private boolean isHistory;

    public StatusAdapter(List<OrderHistory> historylist, Context mContext,boolean isHistory) {
        this.historylist = historylist;
        this.isHistory=isHistory;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public statusholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status, parent, false);
        return new statusholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final statusholder holder, final int position) {

        holder.mordid.setText(historylist.get(position).getOrderID());
        holder.mtotalprice.setText("Rs." + historylist.get(position).getOrderPrice() + "/-");
        Glide.with(mContext).load(historylist.get(position).getStoreImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.mstrimg);
        holder.mcreated.setText(historylist.get(position).getOrderDate());
        holder.mstorename.setText(historylist.get(position).getStoreName());


        if(isHistory){
            FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getUid()).child(historylist.get(position).getOrderID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {


                        if(dataSnapshot.hasChild("status5")){

                            holder.mstatus.setText(dataSnapshot.child("status5").getValue().toString());


                        }else {
                            try {
                                historylist.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, historylist.size());
                            }catch (Exception e){
                                Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }else {
                        try {
                            historylist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, historylist.size());
                        }catch (Exception e){
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {

            FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getUid()).child(historylist.get(position).getOrderID().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       if(dataSnapshot.hasChild("status5")){
                           try {
                               historylist.remove(position);
                               notifyItemRemoved(position);
                               notifyItemRangeChanged(position, historylist.size());
                           }catch (Exception e){
                               Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                           }
                       }else if(dataSnapshot.hasChild("status4")){
                           holder.mstatus.setText(dataSnapshot.child("status4").getValue().toString());
                       }else if(dataSnapshot.hasChild("status3")){
                           holder.mstatus.setText(dataSnapshot.child("status3").getValue().toString());
                       }else if(dataSnapshot.hasChild("status2")){
                           holder.mstatus.setText(dataSnapshot.child("status2").getValue().toString());
                       }
                       else if(dataSnapshot.hasChild("status1")){
                           holder.mstatus.setText(dataSnapshot.child("status1").getValue().toString());
                       }
                    }else {
                       try {
                           historylist.remove(position);
                           notifyItemRemoved(position);
                           notifyItemRangeChanged(position, historylist.size());
                       }catch (Exception e){
                           Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                       }
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        holder.mclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.mstatus.getText().toString().equals("Pending")) {
                    Intent intent = new Intent(mContext, OrderTrackActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("price", historylist.get(position).getOrderPrice());
                    intent.putExtra("add", historylist.get(position).getAddress());
                    intent.putExtra("orderid", historylist.get(position).getOrderID());
                    intent.putExtra("storeid", historylist.get(position).getStoreID());
                    intent.putExtra("storename", historylist.get(position).getStoreName());
                    intent.putExtra("created", historylist.get(position).getOrderDate());
                    intent.putExtra("storeimage", historylist.get(position).getStoreImage());
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "This order is not accepted yet!!", Toast.LENGTH_SHORT).show();
                }


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
