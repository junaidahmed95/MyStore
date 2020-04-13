package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.AllStoresActivity;
import com.example.mystore.Model.AllStore;
import com.example.mystore.R;

import java.util.List;

public class AllStoreAdapter extends BaseAdapter {

    List<AllStore> storeList;
    Context mcontext;

    public AllStoreAdapter(List<AllStore> storeList, Context mcontext) {
        this.storeList = storeList;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allstoreitem, parent, false);
        LinearLayout mmainLayout = convertView.findViewById(R.id.mainLayout);
        CardView storecardId = convertView.findViewById(R.id.storecardId);
        ImageView mimg_store = convertView.findViewById(R.id.img_store);
        TextView txt_storedistance = convertView.findViewById(R.id.txt_storedistance);
        TextView txt_storename = convertView.findViewById(R.id.txt_storename);

        storecardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, AllStoresActivity.class);
                intent.putExtra("storeid", storeList.get(position).getStr_id());
                mcontext.startActivity(intent);
            }
        });

        Glide.with(mcontext).load(storeList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mimg_store);
        txt_storename.setText(storeList.get(position).getName());
        txt_storedistance.setText(storeList.get(position).getDistance()+"Km");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "" + storeList.get(position).getStr_id(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
