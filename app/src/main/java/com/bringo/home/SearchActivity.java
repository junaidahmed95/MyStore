package com.bringo.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.PCatAdapter;
import com.bringo.home.Adapter.SearchProductAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.bringo.home.ui.home.HomeFragment.nearesStoresList;


public class SearchActivity extends AppCompatActivity {
    private ArrayList<CatLvlItemList> list;
    private ArrayList<CatLvlItemList> prolist;
    Toolbar toolbar;
    private static TextView textCartItemCount;
    private static HelpingMethods helpingMethods;
    private SearchProductAdapter searchjAdapter;
    private Boolean IsAdded = false;
    //

    private ShimmerFrameLayout mshimmer_search;
    EditText meditText;

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;

    PCatAdapter pCatAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView.LayoutManager mLayoutManager;
    private String ownerID, ownerImage, ownerName, cat_Name, search;
    private static String store_ID;
    private String getid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mshimmer_search = findViewById(R.id.shimmer_search);
        mshimmer_search.startShimmerAnimation();


        store_ID = getIntent().getStringExtra("stID");
        cat_Name = getIntent().getStringExtra("catName");
        ownerName = getIntent().getStringExtra("stname");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");

        meditText = findViewById(R.id.edittext);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        helpingMethods = new HelpingMethods(this);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        list = new ArrayList<>();

        prolist = new ArrayList<>();
        ConnectionDetector connectionDetector = new ConnectionDetector(this);


        search = getIntent().getStringExtra("search");


        if (search.equals("mulstore")) {
            if (connectionDetector.isConnected()) {
                seach();
            } else {
                Toast.makeText(this, "Check your internet and retry again.", Toast.LENGTH_SHORT).show();
            }
        } else if (search.equals("cat")) {
            if (connectionDetector.isConnected()) {
                searchjAdapter = new SearchProductAdapter(SubCatActivity.prolist, SearchActivity.this);
                mRecyclerView.setAdapter(searchjAdapter);
                mshimmer_search.stopShimmerAnimation();
                mshimmer_search.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                searchjAdapter.notifyDataSetChanged();

                meditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().equals("")) {
                            filter(s.toString());
                        } else {
                            searchjAdapter = new SearchProductAdapter(SubCatActivity.prolist, SearchActivity.this);
                            mRecyclerView.setAdapter(searchjAdapter);
                            searchjAdapter.notifyDataSetChanged();

                        }


                    }
                });
                if (!meditText.getText().toString().equals("")) {
                    filter(meditText.getText().toString());
                }
            } else {
                Toast.makeText(this, "Check your internet and retry again.", Toast.LENGTH_SHORT).show();
            }
        } else if (search.equals("single")) {
            if (connectionDetector.isConnected()) {
                createExampleList();
            } else {
                Toast.makeText(this, "Check your internet and retry again.", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void filter(String text) {
        ArrayList<CatLvlItemList> filteredList = new ArrayList<>();

        if (search.equals("cat")) {
            for (CatLvlItemList item : SubCatActivity.prolist) {
                if (item.getDesc().toString().toLowerCase().contains(text.toLowerCase()) || item.getCatName().toString().toLowerCase().contains(text.toLowerCase()) ||item.getP_price().toString().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        } else {
            for (CatLvlItemList item : prolist) {
                if (item.getDesc().toString().toLowerCase().contains(text.toLowerCase()) || item.getCatName().toString().toLowerCase().contains(text.toLowerCase()) ||item.getP_price().toString().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        if (search.equals("mulstore") || search.equals("cat")) {
            searchjAdapter.filterList(filteredList);
        } else if (search.equals("single")) {
            pCatAdapter.filterList(filteredList);
        }

    }


    private void createExampleList() {


        JsonArrayRequest request = new JsonArrayRequest("https://bringo.biz/backend/api/get/stores/products?str_id=" + getIntent().getStringExtra("stID"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        String cat_id = jsonObject.getString("cat_a_id");
                        String mCat = jsonObject.getString("p_name");
                        String str_id = jsonObject.getString("str_id");
                        String mTitle = jsonObject.getString("product_name");
                        String mprice = jsonObject.getString("str_prc");
                        String mimage = jsonObject.getString("product_image");
                        String product_id = jsonObject.getString("p_id");
                        String sim_id = jsonObject.getString("id");
                        String desc = jsonObject.getString("product_unit");
                        prolist.add(new CatLvlItemList(mTitle, mprice, mimage, product_id, str_id, mCat, sim_id, mprice, desc, cat_id));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pCatAdapter = new PCatAdapter(prolist, SearchActivity.this, store_ID, ownerID, ownerImage, ownerName, cat_Name, true);
                mRecyclerView.setAdapter(pCatAdapter);
                mshimmer_search.stopShimmerAnimation();
                mshimmer_search.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                pCatAdapter.notifyDataSetChanged();

                meditText.setText(getIntent().getStringExtra("value"));
                meditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().equals("")) {
                            filter(s.toString());
                        } else {
                            pCatAdapter = new PCatAdapter(prolist, SearchActivity.this, store_ID, ownerID, ownerImage, ownerName, cat_Name, true);
                            mRecyclerView.setAdapter(pCatAdapter);
                            pCatAdapter.notifyDataSetChanged();
                        }


                    }
                });
                if (!meditText.getText().toString().equals("")) {
                    filter(meditText.getText().toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


//        list = new ArrayList<>();
//        list.add(new SearchClass(R.drawable.arrow, "ONE PEPSI", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Two ", "Line 2"));
//        list.add(new SearchClass(R.drawable.attachment_ic, "Three", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Four", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Five", "Line 2"));
//        list.add(new SearchClass(R.drawable.arrow, "AATA", "Line 2"));
//        list.add(new SearchClass(R.drawable.arrow, "Seven", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Eight", "Line 2"));
//        list.add(new SearchClass(R.drawable.attachment_ic, "Nine", "Line 2"));
    }


    private void seach() {


        for (int i = 0; i < nearesStoresList.size(); i++) {
            getid += "str_id[]=" + nearesStoresList.get(i).getId() + "&";
        }

        JsonArrayRequest request = new JsonArrayRequest("https://bringo.biz/backend/api/search/store/products?" + getid + "search=" + meditText.getText().toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String cat_id = jsonObject.getString("m_name");
                        String mCat = jsonObject.getString("p_name");
                        String str_id = jsonObject.getString("str_id");
                        String mTitle = jsonObject.getString("product_name");
                        String mprice = jsonObject.getString("str_prc");
                        String mimage = jsonObject.getString("product_image");
                        String product_id = jsonObject.getString("p_id");
                        String sim_id = jsonObject.getString("id");
                        String desc = jsonObject.getString("product_unit");
                        String ownerID = jsonObject.getString("u_id");
                        String ownerName = jsonObject.getString("str_name");
                        String ownerImage = jsonObject.getString("str_image");

                        prolist.add(new CatLvlItemList(mTitle, mprice, mimage, product_id, str_id, mCat, sim_id, mprice, desc, cat_id, ownerID, ownerName, ownerImage));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                searchjAdapter = new SearchProductAdapter(prolist, SearchActivity.this);
                mRecyclerView.setAdapter(searchjAdapter);
                mshimmer_search.stopShimmerAnimation();
                mshimmer_search.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                searchjAdapter.notifyDataSetChanged();

                meditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().equals("")) {
                            filter(s.toString());
                        } else {
                            searchjAdapter = new SearchProductAdapter(prolist, SearchActivity.this);
                            mRecyclerView.setAdapter(searchjAdapter);
                            searchjAdapter.notifyDataSetChanged();
                        }


                    }
                });
                if (!meditText.getText().toString().equals("")) {
                    filter(meditText.getText().toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


//        list = new ArrayList<>();
//        list.add(new SearchClass(R.drawable.arrow, "ONE PEPSI", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Two ", "Line 2"));
//        list.add(new SearchClass(R.drawable.attachment_ic, "Three", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Four", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Five", "Line 2"));
//        list.add(new SearchClass(R.drawable.arrow, "AATA", "Line 2"));
//        list.add(new SearchClass(R.drawable.arrow, "Seven", "Line 2"));
//        list.add(new SearchClass(R.drawable.audio, "Eight", "Line 2"));
//        list.add(new SearchClass(R.drawable.attachment_ic, "Nine", "Line 2"));
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);


        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        SearchsetupBadge();
        return true;
    }

    public static void SearchsetupBadge() {
        if (helpingMethods.GetStoreID() != null) {
            if (helpingMethods.GetCartCount(helpingMethods.GetStoreID()) == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText("" + helpingMethods.GetCartCount(helpingMethods.GetStoreID()));
                //textCartItemCount.setText(""+2);
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }

            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_cart) {
            if (helpingMethods.GetCartCount(store_ID) > 0) {
                Intent intent = new Intent(this, CartActivity.class);
                intent.putExtra("StID", store_ID);
                intent.putExtra("for", "search");
                intent.putExtra("catName", cat_Name);
                intent.putExtra("stname", ownerName);
                intent.putExtra("ownerID", ownerID);
                intent.putExtra("ownerImage", ownerImage);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

}