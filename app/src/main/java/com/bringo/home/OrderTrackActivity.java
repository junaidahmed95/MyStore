package com.bringo.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.bringo.home.Adapter.OrderHistoryDetailAdapter;
import com.bringo.home.Model.OrderHistory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.bringo.home.Adapter.StatusAdapter.historylist;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OrderTrackActivity extends AppCompatActivity {

    String get_status;
    TextView mtxt_ordertime, mtxt_storename;
    CircleImageView mstoreimg;
    Button btnhis_detail;
    int position = 0;
    private int qtycount = 0;
    BottomSheetDialog orderdetailSheetDialog;
    List<OrderHistory> list;
    String chkstuts;

    FloatingActionButton mstepOneFab, mstepTwoFab, mstepThreeFab, mstepFourFab;
    ProgressBar mstepTwoBar, mstepThreeBar, mstepFourBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        list = new ArrayList<>();
        get_status = "https://bringo.biz/api/get/order/status?ord_id="+getIntent().getStringExtra("orderid");
        mtxt_ordertime = findViewById(R.id.txt_ordertime);
        mtxt_storename = findViewById(R.id.txt_storename);
        btnhis_detail = findViewById(R.id.btnhis_detail);
        mstoreimg = findViewById(R.id.storeimg);

        mstepOneFab = findViewById(R.id.stepOneFab);
        mstepTwoBar = findViewById(R.id.stepOneBar);

        mstepTwoFab = findViewById(R.id.stepTwoFab);
        mstepThreeBar = findViewById(R.id.stepThreeBar);

        mstepThreeFab = findViewById(R.id.stepThreeFab);
        mstepFourBar = findViewById(R.id.stepFourBar);

        mstepFourFab = findViewById(R.id.stepFourFab);

        mtxt_storename.setText(getIntent().getStringExtra("storename"));
        mtxt_ordertime.setText(getIntent().getStringExtra("created"));
        Glide.with(this).load(getIntent().getStringExtra("storeimage")).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mstoreimg).toString();
        position = getIntent().getIntExtra("position",0);
        status();

        btnhis_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View mView = LayoutInflater.from(OrderTrackActivity.this).inflate(R.layout.orderdetail_sheet, null);
                Toolbar mappBar = mView.findViewById(R.id.appBar);

                mappBar.setTitle("Order Detail");
                final RecyclerView recyclerView = mView.findViewById(R.id.oorderdetail_gd);

                TextView mtxt_qtys = mView.findViewById(R.id.txt_total_qtys);
                TextView mtxt_totalproductss = mView.findViewById(R.id.txt_totalproductss);
                TextView mtxt_addresss = mView.findViewById(R.id.txt_addresss);
                TextView mtxt_prices = mView.findViewById(R.id.totalitemprice);
                TextView mstatus = mView.findViewById(R.id.status);


                for (int a = 0; a < historylist.get(position).getGetorderbykeylist().size(); a++) {
                    Log.d("Order",historylist.get(position).getGetorderbykeylist().get(a).getUaddress());
                    mtxt_addresss.setText(historylist.get(position).getGetorderbykeylist().get(a).getUaddress());
                    mtxt_prices.setText(historylist.get(position).getGetorderbykeylist().get(a).getPtotalprice());
                    chkstuts = historylist.get(position).getGetorderbykeylist().get(a).getStatus();
                    list.add(new OrderHistory(historylist.get(position).getGetorderbykeylist().get(a).getMtxt_price(), historylist.get(position).getGetorderbykeylist().get(a).getMtxt_qty(), historylist.get(position).getGetorderbykeylist().get(a).getAct_price(), historylist.get(position).getGetorderbykeylist().get(a).getImage(), historylist.get(position).getGetorderbykeylist().get(a).getTitle()));
                }
                mtxt_totalproductss.setText(""+ historylist.get(position).getGetorderbykeylist().size());
                mtxt_qtys.setText(""+historylist.get(position).getOrderid());
                if(chkstuts.equals("1")){
                    mstatus.setText("Acceted");
                }else if(chkstuts.equals("2")){
                    mstatus.setText("Assembled");
                }else if(chkstuts.equals("3")){
                    mstatus.setText("OnRoute");
                }else if(chkstuts.equals("4")){
                    mstatus.setText("Delivered");
                }



                FloatingActionButton mfabClose = mView.findViewById(R.id.fabClose);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderTrackActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);


                OrderHistoryDetailAdapter orderAdapter = new OrderHistoryDetailAdapter(new ArrayList<OrderHistory>(list), getApplicationContext());
                recyclerView.setAdapter(orderAdapter);

                list.clear();


                orderdetailSheetDialog = new BottomSheetDialog(mView.getContext());
                orderdetailSheetDialog.setContentView(mView);
                orderdetailSheetDialog.show();


            }
        });




    }

    public void status() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, get_status, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(JSONArray response) {
                try {


                    JSONObject steps = response.getJSONObject(0);
                    String status = steps.getString("status");
                    if (status.equals("1")) {

                        mstepOneFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepTwoBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));


                    } else if (status.equals("2")) {
                        mstepThreeBar.setProgress(100);
                        mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepThreeBar.setProgressBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    } else if (status.equals("3")) {
                        mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepThreeBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepFourBar.setProgress(100);
                        mstepThreeBar.setProgress(100);

                        mstepThreeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepFourBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                    } else if (status.equals("4")) {
                        mstepTwoFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepThreeBar.setProgressBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepFourBar.setProgress(100);
                        mstepThreeBar.setProgress(100);
                        mstepThreeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        mstepFourBar.setProgressBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));


                        mstepFourFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    }


                } catch (JSONException e) {
                    // mProgressDialog.cancel();
                    Toast.makeText(OrderTrackActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mProgressDialog.cancel();
                        Toast.makeText(OrderTrackActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }
}