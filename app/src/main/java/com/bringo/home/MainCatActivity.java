package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.MainCategoryAdapter;
import com.bringo.home.Adapter.StoresAdapter;
import com.bringo.home.Model.Category;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.ShowStores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bringo.home.ui.home.HomeFragment.nearesStoresList;

public class MainCatActivity extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView mstore_catwise;
    private CardView mcdv_dialog;
    private ProgressBar mloadingImage;
    private Button mretryBtn;
    private List<ShowStores> storesList;
    private String getid ="";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cat);

        toolbar = findViewById(R.id.appBar);
        toolbar.setTitle("Stores");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mstore_catwise = findViewById(R.id.store_catwise);
        mloadingImage = findViewById(R.id.spin_kit);
        mretryBtn = findViewById(R.id.retryBtn);
        mstore_catwise.setLayoutManager(new GridLayoutManager(this, 2));
        mcdv_dialog = findViewById(R.id.cdv_dialog);


        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnected()) {
            mloadingImage.setVisibility(View.VISIBLE);
            mretryBtn.setVisibility(View.GONE);
            GetNearByStores();

        } else {
            Toast.makeText(this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }



        storesList = new ArrayList<>();
        mretryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(MainCatActivity.this);
                if (connectionDetector.isConnected()) {
                    mloadingImage.setVisibility(View.VISIBLE);
                    mretryBtn.setVisibility(View.GONE);
                    GetNearByStores();

                } else {
                    Toast.makeText(MainCatActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }





    private void GetNearByStores() {

        for(int i =0; i< nearesStoresList.size();i++){
            getid +="str_id[]="+ nearesStoresList.get(i).getId()+"&";
        }

        request = new JsonArrayRequest("https://bringo.biz/api/search/store/category?="+getid+"cat_id="+getIntent().getStringExtra("cat_id"), new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.isNull(0)) {
                    mcdv_dialog.setVisibility(View.VISIBLE);
                    mloadingImage.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.VISIBLE);
                } else {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            String userID = jsonObject.getString("u_id");
                            String storename = jsonObject.getString("str_name");
                            String storeaddr = jsonObject.getString("address");
                            String store_id = jsonObject.getString("id");
//                            double distance1 = Double.parseDouble(jsonObject.getString("distance"));
                            String store_image = jsonObject.getString("thumbnail");
//                            NumberFormat formatter = new DecimalFormat("#0.00");
//                            String distance = formatter.format(distance1);
                            //Log.d("distance", formatter.format(distance1) + " " + storename);
                            storesList.add(new ShowStores(storename, store_id, userID, store_image, "", storeaddr));
                        } catch (JSONException e) {
                            mstore_catwise.setVisibility(View.GONE);
                            mloadingImage.setVisibility(View.GONE);
                            Toast.makeText(MainCatActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            mretryBtn.setVisibility(View.VISIBLE);
                        }
                    }
                    StoresAdapter  allStoreAdapter = new StoresAdapter(storesList, MainCatActivity.this, false);
                    mstore_catwise.setAdapter(allStoreAdapter);
                    mcdv_dialog.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.GONE);
                    mstore_catwise.setVisibility(View.VISIBLE);
                    allStoreAdapter.notifyDataSetChanged();
                    mloadingImage.setVisibility(View.GONE);
                    // mBtnViewAll.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mstore_catwise.setVisibility(View.GONE);
                mloadingImage.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.VISIBLE);
                if (getApplicationContext() != null) {
                    Toast.makeText(MainCatActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(MainCatActivity.this);
        requestQueue.add(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
