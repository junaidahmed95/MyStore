package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.AllStoreAdapter;
import com.example.mystore.Model.AllStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllStoresActivity extends AppCompatActivity {

    private String get_store = "https://chhatt.com/Cornstr/grocery/api/get/allstores";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    private GridView grd_str;
    private AllStoreAdapter allStoreAdapter;
    private List<AllStore> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Store");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        grd_str = findViewById(R.id.gd1);
        storeList = new ArrayList<>();

        getstore();

    }

    private void getstore() {

        request = new JsonArrayRequest(get_store, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        String storename = jsonObject.getString("str_name");
                        String store_id = jsonObject.getString("id");
                        String distance = jsonObject.getString("address");
                        String store_image = jsonObject.getString("user_thumb");


                        storeList.add(new AllStore(store_image, storename, distance, store_id));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allStoreAdapter = new AllStoreAdapter(storeList, AllStoresActivity.this);
                grd_str.setAdapter(allStoreAdapter);
                allStoreAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }
}
