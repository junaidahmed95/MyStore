package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mystore.Adapter.AllStoreAdapter;
import com.example.mystore.Adapter.CategoryAdapter;
import com.example.mystore.Model.AllStore;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.Category;
import com.example.mystore.Model.ConnectionDetector;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllStoresActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/maincat";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private Button mretryBtn;
    private ProgressBar mloadingImage;
    private ProgressDialog mProgressDialog;
    private List<String> backupList;
    private List<Category> productList;
    ProgressBar mprogressbar;
    private String stID,ownerID,ownerImage,ownerName;
    private GridView categoryRecyclerView;

    public static boolean flagfroprice = true;
    public static TextView textCartItemCount;
    public static int mCartItemCount = 0;


    public static List<CatLvlItemList> favlist = new ArrayList<>();
    public static List<String> checklist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);

        mloadingImage = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new CubeGrid();
        mloadingImage.setIndeterminateDrawable(doubleBounce);
        mretryBtn = findViewById(R.id.retryBtn);



        stID = getIntent().getStringExtra("storeid");
        ownerName = getIntent().getStringExtra("stname");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(ownerName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backupList = new ArrayList<>();


        productList = new ArrayList<>();

        categoryRecyclerView = findViewById(R.id.cat_recyclerView);




        ConnectionDetector connectionDetector = new ConnectionDetector(AllStoresActivity.this);
        if(connectionDetector.isConnected()){
            parseJSON();
        }else {
            mloadingImage.setVisibility(View.GONE);
            mretryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(AllStoresActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }

        mretryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(AllStoresActivity.this);
                if(connectionDetector.isConnected()){
                    mloadingImage.setVisibility(View.VISIBLE);
                    mretryBtn.setVisibility(View.GONE);
                    parseJSON();
                }else {
                    Toast.makeText(AllStoresActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }
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
                        if(!backupList.contains(jsonObject.getString("m_name"))){
                            backupList.add(jsonObject.getString("m_name"));
                            productList.add(new Category(catimage, catTitle));
                        }



                    } catch (JSONException e) {
                        mloadingImage.setVisibility(View.GONE);
                        mretryBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(AllStoresActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(AllStoresActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                    }
                }


                CategoryAdapter categoryAdapter = new CategoryAdapter(productList, AllStoresActivity.this, stID,ownerID,ownerImage,ownerName);
                categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
                if (productList.size() == 0) {
                    Toast.makeText(AllStoresActivity.this, "This store does not have any category yet!", Toast.LENGTH_LONG).show();
                }
                mloadingImage.setVisibility(View.GONE);
                categoryRecyclerView.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mloadingImage.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.VISIBLE);
                Toast.makeText(AllStoresActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AllStoresActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();


            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {


                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //Toast.makeText(this, ""+result.get(0), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("value", result.get(0));
                intent.putExtra("stID",stID);
                startActivity(intent);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            finish();

        }else if (id == R.id.menu_search) {


            Intent intent = new Intent(AllStoresActivity.this, SearchActivity.class);

            startActivity(intent);


        } else if (id == R.id.menu_mic) {

            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");


            try {
                startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT);

            } catch (Exception e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//   // public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == android.R.id.home) {
//
//            finish();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}