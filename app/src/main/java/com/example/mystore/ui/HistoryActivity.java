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
import com.example.mystore.Model.OrderHistory;
import com.example.mystore.Model.RequestHandlerSingleten;
import com.example.mystore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.gson.JsonArray;

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

    private StringRequest request;
    private RequestQueue requestQueue;
    private final String JSON_URL =  "https://chhatt.com/Cornstr/grocery/api/get/order?user_id=jAHDba6PiRNDzgT8QadpePR1eju1";
   // private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    RecyclerView mhis_recycler;
    List<OrderHistory> historylist;
    List<OrderHistory> products_list;
    private int qtyplus = 0;
    private int plus =0;
    private ProgressDialog progressDialog;


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

       // progressDialog = new ProgressDialog(this);
      //  progressDialog.setMessage("Please Wait...");
      ///  progressDialog.setCancelable(false);
       // progressDialog.show();


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

        parseJSON();


    }

    private void parseJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Do something with response
                //mTextView.setText(response.toString());

                // Process the JSON
                try{
                    // Loop through the array elements
                    String storename = response.getJSONObject(1).getString("str_name");

                    for(int i=0;i<response.length();i++){
                        // Get current json object
                        JSONObject student = response.getJSONObject(i);


                        Iterator<String> key = student.keys();

                        // String key = student.keys().toString();

                        while (key.hasNext()) {

                            String key12 = (String) key.next();//karachi key


                            JSONArray jsonArray = (JSONArray) student.get(key12);

                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String pname = jsonObject1.getString("sp_name");
                                String actprice = jsonObject1.getString("act_prc");
                                String address = jsonObject1.getString("new_address");
                                String proimage = jsonObject1.getString("sp_image");
                                String pqty = jsonObject1.getString("ord_qty");
                                String tprice = jsonObject1.getString("t_price");
                                String datetime = jsonObject1.getString("created_at");
                                String uid = jsonObject1.getString("user_id");
                                String tpprice = jsonObject1.getString("str_prc");





                                //String status = jsonObject1.getString("status");
                                //String size =""+  historylist.get(plus).getGetorderbykeylist().size();

                                products_list.add(new OrderHistory(actprice, pqty, storename, datetime, proimage, pname, uid, address, null,tprice , tpprice));



                            }

                            historylist.add(new OrderHistory(key12, new ArrayList<OrderHistory>(products_list)));
                            products_list.clear();


                        }


                    }
                    HistoryAdapter historyadp = new HistoryAdapter(historylist, HistoryActivity.this);
                    mhis_recycler.setAdapter(historyadp);
                    progressDialog.dismiss();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

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
