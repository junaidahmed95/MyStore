package com.example.mystore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.HelpingMethods;
import com.example.mystore.Model.ShowStores;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;
import static com.example.mystore.MainActivity.checklist;
import static com.example.mystore.MainActivity.textCartItemCount;

public class SubCatActivity extends AppCompatActivity {


    private String mJSON_URL = "";
    private String[] tabTitles;

    private JsonArrayRequest mrequest;
    public static ProgressDialog mProgressDialog;
    private List<String> dummyList;
    private RequestQueue mrequestQueue;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    int position;

    public static List<CatLvlItemList> list, real;
    private List<String> store;
    public static List<ShowStores> storelist;

    public static TabLayout mtabs;
    private ViewPager mviewpager;
    private Spinner msp_selectStore;
    private ProgressBar mProgressBar;

    public static HelpingMethods helpingMethods;
    private Boolean IsAdded = false;
    public static List<CatLvlItemList> prolist;
    public static FloatingActionButton mfbcart;
    int no_of_categories = -1;
    private String catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);
        mProgressDialog = new ProgressDialog(SubCatActivity.this);
        mProgressDialog.setMessage("Getting Products...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        catName = getIntent().getStringExtra("catName");
        list = new ArrayList<>();
        store = new ArrayList<>();
        storelist = new ArrayList<>();
        dummyList = new ArrayList<>();
        helpingMethods = new HelpingMethods(SubCatActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Grocery");
        //toolbar.setTitleMargin(0,0,5,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prolist = new ArrayList<>();

        mfbcart = findViewById(R.id.fbcart);
        mtabs = findViewById(R.id.tabs);
        mviewpager = findViewById(R.id.viewpager);

        GetStoreData();
        mfbcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsAdded = true;
                Toast.makeText(SubCatActivity.this, "Product Is Added", Toast.LENGTH_SHORT).show();
            }
        });


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
        setupBadge();
        return true;
    }

    public static void setupBadge() {
        if (helpingMethods.GetCartCount() == 0) {
            if (textCartItemCount.getVisibility() != View.GONE) {
                textCartItemCount.setVisibility(View.GONE);
            }
        } else {
            textCartItemCount.setText("" + helpingMethods.GetCartCount());
            //textCartItemCount.setText(""+2);
            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                textCartItemCount.setVisibility(View.VISIBLE);
            }

        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_cart) {
            if (helpingMethods.GetCartCount() > 0) {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);

            }
        }
        return true;
    }





    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        Fragment fragment = null;

        public ViewPagerAdapter(FragmentManager fm, int size) {
            super(fm);

        }


        @Override
        public Fragment getItem(int position) {
            for (int i = 0; i < tabTitles.length; i++) {
                if (i == position) {

                    fragment = new CatLvlFragment();


                    break;
                }
            }

            return fragment;

        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


    private void GetStoreData() {
        mJSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/stores/products?str_id=" + getIntent().getStringExtra("storeid");
        mrequest = new JsonArrayRequest(mJSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dummyList.clear();
                prolist.clear();
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getString("m_name").equals(catName)) {
                            if (!dummyList.contains(jsonObject.getString("p_name"))) {
                                dummyList.add(jsonObject.getString("p_name"));
                            }
                            String mCat = jsonObject.getString("p_name");
                            String mTitle = jsonObject.getString("product_name");
                            String mprice = jsonObject.getString("str_prc");
                            String mimage = jsonObject.getString("product_image");
                            String product_id = jsonObject.getString("p_id");
                            prolist.add(new CatLvlItemList(mTitle, mprice, product_id, mimage, mCat));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (prolist.size() == 0) {
                    mProgressDialog.cancel();
                    Toast.makeText(SubCatActivity.this, "This store doesn't have any products.", Toast.LENGTH_SHORT).show();
                }

                tabTitles = new String[dummyList.size()];
                for (int i = 0; i < dummyList.size(); i++) {
                    tabTitles[i] = dummyList.get(i);
                }


                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), no_of_categories);
                mviewpager.setAdapter(adapter);
                mtabs.setupWithViewPager(mviewpager);

                mProgressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                Toast.makeText(SubCatActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        mrequestQueue = Volley.newRequestQueue(getApplicationContext());
        mrequestQueue.add(mrequest);


    }
}