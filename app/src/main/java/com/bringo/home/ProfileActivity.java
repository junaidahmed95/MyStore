package com.bringo.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Model.AppHelper;
import com.bringo.home.Model.VolleyMultipartRequest;
import com.bringo.home.Model.VolleySingleton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bringo.home.Adapter.AddressAdapter;
import com.bringo.home.Model.AddressClass;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.RequestHandlerSingleten;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bringo.home.MapActivity.myaddress;
import static com.bringo.home.Verification.mylatlng;


public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FloatingActionButton maddAddre;
    private AddressAdapter addressAdapter;
    private RecyclerView mAddressRecyclerView;
    static List<Address> addresses;
    private Toolbar toolbar;
    private Bitmap bitmap = null;
    private CircleImageView mImage;
    private Uri imageUri;
    static LatLng Your_Location = new LatLng(23.81, 90.41);
    private String mU_id;
    public static String mAddress = "";
    private BottomSheetDialog editProfileDialog;
    private List<AddressClass> addresslist;
    private RelativeLayout mupdateProfile;
    private String custID;
    private TextView mName, mPhone, mEmail, muser_Name, mlabelEmail, muserEmail;
    public static boolean check = true;
    private FloatingActionButton mAddPicFab, meditFab;
    private HelpingMethods helpingMethods;
    private static final int GALLERY_CODE = 101;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    Task location;
    boolean flag = false;
    private ProgressDialog mProgressDialog;
    private double latitude;
    private double longitude;



    static Geocoder geocoder;
    private String address = "", city = "";
    private EditText meditaddress;
    private JsonArrayRequest request, addressrequest;
    private RequestQueue requestQueue, addressrequestQueue;
    private final String JSON_URL = " https://bringo.biz/backend/api/get/customer?u_id=" + FirebaseAuth.getInstance().getUid();
    private final String Get_URL = " https://bringo.biz/backend/api/get/address?user_id=" + FirebaseAuth.getInstance().getUid();
    //private final String Get_URL = " @Junaid Ahmed https://bringo.biz/api/edit/customer/profile?id=4080&user_name=check" + FirebaseAuth.getInstance().getUid();

    private EditText muserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        toolbar = findViewById(R.id.appBar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mU_id = FirebaseAuth.getInstance().getUid();
        addresslist = new ArrayList<>();
        mAddressRecyclerView = findViewById(R.id.addressesRecyclerView);
        mImage = findViewById(R.id.userProfile);
        helpingMethods = new HelpingMethods(ProfileActivity.this);

            Glide.with(getApplicationContext()).asBitmap().load(helpingMethods.GetUImage()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mImage);


        mName = findViewById(R.id.userName);
        muser_Name = findViewById(R.id.user_Name);
        mPhone = findViewById(R.id.userPhone);
        mlabelEmail = findViewById(R.id.labelEmail);
        muserEmail = findViewById(R.id.userEmail);

        if (helpingMethods.GetUEmail() != null) {
            if (!helpingMethods.GetUEmail().equals("")) {
                if (!helpingMethods.GetUEmail().equals("null")) {
                    mlabelEmail.setVisibility(View.VISIBLE);
                    muserEmail.setText(helpingMethods.GetUEmail());
                    muserEmail.setVisibility(View.VISIBLE);
                }
            }
        }

        mAddPicFab = findViewById(R.id.addPic);
        mupdateProfile = findViewById(R.id.updateProfile);
        meditFab = findViewById(R.id.editFab);
        maddAddre = findViewById(R.id.addAddre);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mAddressRecyclerView.setLayoutManager(linearLayoutManager);

        meditaddress = findViewById(R.id.userAdd);
        mName.setText(helpingMethods.GetUName());
        mPhone.setText(helpingMethods.GetUPhone());
        muser_Name.setText(helpingMethods.GetUName());
        parseJSON();


        maddAddre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddessDialog(v);


            }
        });


        mupdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileSheet();
            }
        });

        meditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialog();
            }
        });


        parseAddressJSON();

    }

    private void AddAddessDialog(final View v) {
        getPermisssion(v);

    }


    private void EditNameDialog() {
        LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
        View promptsView = li.inflate(R.layout.editname_dialog, null);

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
        alertDialogBuilder.setView(promptsView);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        final ConnectionDetector detector = new ConnectionDetector(ProfileActivity.this);
        final EditText muserName = promptsView.findViewById(R.id.userName);
        muserName.setText(helpingMethods.GetUName().toUpperCase());
        Button mCancel = promptsView.findViewById(R.id.cancel);
        Button mSave = promptsView.findViewById(R.id.save);


        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detector.isConnected()) {
                    if (!muserName.getText().toString().trim().equals("")) {
                        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mU_id).child("name").setValue(muserName.getText().toString());
                        helpingMethods.saveuser(muserName.getText().toString(), helpingMethods.GetUImage(), helpingMethods.GetUPhone(), helpingMethods.GetUEmail());
                        alertDialog.cancel();
                    } else {
                        helpingMethods.SnackBar("Enter your name.", view);
                    }

                } else {
                    helpingMethods.SnackBar("Check your internet connection.", view);
                }
            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void ProfileSheet() {
        View mView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.updateprofile_bottomsheet, null);
        final LinearLayout mviewProfile, mmoreLayout;
        RelativeLayout mupdProfileLayour, mcameraLayout, mgalleryLayout;
        FloatingActionButton closeButton = mView.findViewById(R.id.closeFab);
        final ImageView mupdoImage = mView.findViewById(R.id.updoImage);
        mviewProfile = mView.findViewById(R.id.viewProfile);
        mupdProfileLayour = mView.findViewById(R.id.updProfileLayout);
        mmoreLayout = mView.findViewById(R.id.moreLayout);
        mcameraLayout = mView.findViewById(R.id.cameraLayout);
        mgalleryLayout = mView.findViewById(R.id.galleryLayout);

        mupdProfileLayour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmoreLayout.getVisibility() == View.GONE) {
                    mupdoImage.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    mmoreLayout.setVisibility(View.VISIBLE);

                } else {
                    mupdoImage.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    mmoreLayout.setVisibility(View.GONE);
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileDialog.dismiss();
            }
        });

        mviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, videoView.class);
                intent.putExtra("image", helpingMethods.GetUImage());
                startActivity(intent);
                editProfileDialog.dismiss();
            }
        });

        mgalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector detector = new ConnectionDetector(ProfileActivity.this);
                if (detector.isConnected()) {
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        GalleryIntent();
                    } else {
                        GalleryPermission();
                    }
                } else {
                    helpingMethods.SnackBar("Check your internet connection.", v);
                }

            }
        });

        editProfileDialog = new BottomSheetDialog(mView.getContext());
        editProfileDialog.setContentView(mView);
        editProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editProfileDialog.show();
    }


    private void parseJSON() {
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {


                    try {
                        jsonObject = response.getJSONObject(i);

                        custID = jsonObject.get("id").toString();


                        mProgressDialog.cancel();
                    } catch (JSONException e) {
                        mProgressDialog.cancel();
                        e.printStackTrace();
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
            }
        });


        requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(request);
    }

    private void parseAddressJSON() {
        addressrequest = new JsonArrayRequest(Get_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {


                    try {
                        jsonObject = response.getJSONObject(i);
                        addresslist.add(new AddressClass(jsonObject.get("address").toString(), false,jsonObject.get("lat").toString(),jsonObject.get("lng").toString()));
                    } catch (JSONException e) {
                        mProgressDialog.cancel();
                        e.printStackTrace();
                    }


                }
                addressAdapter = new AddressAdapter(addresslist, false);
                mAddressRecyclerView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
                mProgressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                Toast.makeText(ProfileActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addressrequestQueue = Volley.newRequestQueue(ProfileActivity.this);
        addressrequestQueue.add(addressrequest);
    }


    private void GalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_CODE);
        editProfileDialog.dismiss();

    }

    private void GalleryPermission() {
        Dexter.withActivity(ProfileActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        GalleryIntent();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Deny", null)
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            helpingMethods.SnackBar("Permission Denied", mImage);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Glide.with(getApplicationContext()).load(String.valueOf(imageUri)).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mImage);
            helpingMethods.saveuser(helpingMethods.GetUName(), String.valueOf(imageUri), helpingMethods.GetUPhone(), helpingMethods.GetUEmail());
//            mProgressDialog.show();
//            String url = "https://bringo.biz/api/edit/customer/profile?id=" + custID;
//            VolleyMultipartRequest multipartRequest = new
//                    VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
//                        @Override
//                        public void onResponse(NetworkResponse response) {
//                            if (response.statusCode == 200) {
//                                String userImage = null;
//                                try {
//                                    userImage = new String(response.data, "UTF-8");
//                                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid());
//                                    userReference.child("picture").setValue(userImage);
//
//                                    mProgressDialog.cancel();
//                                    Toast.makeText(ProfileActivity.this, "Profile picture is updated.", Toast.LENGTH_SHORT).show();
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//
//                            } else {
//                                mProgressDialog.cancel();
//                                Toast.makeText(ProfileActivity.this, "Error founded: " + response.statusCode, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            NetworkResponse networkResponse = error.networkResponse;
//                            String errorMessage = "Unknown error";
//                            if (networkResponse == null) {
//                                if (error.getClass().equals(TimeoutError.class)) {
//                                    errorMessage = "Request timeout";
//                                } else if (error.getClass().equals(NoConnectionError.class)) {
//                                    errorMessage = "Failed to connect server";
//                                }
//                                mProgressDialog.cancel();
//                                Toast.makeText(ProfileActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
//                            } else {
//                                String result = new String(networkResponse.data);
//                                try {
//                                    JSONObject response = new JSONObject(result);
//                                    String status = response.getString("status");
//                                    String message = response.getString("message");
//
//                                    Log.e("Error Status", status);
//                                    Log.e("Error Message", message);
//
//                                    if (networkResponse.statusCode == 404) {
//                                        errorMessage = "Resource not found";
//                                    } else if (networkResponse.statusCode == 401) {
//                                        errorMessage = message + " Please login again";
//                                    } else if (networkResponse.statusCode == 400) {
//                                        errorMessage = message + " Check your inputs";
//                                    } else if (networkResponse.statusCode == 500) {
//                                        errorMessage = message + " Something is getting wrong";
//                                    }
//
//                                    mProgressDialog.cancel();
//                                    Toast.makeText(ProfileActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            Log.i("Error", errorMessage);
//                            error.printStackTrace();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, DataPart> getByteData() {
//                            Map<String, DataPart> params = new HashMap<>();
//                            params.put("thumbnail [" + 0 + "]", new DataPart("profileimage.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), bitmap), "image/jpeg"));
//
//
//                            return params;
//                        }
//
//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("user_name", muser_Name.getText().toString());
//
//                            return hashMap;
//                        }
//                    };
//            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);


        }
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


        return address + " " + city;

    }


    @Override
    public void onBackPressed() {
        check = true;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void kamkicheez(final View v) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ProfileActivity.this);
        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {


                    LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
                    View promptsView = li.inflate(R.layout.editaddress_dialog, null);
                    final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
                    alertDialogBuilder.setView(promptsView);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    final ConnectionDetector detector = new ConnectionDetector(ProfileActivity.this);
                    muserName = promptsView.findViewById(R.id.userAdd);
                    final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button mselectadd = promptsView.findViewById(R.id.selectadd);
                    Button mCancel = promptsView.findViewById(R.id.cancel);
                    Button mSave = promptsView.findViewById(R.id.save);

                    mselectadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                            flag = true;
                            intent.putExtra("activity", "profile");
                            startActivity(intent);
                        }
                    });

                    mSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (detector.isConnected()) {
                                if (!muserName.getText().toString().trim().equals("")) {
                                    mProgressDialog.show();
                                    String url = "https://bringo.biz/backend/api/post/address";
                                    StringRequest postdata = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            mProgressDialog.dismiss();
                                            alertDialog.cancel();

                                            Toast.makeText(ProfileActivity.this, "Address is added", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            mProgressDialog.dismiss();
                                            Toast.makeText(ProfileActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }


                                    }


                                    ) {
                                        @Override
                                        protected Map<String, String> getParams() {

                                            HashMap<String, String> hashMap = new HashMap<>();

                                            hashMap.put("address", muserName.getText().toString().trim());
                                            hashMap.put("cs_id", custID);
                                            hashMap.put("user_id", FirebaseAuth.getInstance().getUid());
                                            hashMap.put("lat", String.valueOf(latitude));
                                            hashMap.put("lng", String.valueOf(longitude));



                                            return hashMap;
                                        }


                                        {

                                        }
                                    };


                                    postdata.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestHandlerSingleten.getInstance(getBaseContext()).addToRequestQueue(postdata);

                                } else {
                                    helpingMethods.SnackBar("Enter your Address.", v);
                                }

                            } else {
                                helpingMethods.SnackBar("Check your internet connection.", v);
                            }


                        }

                    });


                    final android.location.Location currentLocation = (Location) task.getResult();
                    try {
                        Your_Location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                        mAddress = getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
                        ((SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapview)).getMapAsync(new OnMapReadyCallback() {

                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();
                                Your_Location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mMap = googleMap;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 15));  //move camera to location
                                mAddress = getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
                                muserName.setText(mAddress);
                                if (mMap != null) {
                                    Marker hamburg = mMap.addMarker(new MarkerOptions().position(Your_Location));
                                }
                            }
                            // Rest of the stuff you need to do with the map

                        });

                    } catch (Exception e) {
                        Log.d("dfg", e.getMessage());

                    }


//                    mSave.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (detector.isConnected()) {
//                                if (!muserName.getText().toString().trim().equals("")) {
//                                    alertDialog.cancel();
//                                } else {
//                                    helpingMethods.SnackBar("Enter your Address.", v);
//                                }
//
//                            } else {
//                                helpingMethods.SnackBar("Check your internet connection.", view);
//                            }
//                        }
//                    });


                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });

                    alertDialog.show();

                }
            }
        });
    }


    private void getPermisssion(final View v) {

        Dexter.withActivity(ProfileActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        kamkicheez(v);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Deny", null)
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            helpingMethods.SnackBar("Permission Denied", maddAddre);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }
        if (flag) {
            String[] getvv = mylatlng.split(",");
             latitude = Double.parseDouble(getvv[0]);
             longitude = Double.parseDouble(getvv[1]);
            Your_Location = new LatLng(latitude, longitude);
            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapview)).getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mMap = googleMap;
                    mMap = googleMap;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 15));  //move camera to location
                    mAddress = getAddress(latitude, longitude);
                    muserName.setText(mAddress);
                    if (mMap != null) {
                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(Your_Location));
                    }
                    // Rest of the stuff you need to do with the map
                }
            });
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(ProfileActivity.this);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
