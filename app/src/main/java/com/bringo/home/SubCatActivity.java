package com.bringo.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.ShowStores;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bringo.home.BringoActivity.textCartItemCount;


public class SubCatActivity extends AppCompatActivity {


    private String mJSON_URL = "";
    private String[] tabTitles;
    private JsonArrayRequest mrequest;
    private List<String> dummyList;
    private RequestQueue mrequestQueue;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    int position;
    private Button mretryBtn;
    public static ProgressBar mloadingImage;
public static String checkSID;
    public static List<CatLvlItemList> list, real;
    private List<String> store;
    public static List<ShowStores> storelist;

    public static TabLayout mtabs;
    private ViewPager mviewpager;
    private Spinner msp_selectStore;

    public static HelpingMethods helpingMethods;
    private Boolean IsAdded = false;
    public static List<CatLvlItemList> prolist;
    public static FloatingActionButton mfbcart;
    int no_of_categories = -1;
    private String cat_Name, store_ID,ownerID,ownerImage,ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);

        mloadingImage = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new CubeGrid();
        mloadingImage.setIndeterminateDrawable(doubleBounce);
        mretryBtn = findViewById(R.id.retryBtn);


        cat_Name = getIntent().getStringExtra("catName");
        checkSID= getIntent().getStringExtra("storeid");
        store_ID = getIntent().getStringExtra("storeid");
        ownerName = getIntent().getStringExtra("stname");
        ownerImage = getIntent().getStringExtra("ownerImage");
        ownerID = getIntent().getStringExtra("ownerID");
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

        mtabs = findViewById(R.id.tabs);
        mviewpager = findViewById(R.id.viewpager);




        ConnectionDetector connectionDetector = new ConnectionDetector(SubCatActivity.this);
        if(connectionDetector.isConnected()){
            GetStoreData();
        }else {
            mloadingImage.setVisibility(View.GONE);
            mretryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(SubCatActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }

        mretryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(SubCatActivity.this);
                if(connectionDetector.isConnected()){
                    mloadingImage.setVisibility(View.VISIBLE);
                    mretryBtn.setVisibility(View.GONE);
                    GetStoreData();
                }else {
                    Toast.makeText(SubCatActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }
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
        if (helpingMethods.GetCartCount(checkSID) == 0) {
            if (textCartItemCount.getVisibility() != View.GONE) {
                textCartItemCount.setVisibility(View.GONE);
            }
        } else {
            textCartItemCount.setText("" + helpingMethods.GetCartCount(checkSID));
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
            if (helpingMethods.GetCartCount(checkSID) > 0) {
                Intent intent = new Intent(this, CartActivity.class);
                intent.putExtra("StID", store_ID);
                intent.putExtra("catName", cat_Name);
                intent.putExtra("stname",ownerName);
                intent.putExtra("ownerID",ownerID);
                intent.putExtra("ownerImage",ownerImage);
                startActivity(intent);
                finish();
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

                    fragment = new CatLvlFragment(store_ID,ownerID,ownerImage,ownerName);


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
        mJSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/stores/products?str_id=" + store_ID;
        mrequest = new JsonArrayRequest(mJSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dummyList.clear();
                prolist.clear();
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getString("m_name").equals(cat_Name)) {
                            if (!dummyList.contains(jsonObject.getString("p_name"))) {
                                dummyList.add(jsonObject.getString("p_name"));
                            }
                            String mCat = jsonObject.getString("p_name");
                            String str_id = jsonObject.getString("str_id");
                            String mTitle = jsonObject.getString("product_name");
                            String mprice = jsonObject.getString("str_prc");
                            String mimage = jsonObject.getString("product_image");
                            String product_id = jsonObject.getString("p_id");
                            String sim_id = jsonObject.getString("id");
                            prolist.add(new CatLvlItemList(mTitle, mprice,mimage,product_id,str_id,mCat,sim_id,mprice));

                        }

                    } catch (JSONException e) {
                        mloadingImage.setVisibility(View.GONE);
                        mretryBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SubCatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SubCatActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (prolist.size() == 0) {
                    mloadingImage.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(SubCatActivity.this, "This store doesn't have any products.", Toast.LENGTH_SHORT).show();
                }

                tabTitles = new String[dummyList.size()];
                for (int i = 0; i < dummyList.size(); i++) {
                    tabTitles[i] = dummyList.get(i);
                }


                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), no_of_categories);
                mviewpager.setAdapter(adapter);
                mtabs.setupWithViewPager(mviewpager);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mloadingImage.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.VISIBLE);
                Toast.makeText(SubCatActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SubCatActivity.this, "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
            }
        });


        mrequestQueue = Volley.newRequestQueue(getApplicationContext());
        mrequestQueue.add(mrequest);


    }
}