package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.CategoryAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.Category;
import com.bringo.home.Model.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreInfoActivity extends AppCompatActivity {

    private ImageButton mbtnBack;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/maincat";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private TextView mstName, mstAddress;
    private Button mretryBtn;
    private ProgressDialog mProgressDialog;
    private List<String> backupList;
    private List<Category> productList;
    ProgressBar mprogressbar;
    private String stID, ownerID, ownerImage, ownerName, ownerAdd;
    private RecyclerView categoryRecyclerView;

    public static boolean flagfroprice = true;
    public static TextView textCartItemCount;
    public static int mCartItemCount = 0;


    public static List<CatLvlItemList> favlist = new ArrayList<>();
    public static List<String> checklist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        mProgressDialog = new ProgressDialog(StoreInfoActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mstName = findViewById(R.id.stName);
        mstAddress = findViewById(R.id.stAddress);
        mretryBtn = findViewById(R.id.retryBtn);
        stID = getIntent().getStringExtra("storeid");
        ownerName = getIntent().getStringExtra("stname");
        ownerAdd = getIntent().getStringExtra("address");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");

        mstAddress.setText(ownerAdd);
        mstName.setText(ownerName);

        mbtnBack = findViewById(R.id.btnBack);

        backupList = new ArrayList<>();


        productList = new ArrayList<>();

        categoryRecyclerView = findViewById(R.id.cat_recyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(StoreInfoActivity.this, 2));

        ConnectionDetector connectionDetector = new ConnectionDetector(StoreInfoActivity.this);
        if (connectionDetector.isConnected()) {
            parseJSON();
        } else {
            mProgressDialog.cancel();
            mretryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(StoreInfoActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }

        mretryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(StoreInfoActivity.this);
                if (connectionDetector.isConnected()) {
                    mProgressDialog.show();
                    mretryBtn.setVisibility(View.GONE);
                    parseJSON();
                } else {
                    Toast.makeText(StoreInfoActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void parseJSON() {
        request = new JsonArrayRequest("https://chhatt.com/Cornstr/grocery/api/get/stores/products?str_id=" + stID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String catTitle = jsonObject.getString("m_name");
                        String catimage = jsonObject.getString("product_image");
                        if (!backupList.contains(jsonObject.getString("m_name"))) {
                            backupList.add(jsonObject.getString("m_name"));
                            productList.add(new Category(catimage, catTitle));
                        }


                    } catch (JSONException e) {
                        mProgressDialog.cancel();
                        mretryBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(StoreInfoActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(StoreInfoActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                    }
                }


                CategoryAdapter categoryAdapter = new CategoryAdapter(productList, StoreInfoActivity.this, stID, ownerID, ownerImage, ownerName);
                categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
                if (productList.size() == 0) {
                    Toast.makeText(StoreInfoActivity.this, "This store does not have any category yet!", Toast.LENGTH_LONG).show();
                }
                mProgressDialog.cancel();
                categoryRecyclerView.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                mretryBtn.setVisibility(View.VISIBLE);
                Toast.makeText(StoreInfoActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(StoreInfoActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();


            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
