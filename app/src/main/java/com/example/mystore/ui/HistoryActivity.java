package com.example.mystore.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Adapter.HistoryAdapter;
import com.example.mystore.Adapter.HistoryAdapter2;
import com.example.mystore.Adapter.OrderAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.ConnectionDetector;
import com.example.mystore.Model.OrderHistory;
import com.example.mystore.Model.RequestHandlerSingleten;
import com.example.mystore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private StringRequest request;
    private RequestQueue requestQueue;
    //private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/order?user_id=jAHDba6PiRNDzgT8QadpePR1eju1";
    private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    RecyclerView mhis_recycler;
    List<OrderHistory> historylist;
    List<OrderHistory> products_list;
    private int qtyplus = 0;
    private int plus = 0;
    private ProgressDialog progressDialog;
    private int count = 0;


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        toolbar = findViewById(R.id.bar);
        toolbar.setTitle("Order History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mhis_recycler = findViewById(R.id.his_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mhis_recycler.setLayoutManager(linearLayoutManager);

        historylist = new ArrayList<>();
        products_list = new ArrayList<>();

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnected()) {
            parseJSON();
        } else {
            mProgressDialog.cancel();
            Toast.makeText(this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
        }


    }

    private void parseJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray storename = response.getJSONArray(0);
                    JSONObject store = response.getJSONObject(1);
                    Iterator<String> keys = store.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        JSONArray storeArray = (JSONArray) store.getJSONArray(key);
                        for (int i = 0; i < storeArray.length(); i++) {
                            JSONObject storeObject = storeArray.getJSONObject(i);
                            JSONObject s = storename.getJSONObject(count);
                            count++;
                            String l = s.getString("str_name");
                            String pname = storeObject.getString("sp_name");
                            String actprice = storeObject.getString("act_prc");
                            String address = storeObject.getString("new_address");
                            String proimage = storeObject.getString("sp_image");
                            String pqty = storeObject.getString("ord_qty");
                            String tprice = storeObject.getString("t_price");
                            String datetime = storeObject.getString("created_at");
                            String uid = storeObject.getString("user_id");
                            String tpprice = storeObject.getString("str_prc");

                            products_list.add(new OrderHistory(actprice, pqty, l, datetime, proimage, pname, uid, address, null, tprice, tpprice));

                        }
                        historylist.add(new OrderHistory(key, new ArrayList<OrderHistory>(products_list)));
                        products_list.clear();

                        HistoryAdapter historyadp = new HistoryAdapter(historylist, HistoryActivity.this);
                        mhis_recycler.setAdapter(historyadp);
                        historyadp.notifyDataSetChanged();
                        mProgressDialog.cancel();
                    }


                } catch (JSONException e) {
                    mProgressDialog.cancel();
                    Toast.makeText(HistoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.cancel();
                        Toast.makeText(HistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;

    }
}