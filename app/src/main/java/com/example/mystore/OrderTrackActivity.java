package com.example.mystore;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mystore.Adapter.OrderAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.OrderHistory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class OrderTrackActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button mbtnhis_detail;
    BottomSheetDialog orderdetailSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);

        mbtnhis_detail = findViewById(R.id.btnhis_detail);

        toolbar = findViewById(R.id.bar);
        toolbar.setTitle("Order Track");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mbtnhis_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mView = LayoutInflater.from(OrderTrackActivity.this).inflate(R.layout.orderdetail_sheet, null);
                Toolbar mappBar = mView.findViewById(R.id.appBar);

                mappBar.setTitle("Order Detail");
                final RecyclerView recyclerView = mView.findViewById(R.id.oorderdetail_gd);
                TextView textView = mView.findViewById(R.id.totalitemprice);






                FloatingActionButton mfabClose = mView.findViewById(R.id.fabClose);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderTrackActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);


                final List<CatLvlItemList> mCatLvlItemList = new ArrayList<>();
                mCatLvlItemList.add(new CatLvlItemList("BAKERS LAND CHOCOLAY BISCUIT H/R", "13", "1", "https://chhatt.com/Cornstr/grocery/public/prod/BakersLandChocolayBiscuitHR.jpg",""));
                mCatLvlItemList.add(new CatLvlItemList("BAKERS LAND ROYAL TREAT BISCUIT T/P BOX 24PCS", "105", "1", "https://chhatt.com/Cornstr/grocery/public/prod/BakersLandRoyalTreatBiscuitTPBox24pcs.jpg",""));
                mCatLvlItemList.add(new CatLvlItemList("BAKERS LAND CHOCOLAY BISCUIT H/R", "13", "1", "https://chhatt.com/Cornstr/grocery/public/prod/BakersLandChocolayBiscuitHR.jpg",""));
                mCatLvlItemList.add(new CatLvlItemList("BAKERS LAND ROYAL TREAT BISCUIT T/P BOX 24PCS", "105", "1", "https://chhatt.com/Cornstr/grocery/public/prod/BakersLandRoyalTreatBiscuitTPBox24pcs.jpg",""));
                OrderAdapter orderAdapter = new OrderAdapter(mCatLvlItemList, OrderTrackActivity.this,true,false);
                recyclerView.setAdapter(orderAdapter);
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


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
