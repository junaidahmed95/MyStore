package com.bringo.home.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.CategoryAdapter;
import com.bringo.home.Adapter.MainCategoryAdapter;
import com.bringo.home.Adapter.SliderAdapter;
import com.bringo.home.Adapter.StoresAdapter;
import com.bringo.home.GirdListView;
import com.bringo.home.MainCatActivity;
import com.bringo.home.Model.Categories;
import com.bringo.home.Model.Category;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.Fruit;
import com.bringo.home.Model.Listmmh;
import com.bringo.home.Model.RetrofitClient;
import com.bringo.home.Model.ShowStores;
import com.bringo.home.Model.sample;
import com.bringo.home.R;
import com.bringo.home.ViewAllStoresActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {


    ArrayList<Categories> maincat;
    ArrayList<Fruit> fruit ;
    ArrayList<Listmmh> mmh;
    ArrayList<Fruit> veg;


    private SliderView sliderView;
    private List<Category> productList;
    // ProgressBar mprogressbar;
    private RecyclerView categoryRecyclerView,mfruitrecyclerView,vegrecyclerView;
    private ScrollView mScrollView;
    private Location mLastLocation;

    public static String forWhat = "All";
    private List<Category> catList;
    List<GirdListView> list;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private CardView mcdv_dialog;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    int PERMISSION_ID = 44;
    private RecyclerView grd_str,hmmCatrecyclerView;
    private String test;
    private ProgressBar mloadingImage;
    private StoresAdapter allStoreAdapter;
    private RecyclerView mmainCatrecyclerView;
    public static List<ShowStores> nearesStoresList;
    private Button mretryBtn, mBtnViewAll;
    private SwipeRefreshLayout pullToRefresh;
    private TextView mcategory_name;
    private ImageView mcategory_image;
    private LinearLayout cat_layout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        pullToRefresh = root.findViewById(R.id.pullToRefresh);
        mloadingImage = root.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new CubeGrid();
        mloadingImage.setIndeterminateDrawable(doubleBounce);
        mretryBtn = root.findViewById(R.id.retryBtn);
        cat_layout = root.findViewById(R.id.cat_layout);
        mcategory_name = root.findViewById(R.id.category_name);
        mcategory_image = root.findViewById(R.id.category_image);
        mBtnViewAll = root.findViewById(R.id.btnViewAll);
        grd_str = root.findViewById(R.id.gd1);
       hmmCatrecyclerView = root.findViewById(R.id. hmmCatrecyclerView);
        mmainCatrecyclerView = root.findViewById(R.id.mainCatrecyclerView);
        mfruitrecyclerView = root.findViewById(R.id.fruitrecyclerView);
        vegrecyclerView= root.findViewById(R.id.vegrecyclerView);


        catList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mmainCatrecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManage = new LinearLayoutManager(getActivity());
        layoutManage.setOrientation(RecyclerView.HORIZONTAL);
        hmmCatrecyclerView.setLayoutManager(layoutManage);

        LinearLayoutManager layoutManag = new LinearLayoutManager(getActivity());
        layoutManag.setOrientation(RecyclerView.HORIZONTAL);
        mfruitrecyclerView.setLayoutManager(layoutManag);

        LinearLayoutManager layoutMana = new LinearLayoutManager(getActivity());
        layoutMana.setOrientation(RecyclerView.HORIZONTAL);
        vegrecyclerView.setLayoutManager(layoutMana);

        //GetCategories();
        GetCat();
        //Gethmm();
        grd_str.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        nearesStoresList = new ArrayList<>();





        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetNearByStores(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                pullToRefresh.setRefreshing(false);
            }
        });

        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if (connectionDetector.isConnected()) {
            mloadingImage.setVisibility(View.VISIBLE);
            mretryBtn.setVisibility(View.GONE);
            CheckLocationPermission();
        } else {
            Toast.makeText(getActivity(), "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }

        mretryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
                if (connectionDetector.isConnected()) {
                    mloadingImage.setVisibility(View.VISIBLE);
                    mcdv_dialog.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.GONE);
                    CheckLocationPermission();
                } else {
                    Toast.makeText(getActivity(), "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        mcdv_dialog = root.findViewById(R.id.cdv_dialog);
        sliderView = root.findViewById(R.id.imageSlider);
        categoryRecyclerView = root.findViewById(R.id.gd1);
        GetNearestStores();


        final SliderAdapter adapter = new SliderAdapter(getActivity());
        adapter.setCount(6);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        mBtnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allStoreIntent = new Intent(getActivity(), ViewAllStoresActivity.class);
                getActivity().startActivity(allStoreIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });






        return root;
    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void GetNearestStores() {
        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if (connectionDetector.isConnected()) {
            CheckLocationPermission();
        } else {
            mloadingImage.setVisibility(View.GONE);
            mretryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Check your inetrnet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER

        );
    }

    @Override
    public void onResume() {
        super.onResume();
        mretryBtn.setVisibility(View.GONE);
        CheckLocationPermission();


        //streamLocation();
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLastLocation = locationResult.getLastLocation();
            GetNearByStores(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    //https://bringo.biz/api/get/nearest/stores?latitude=24.8147631&longitude=67.0698717
    //String url = "https://bringo.biz/api/get/nearest/stores?latitude=24.8147631&longitude=67.0698717";
    //"https://bringo.biz/api/get/nearest/stores?latitude="+String.valueOf(latitude)+"&longitude="+String.valueOf(longitude)

    private void GetNearByStores(final double latitude, final double longitude) {
        request = new JsonArrayRequest("https://bringo.biz/api/get/nearest/stores?page=1&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude), new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.isNull(0)) {
                    mcdv_dialog.setVisibility(View.VISIBLE);
                    mloadingImage.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.VISIBLE);
                } else {
                    Log.d("getlatlng", latitude + " " + longitude);
                    nearesStoresList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            String userID = jsonObject.getString("u_id");
                            String storename = jsonObject.getString("str_name");
                            String storeaddr = jsonObject.getString("address");
                            String store_id = jsonObject.getString("id");
                            double distance1 = Double.parseDouble(jsonObject.getString("distance"));
                            String store_image = jsonObject.getString("thumbnail");
                            NumberFormat formatter = new DecimalFormat("#0.00");
                            String distance = formatter.format(distance1);
                            Log.d("distance", formatter.format(distance1) + " " + storename);
                            nearesStoresList.add(new ShowStores(storename, store_id, userID, store_image, distance, storeaddr));
                        } catch (JSONException e) {
                            grd_str.setVisibility(View.GONE);
                            mloadingImage.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            mretryBtn.setVisibility(View.VISIBLE);
                        }
                    }
                    allStoreAdapter = new StoresAdapter(nearesStoresList, getActivity(), false);
                    grd_str.setAdapter(allStoreAdapter);
                    mcdv_dialog.setVisibility(View.GONE);
                    mretryBtn.setVisibility(View.GONE);
                    grd_str.setVisibility(View.VISIBLE);
                    allStoreAdapter.notifyDataSetChanged();
                    mloadingImage.setVisibility(View.GONE);
                    // mBtnViewAll.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                grd_str.setVisibility(View.GONE);
                mloadingImage.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    @SuppressLint("MissingPermission")
    private void streamLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getActivity(), "Location Changed" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                grd_str.setVisibility(View.GONE);
                mcdv_dialog.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.GONE);
                mloadingImage.setVisibility(View.VISIBLE);
                mLastLocation = location;
                GetNearByStores(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();

                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    mLastLocation = location;
                                    GetNearByStores(location.getLatitude(), location.getLongitude());

                                }
                            }
                        }
                );


            } else {
                Toast.makeText(getContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private void CheckLocationPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        getLastLocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            mloadingImage.setVisibility(View.GONE);
                            mretryBtn.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            mloadingImage.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                            mretryBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


//    //private void GetCategories() {
//        request = new JsonArrayRequest("https://bringo.biz/api/maincat", new Response.Listener<JSONArray>() {
//
//
//            @Override
//            public void onResponse(JSONArray response) {
//
//                JSONObject jsonObject = null;
//                if (response.isNull(0)) {
//                    mcdv_dialog.setVisibility(View.VISIBLE);
//                    mloadingImage.setVisibility(View.GONE);
//                    mretryBtn.setVisibility(View.VISIBLE);
//
//                } else {
//                    //catList.clear();
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            jsonObject = response.getJSONObject(i);
//
//                            String image = jsonObject.getString("thumbnail");
//                            String text = jsonObject.getString("m_name");
//                            String cat_id = jsonObject.getString("id");
//                            catList.add(new Category(image, text,cat_id));
//
//
//
//
//                        } catch (JSONException e) {
//                            grd_str.setVisibility(View.GONE);
//                            mloadingImage.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            mretryBtn.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    Glide.with(getActivity()).asBitmap().load(catList.get(6).getCatImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mcategory_image);
//                    mcategory_name.setText(""+catList.get(6).getCatName());
//                    MainCategoryAdapter allStoreAdapter = new MainCategoryAdapter(catList, getActivity());
//                    mmainCatrecyclerView.setAdapter(allStoreAdapter);
//                    hmmCatrecyclerView.setAdapter(allStoreAdapter);
//                    mmainCatrecyclerView.setVisibility(View.VISIBLE);
//                    allStoreAdapter.notifyDataSetChanged();
//                    mloadingImage.setVisibility(View.GONE);
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                grd_str.setVisibility(View.GONE);
//                mloadingImage.setVisibility(View.GONE);
//                mretryBtn.setVisibility(View.VISIBLE);
//                if (getActivity() != null) {
//                    Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//
//        requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(request);
//
//    }
    private void GetCat(){
        Call<sample> done = RetrofitClient.getmInstance().getApi()
                .editprice();
        done.enqueue(new Callback<sample>() {
            @Override
            public void onResponse(Call<sample> call, retrofit2.Response<sample> response) {

            maincat = response.body().getCategories();
            fruit = response.body().getFruits();
            mmh = response.body().getMmh();
                veg = response.body().getVegetables();

                MainCategoryAdapter allStoreAdapter = new MainCategoryAdapter(maincat,mmh,fruit,veg, getActivity(),"1");
                mmainCatrecyclerView.setAdapter(allStoreAdapter);
                MainCategoryAdapter allStoreAdapter1 = new MainCategoryAdapter(maincat,mmh,fruit,veg, getActivity(),"2");
                hmmCatrecyclerView.setAdapter(allStoreAdapter1);
                MainCategoryAdapter allStoreAdapter2 = new MainCategoryAdapter(maincat,mmh,fruit,veg, getActivity(),"3");
                mfruitrecyclerView.setAdapter(allStoreAdapter2);
                MainCategoryAdapter allStoreAdapter3 = new MainCategoryAdapter(maincat,mmh,fruit,veg, getActivity(),"4");
                vegrecyclerView.setAdapter(allStoreAdapter3);
                mmainCatrecyclerView.setVisibility(View.VISIBLE);
                allStoreAdapter.notifyDataSetChanged();
                mloadingImage.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<sample> call, Throwable t) {
                grd_str.setVisibility(View.GONE);
                mloadingImage.setVisibility(View.GONE);
                mretryBtn.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "no" , Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT).show();

            }
        });



//        Call<List<sample>> call = RetrofitClient.getmInstance().
//                getApi().editprice();
//        call.enqueue(new Callback<List<sample>>() {
//            @Override
//            public void onResponse(Call<sample> call, retrofit2.Response<sample> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), ""+response.body().toString(), Toast.LENGTH_SHORT).show();
//                    ArrayList<Categories> maincat = response.body().getCategories();
//                    ArrayList<Fruit> fruit = response.body().getFruits();
//                    ArrayList<Listmmh> mmh = response.body().getMmh();
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<sample> call, Throwable t) {
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}


