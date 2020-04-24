package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore.Adapter.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.Verification.mylatlng;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener {

    public static String gettextforlocation;
    static ImageView search;
    static int flag = 1;
    private static final String TAG = "MapActivity";
    ImageView image;
    Intent intent1;
    private static LatLng latLng;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FloatingActionButton submitbtn;
    private android.location.Location lastLocation;
    String cityLat, cityLong;
    private Marker UserCurrentLocation;
    public static String myaddress = "";
    private static final int Request_User_Location_Code = 99;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 30f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    Intent intent;
    private String Location;
    static String address = "";
    static String city = "";
    static Geocoder geocoder;
    static List<Address> addresses;


    //widgets
    private AutoCompleteTextView mSearchText;
    private FloatingActionButton mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
//


        //mMap.setOnCameraIdleListener(onCameraIdleListener);


       /* mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                LatLng midLatLng = mMap.getCameraPosition().target;
            }
        });*/


        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);




        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }*/

            init();

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent1 = getIntent();
        Location = intent1.getStringExtra("location");
        image = findViewById(R.id.imageView);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);
        submitbtn = findViewById(R.id.submitmap);

        cityLat = getIntent().getStringExtra("lat");
        cityLong = getIntent().getStringExtra("long");

        try {
            String get = getIntent().getStringExtra("search");
            if (!get.equals("")) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) submitbtn.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
        } catch (Exception e) {
        }
        getLocationPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkeUserLocationPermission();
        }

        search = findViewById(R.id.Search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(mSearchText.getText().toString());
                Toast.makeText(MapActivity.this, "" + mSearchText.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gettextforlocation = mSearchText.getText().toString();
                Toast.makeText(MapActivity.this, "" + mSearchText.getText().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mylatlng.equals("")) {
                    Toast.makeText(MapActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }


            }
        });

    }

    private void init() {


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    public boolean checkeUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    private void geoLocate() {


        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {

        }

        if (list.size() > 0) {
            Address address = list.get(0);


            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Location currentLocation = (Location) task.getResult();

                            try {
                                if (!mylatlng.equals("")) {
                                    String[] getvv = mylatlng.split(",");
                                    double latitude = Double.parseDouble(getvv[0]);
                                    double longitude = Double.parseDouble(getvv[1]);
                                    moveCamera(new LatLng(latitude, longitude),
                                            DEFAULT_ZOOM,
                                            "My Location");
                                } else {
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                            DEFAULT_ZOOM,
                                            "My Location");
                                }
                            } catch (Exception e) {
                                checkGPSLOC();
                            }


                        } else {

                            checkGPSLOC();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void checkGPSLOC() {
        LocationManager lm = (LocationManager) MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


            alertDialogBuilder
                    .setMessage("Enable GPS Location..")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {

        mylatlng = latLng.latitude + "," + latLng.longitude;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);

        }

        hideSoftKeyboard();
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

    }

    private void getLocationPermission() {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;

                            return;
                        }
                    }

                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onLocationChanged(final Location location) {


        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        lastLocation = location;
        latLng = new LatLng(location.getLatitude(), location.getLongitude());


        if (UserCurrentLocation != null) {
            UserCurrentLocation.remove();
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String check = getAddress(latLng.latitude, latLng.longitude);


            if (UserCurrentLocation == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                UserCurrentLocation = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            } else {

                UserCurrentLocation.setPosition(latLng);
            }


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng centre = mMap.getCameraPosition().target;
                    if (UserCurrentLocation != null) {
                        UserCurrentLocation.remove();
                        mMap.clear();
                        //mMap.addMarker(new MarkerOptions().position(centre).title(a));
                        latLng = UserCurrentLocation.getPosition();

                        Verification.mAddress = getAddress(centre.latitude,centre.longitude);

                        myaddress = getAddress(centre.latitude, centre.longitude);
                        mylatlng = centre.latitude + "," + centre.longitude;


                    }
                }
            });
        }



      /*  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        //mMap.addMarker(new MarkerOptions().position(latLng).title("Dragable").draggable(true));
        UserCurrentLocation = mMap.addMarker(new MarkerOptions().position(latLng).title("Address").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15f));
*/
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationListener) this);
        }


        //getAddress(latLng.latitude,latLng.longitude);
    }


    public String getAddress(Double lat, Double lng) {

        geocoder = new Geocoder(this);
        try {
            address = null;
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //button.setText(address + "" + city);


        return address + " " + city;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void onSearch(String a) {
        //AutoCompleteTextView SV = (AutoCompleteTextView) findViewById(R.id.input_search);
        String location = a;
        List<Address> addressList = null;

        if (location != null && !location.equals("")) {

            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());

            //UserCurrentLocation.remove();
            mMap.clear();
            //mMap.addMarker(new MarkerOptions().position(latLng).title(address.getSubLocality()).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            myaddress = getAddress(latLng.latitude, latLng.longitude);
            hideSoftKeyboard();
        } else {
            Toast.makeText(MapActivity.this, "Slect place", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}

