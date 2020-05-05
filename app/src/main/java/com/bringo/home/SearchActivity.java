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
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.PCatAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.HelpingMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private ArrayList<CatLvlItemList> list;
    private ArrayList<CatLvlItemList> prolist;
    Toolbar toolbar;
    private static TextView textCartItemCount;
    private static HelpingMethods helpingMethods;
    private Boolean IsAdded = false;
    private ProgressDialog mProgressDialog;


    EditText meditText;

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;

    PCatAdapter pCatAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        meditText = findViewById(R.id.edittext);
        toolbar = findViewById(R.id.toolbar);
        helpingMethods = new HelpingMethods(this);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        list = new ArrayList<>();

        prolist = new ArrayList<>();
        createExampleList();


    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.cart_menu, menu);
//
//        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
//        View actionView = MenuItemCompat.getActionView(menuItem);
//        // if (checklist.size() == 0) {
//        //    textCartItemCount = actionView.findViewById(R.id.cart_badge);
//        // }
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            CheckForCart();
//            finish();
//        } else if (id == R.id.menu_cart) {
//            if (!textCartItemCount.getText().toString().equals("0") && checklist.size() > 0) {
//                Intent intent = new Intent(this, CartActivity.class);
//                startActivity(intent);
//            }
//        }
//        return true;
//    }


    private void filter(String text) {
        ArrayList<CatLvlItemList> filteredList = new ArrayList<>();

        for (CatLvlItemList item : prolist) {
            if (item.getP_name().toLowerCase().contains(text.toLowerCase()) || item.getP_price().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        pCatAdapter.filterList(filteredList);
    }


    private void createExampleList() {


        JsonArrayRequest request = new JsonArrayRequest("https://bringo.biz/api/get/stores/products?str_id=" + getIntent().getStringExtra("stID"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        String mCat = jsonObject.getString("p_name");
                        String str_id = jsonObject.getString("str_id");
                        String mTitle = jsonObject.getString("product_name");
                        String mprice = jsonObject.getString("str_prc");
                        String mimage = jsonObject.getString("product_image");
                        String product_id = jsonObject.getString("p_id");
                        String sim_id = jsonObject.getString("id");
                        String desc = jsonObject.getString("product_unit");
                        prolist.add(new CatLvlItemList(mTitle, mprice, mimage, product_id, str_id, mCat, sim_id, mprice, desc));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pCatAdapter = new PCatAdapter(prolist, SearchActivity.this, getIntent().getStringExtra("stid"), null, null, null, null,true);
                mRecyclerView.setAdapter(pCatAdapter);
                pCatAdapter.notifyDataSetChanged();
                mProgressDialog.cancel();
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
                        }else {
                            pCatAdapter = new PCatAdapter(prolist, SearchActivity.this, getIntent().getStringExtra("stid"), null, null, null, null,true);
                            mRecyclerView.setAdapter(pCatAdapter);
                            pCatAdapter.notifyDataSetChanged();
                        }


                    }
                });
                if (!meditText.getText().toString().trim().equals("")) {
                    filter(meditText.getText().toString().trim());
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
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
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

}