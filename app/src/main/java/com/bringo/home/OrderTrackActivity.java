package com.bringo.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.OrderAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.OrderHistory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrderTrackActivity extends AppCompatActivity {

    String get_status;
    TextView mtxt_ordertime, mtxt_storename, mstepOneTextTime, mstepTwoTextTime, mstepThreeTextTime, mstepFourTextTime;
    CircleImageView mstoreimg;
    Button btnhis_detail;
    int position = 0;
    private String mStatus = "";
    private int qtycount = 0;
    BottomSheetDialog orderdetailSheetDialog;
    List<CatLvlItemList> list;
    String chkstuts;
    private ProgressDialog progressDialog;
    FloatingActionButton mstepOneFab, mstepTwoFab, mstepThreeFab, mstepFourFab;
    ProgressBar mstepTwoBar, mstepThreeBar, mstepFourBar;
    private Toolbar mordertrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        progressDialog = new ProgressDialog(OrderTrackActivity.this);

        mordertrack = findViewById(R.id.trackorder);

        mordertrack.setTitle("Your Order (" + getIntent().getStringExtra("orderid") + ")");
        setSupportActionBar(mordertrack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        mstepOneTextTime = findViewById(R.id.stepOneTextTime);
        mstepTwoTextTime = findViewById(R.id.stepTwoTextTime);
        mstepThreeTextTime = findViewById(R.id.stepThreeTextTime);
        mstepFourTextTime = findViewById(R.id.stepFourTextTime);

        FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getUid()).child(getIntent().getStringExtra("orderid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("status2")) {
                        Date date = new Date(Long.parseLong(dataSnapshot.child("time2").getValue().toString()));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                        String tim = dateFormat.format(date).toUpperCase();
                        mStatus = dataSnapshot.child("status2").getValue().toString();
                        mstepOneTextTime.setText(tim);
                        GetStatus(mStatus);
                    }
                    if (dataSnapshot.hasChild("status3")) {
                        Date date = new Date(Long.parseLong(dataSnapshot.child("time3").getValue().toString()));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                        String tim = dateFormat.format(date).toUpperCase();
                        mStatus = dataSnapshot.child("status3").getValue().toString();
                        mstepTwoTextTime.setText(tim);
                        GetStatus(mStatus);
                    }
                    if (dataSnapshot.hasChild("status4")) {
                        Date date = new Date(Long.parseLong(dataSnapshot.child("time4").getValue().toString()));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                        String tim = dateFormat.format(date).toUpperCase();
                        mStatus = dataSnapshot.child("status4").getValue().toString();
                        mstepThreeTextTime.setText(tim);
                        GetStatus(mStatus);
                    }
                    if (dataSnapshot.hasChild("status5")) {
                        Date date = new Date(Long.parseLong(dataSnapshot.child("time5").getValue().toString()));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                        String tim = dateFormat.format(date).toUpperCase();
                        mStatus = dataSnapshot.child("status5").getValue().toString();
                        mstepFourTextTime.setText(tim);
                        GetStatus(mStatus);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list = new ArrayList<>();

        get_status = "https://bringo.biz/backend/api/get/order/status?ord_id=" + getIntent().getStringExtra("orderid");

        mtxt_ordertime =

                findViewById(R.id.txt_ordertime);

        mtxt_storename =

                findViewById(R.id.txt_storename);

        btnhis_detail =

                findViewById(R.id.btnhis_detail);

        mstoreimg =

                findViewById(R.id.storeimg);

        mstepOneFab =

                findViewById(R.id.stepOneFab);

        mstepTwoBar =

                findViewById(R.id.stepOneBar);

        mstepTwoFab =

                findViewById(R.id.stepTwoFab);

        mstepThreeBar =

                findViewById(R.id.stepThreeBar);

        mstepThreeFab =

                findViewById(R.id.stepThreeFab);

        mstepFourBar =

                findViewById(R.id.stepFourBar);

        mstepFourFab =

                findViewById(R.id.stepFourFab);

        mtxt_storename.setText(

                getIntent().

                        getStringExtra("storename"));
        mtxt_ordertime.setText(

                getIntent().

                        getStringExtra("created"));
        Glide.with(this).

                load(getIntent().

                        getStringExtra("storeimage")).

                apply(new RequestOptions().

                        placeholder(R.drawable.placeholder)).

                into(mstoreimg).

                toString();

        position =

                getIntent().

                        getIntExtra("position", 0);


        btnhis_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View mView = LayoutInflater.from(OrderTrackActivity.this).inflate(R.layout.orderdetail_sheet, null);
                Toolbar mappBar = mView.findViewById(R.id.appBar);

                mappBar.setTitle("Order Detail");
                final RecyclerView recyclerView = mView.findViewById(R.id.oorderdetail_gd);
                TextView textView = mView.findViewById(R.id.totalitemprice);
                TextView txt_addresss = mView.findViewById(R.id.txt_addresss);
                final TextView txt_totalproductss = mView.findViewById(R.id.txt_totalproductss);
                TextView txt_total_qtys = mView.findViewById(R.id.txt_total_qtys);
                TextView mstatus = mView.findViewById(R.id.status);
                if (!mStatus.equals("")) {
                    mstatus.setText(mStatus);
                }

                textView.setText("" + getIntent().getStringExtra("price"));
                txt_addresss.setText("" + getIntent().getStringExtra("add"));

                txt_total_qtys.setText("" + getIntent().getStringExtra("orderid"));
                list.clear();

                String url = "https://bringo.biz/backend/api/get/stores/orders?str_id=" + getIntent().getStringExtra("storeid") + "&ord_id=" + getIntent().getStringExtra("orderid");
                RequestQueue requestQueue = Volley.newRequestQueue(OrderTrackActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            txt_totalproductss.setText("" + response.length());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject storeObject = response.getJSONObject(i);

                                String pname = storeObject.getString("sp_name");
                                String actprice = storeObject.getString("act_prc");
                                String proimage = storeObject.getString("sp_image");
                                String pqty = storeObject.getString("ord_qty");


                                list.add(new CatLvlItemList(pname, actprice, pqty, proimage, getIntent().getStringExtra("storeid")));

                            }
                            OrderAdapter orderAdapter = new OrderAdapter(list, OrderTrackActivity.this, true, false);
                            recyclerView.setAdapter(orderAdapter);
                            progressDialog.cancel();

                        } catch (JSONException e) {
                            orderdetailSheetDialog.cancel();
                            progressDialog.cancel();
                            Toast.makeText(OrderTrackActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                orderdetailSheetDialog.cancel();
                                progressDialog.cancel();
                                Toast.makeText(OrderTrackActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                );
                requestQueue.add(jsonArrayRequest);


                FloatingActionButton mfabClose = mView.findViewById(R.id.fabClose);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderTrackActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);


//                OrderHistoryDetailAdapter orderAdapter = new OrderHistoryDetailAdapter(new ArrayList<OrderHistory>(list), getApplicationContext());
//                recyclerView.setAdapter(orderAdapter);
//                progressDialog.cancel();
//                list.clear();


                orderdetailSheetDialog = new BottomSheetDialog(mView.getContext());
                orderdetailSheetDialog.setContentView(mView);
                orderdetailSheetDialog.show();
                progressDialog.show();

            }
        });


    }

    public void GetStatus(String status) {
        if (status.equals("Accepted")) {
            mstepOneFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        } else if (status.equals("Assembled")) {
            mstepOneFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepTwoBar.setProgress(100);
            mstepTwoBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

            mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        } else if (status.equals("On Route")) {
            mstepOneFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepTwoBar.setProgress(100);
            mstepTwoBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepThreeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepThreeBar.setProgress(100);
            mstepThreeBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (status.equals("Delivered")) {
            mstepOneFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepTwoBar.setProgress(100);
            mstepTwoBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepThreeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            mstepThreeBar.setProgress(100);
            mstepThreeBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            mstepFourBar.setProgress(100);
            mstepFourBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            mstepFourFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}