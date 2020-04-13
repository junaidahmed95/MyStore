package com.example.mystore.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.AllStoreAdapter;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Adapter.CategoryAdapter;
import com.example.mystore.Adapter.ProductAdapter;
import com.example.mystore.Adapter.SliderAdapter;
import com.example.mystore.AllStoresActivity;
import com.example.mystore.GirdListView;
import com.example.mystore.MainAdapter;
import com.example.mystore.Model.AllStore;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.Category;
import com.example.mystore.Model.Product;
import com.example.mystore.Model.ShowStores;
import com.example.mystore.R;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SliderView sliderView;
    private List<Category> productList;
   // ProgressBar mprogressbar;
    private RecyclerView categoryRecyclerView;
    private ScrollView mScrollView;

    public static String forWhat = "All";
    GridView gridView;
    List<GirdListView> list;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private CardView mcdv_dialog;



    private FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private GridView grd_str;
    private String test;
    private AllStoreAdapter allStoreAdapter;
    private List<ShowStores> storeList;
    private ProgressDialog mProgressDialog;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        grd_str = root.findViewById(R.id.gd1);
        //ye rha hai umair
        storeList = new ArrayList<>();
        CheckLocationPermission();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Getting stores");
        mProgressDialog.setCancelable(false);


        mcdv_dialog  = root.findViewById(R.id.cdv_dialog);
        sliderView = root.findViewById(R.id.imageSlider);
        categoryRecyclerView = root.findViewById(R.id.cat_recyclerView);
        //mprogressbar = root.findViewById(R.id.progressbar);



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






//        gridView = root.findViewById(R.id.gd1);
//        list = new ArrayList<>();
//
//        list.add(new GirdListView("2019 new autumn kids shoes","PKR 1,174.23","688 Sold",R.drawable.cocomo));
//
//
//        gridView.setAdapter(new MainAdapter(list,getActivity()));

        return root;
    }






    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
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
            Location mLastLocation = locationResult.getLastLocation();
            mProgressDialog.show();
            GetNearByStores(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };
//"https://chhatt.com/Cornstr/grocery/api/get/nearest/stores?latitude="+String.valueOf(latitude)+"&longitude="+String.valueOf(longitude)
    private void GetNearByStores(double latitude, double longitude) {
        request = new JsonArrayRequest("https://chhatt.com/Cornstr/grocery/api/get/nearest/stores?latitude=24.8183553&longitude=67.0740559", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if (response.isNull(0)) {
                    mcdv_dialog.setVisibility(View.VISIBLE);
                    mProgressDialog.cancel();

                }else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            String userID = jsonObject.getString("u_id");
                            String storename = jsonObject.getString("str_name");
                            String store_id = jsonObject.getString("id");
                            String distance = jsonObject.getString("distance");
                            String store_image = jsonObject.getString("user_thumb");
                            //String store_name, String id, String uid, String store_image
                            storeList.add(new ShowStores(storename,store_id, userID, store_image,distance));

                        } catch (JSONException e) {
                            mProgressDialog.cancel();
                            //Toast.makeText(AllStoresActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    allStoreAdapter = new AllStoreAdapter(storeList, getActivity());
                    grd_str.setAdapter(allStoreAdapter);
                    allStoreAdapter.notifyDataSetChanged();
                    mProgressDialog.cancel();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
                if (isLocationEnabled()) {
                   mFusedLocationClient.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    Location location = task.getResult();

                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
                                        mProgressDialog.show();
                                        GetNearByStores(location.getLatitude(), location.getLongitude());

                                    }
                                }
                            }
                    );
                }

            else {
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
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getLastLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                            Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


}


