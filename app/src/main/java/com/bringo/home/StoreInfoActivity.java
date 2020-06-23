package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.CategoryAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.Category;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreInfoActivity extends AppCompatActivity {

    private ImageButton mbtnBack, mbtn_search;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private HelpingMethods helpingMethods;
    private TextView mstName, mstAddress,mtotalAmount;;
    private Button mretryBtn;
    private TextView textCartItemCount;
    private ProgressDialog mProgressDialog;
    private List<String> backupList;
    private List<Category> productList;
    ProgressBar mprogressbar;
    private String stID, ownerID, ownerImage, ownerName, ownerAdd;
    private RecyclerView categoryRecyclerView;

    public static boolean flagfroprice = true;
    public static int mCartItemCount = 0;
    private ImageView mstImage;
    public static List<CatLvlItemList> favlist = new ArrayList<>();
    public static List<String> checklist = new ArrayList<>();
    private ImageView mbasketImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        mProgressDialog = new ProgressDialog(StoreInfoActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mbtn_search = findViewById(R.id.btnSearch);
        stID = getIntent().getStringExtra("storeid");
        ownerName = getIntent().getStringExtra("stname");
        ownerAdd = getIntent().getStringExtra("address");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");
        helpingMethods = new HelpingMethods(this);
        mtotalAmount = findViewById(R.id.totalAmount);
        textCartItemCount = findViewById(R.id.cart_badge);
        mbasketImageView = findViewById(R.id.basketImageView);
        mbasketImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (helpingMethods.GetCartCount(helpingMethods.GetStoreID()) > 0) {
                        Intent intent = new Intent(StoreInfoActivity.this, CartActivity.class);
                        intent.putExtra("StID",stID);
                        intent.putExtra("catName", "");
                        intent.putExtra("for", "finish");
                        intent.putExtra("stname", ownerName);
                        intent.putExtra("ownerID", helpingMethods.GetStoreUID());
                        intent.putExtra("ownerImage", helpingMethods.GetStoreImage());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }



            }
        });


        mstImage = findViewById(R.id.stImageView);
        mstName = findViewById(R.id.stName);
        mstAddress = findViewById(R.id.stAddress);
        mretryBtn = findViewById(R.id.retryBtn);



        //Glide.with(getApplicationContext()).asBitmap().load(ownerImage).apply(new RequestOptions().placeholder(R.drawable.store_background)).into(mstImage);


        mstAddress.setText(ownerAdd);
        mstName.setText(ownerName);

        mbtnBack = findViewById(R.id.btnBack);

        backupList = new ArrayList<>();


        productList = new ArrayList<>();

        categoryRecyclerView = findViewById(R.id.cat_recyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(StoreInfoActivity.this, 2));

        mbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreInfoActivity.this, SearchActivity.class);
                intent.putExtra("stID", stID);
                intent.putExtra("catName", "");
                intent.putExtra("stname", ownerName);
                intent.putExtra("ownerID", ownerID);
                intent.putExtra("ownerImage", ownerImage);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

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
        request = new JsonArrayRequest("https://bringo.biz/api/get/stores/products?str_id=" + stID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String catTitle = jsonObject.getString("m_name");
                        String catimage = jsonObject.getString("thumbnail");
                        if (!backupList.contains(jsonObject.getString("m_name"))) {
                            backupList.add(jsonObject.getString("m_name"));
                            productList.add(new Category(catimage, catTitle,""));
                        }


                    } catch (JSONException e) {
                        mProgressDialog.cancel();
                        mretryBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(StoreInfoActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(StoreInfoActivity.this, "" + error, Toast.LENGTH_SHORT).show();


            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);







    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void MainBadge() {
                if (helpingMethods.GetCartCount(stID) == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText("" + helpingMethods.GetCartCount(stID));
                    //textCartItemCount.setText(""+2);
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }

                }


    }

    @Override
    protected void onResume() {
        super.onResume();
        MainBadge();

                if(helpingMethods.newone(stID)>0){
                    mtotalAmount.setText("Rs."+helpingMethods.newone(stID)+"/-");
                    mtotalAmount.setVisibility(View.VISIBLE);
                }else {
                    mtotalAmount.setVisibility(View.GONE);
                }



        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

}
