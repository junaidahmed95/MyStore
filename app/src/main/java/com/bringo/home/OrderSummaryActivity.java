package com.bringo.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.AddressAdapter;
import com.bringo.home.Adapter.SummaryAdapter;
import com.bringo.home.Model.APIService;
import com.bringo.home.Model.AddressClass;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.Client;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.Data;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.MyResponce;
import com.bringo.home.Model.RequestHandlerSingleten;
import com.bringo.home.Model.Sender;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bringo.home.Adapter.AddressAdapter.cusaddress;
import static com.bringo.home.Adapter.AddressAdapter.cuslat;
import static com.bringo.home.Adapter.AddressAdapter.cuslng;
import static com.bringo.home.CartActivity.mTxtView_TotalPrice;
import static com.bringo.home.MessagingActivity.unreadListenr;
import static com.bringo.home.Verification.mylatlng;
import static com.bringo.home.ui.cart.CartFragment.mTxtView_Total;


public class OrderSummaryActivity extends AppCompatActivity implements OnMapReadyCallback {
    APIService apiService;
    DatePicker eReminderTime;
    private int uYear, uDOMo, uMOYear, uMinut, uHour;
    private DatePickerDialog datePickerDialog;
    private String timebase = null;
    private EditText mtimeofdeli, mtxtDate;
    private String self_pick = "0";
    private LinearLayout mtimeLayout;
    private final String JSON_URL = "https://bringo.biz/api/get/customer?u_id=" + FirebaseAuth.getInstance().getUid();
    private RecyclerView mAddressRecyclerView, msummaryRecyclerView;
    private static List<CatLvlItemList> orderSummaryList;
    private Button mconfirmorder_btn;
    private String custID;
    private FloatingActionButton maddAddre;
    private Toolbar toolbar;
    private String store_ID, ownerID, ownerImage, ownerName;
    private List<String> mycheckList;
    private String pTotalPrice;
    private HelpingMethods helpingMethods;
    private ProgressDialog mProgressDialog;
    private List<CatLvlItemList> preferenceList;
    private JsonArrayRequest addressrequest;
    private RequestQueue addressrequestQueue, requestQueue;
    private static AddressAdapter addressAdapter;
    String OrdrerID;
    private String status = "";
    String dayString;
    private final String Get_URL = "https://bringo.biz/api/get/address?user_id=" + FirebaseAuth.getInstance().getUid();
    List<AddressClass> kuchbhe;
    private JsonArrayRequest request;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    TextView mtotalprice;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;

    boolean flag = false;

    private DatabaseReference unreadReference, checkReference, ConversionRef;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    Task location;
    private ImageButton mTimeChoose, mopenDateDialog;
    static LatLng Your_Location = new LatLng(23.81, 90.41);
    public static String mAddress = "";
    static List<Address> addresses;
    private String Type = "Self";
    static Geocoder geocoder;
    private String addresss = "", city = "";
    private EditText muserName;
    private RadioButton mSelfBtn, mTimeBtn, mrunRedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order_summary);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Getting addresses...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        helpingMethods = new HelpingMethods(OrderSummaryActivity.this);
        ownerName = helpingMethods.GetStoreName();
        store_ID = helpingMethods.GetStoreID();

        GetCartData();
        GetCheckData();
        mopenDateDialog = findViewById(R.id.openDateDialog);
        mtxtDate = findViewById(R.id.txtDate);
        mtimeLayout = findViewById(R.id.timeLayout);
        mopenDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDia();
            }
        });
        pTotalPrice = getIntent().getStringExtra("totalP");

        mrunRedio = findViewById(R.id.runRedio);

        ownerImage = helpingMethods.GetStoreImage();
        ownerID = helpingMethods.GetStoreUID();
        mTimeChoose = findViewById(R.id.openTimeDialog);
        mSelfBtn = findViewById(R.id.selfRedio);
        mtimeofdeli = findViewById(R.id.timeofdeli);
        mTimeBtn = findViewById(R.id.timeRedio);

        mSelfBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mtimeLayout.setVisibility(View.VISIBLE);
                    self_pick = "1";
                }
            }
        });
        mrunRedio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mtxtDate.setText("");
                    mtimeofdeli.setText("");
                    mtimeLayout.setVisibility(View.GONE);
                    self_pick = "0";
                }
            }
        });
        mTimeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mtimeLayout.setVisibility(View.VISIBLE);
                    self_pick = "0";
                }
            }
        });

        mTimeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrderSummaryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mtimeofdeli.setText(selectedHour + ":" + selectedMinute);

                        uMinut = selectedMinute;
                        uHour = selectedHour;
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Deliver Time");
                mTimePicker.show();

            }
        });

        mconfirmorder_btn = findViewById(R.id.checkBtn);
        mtotalprice = findViewById(R.id.totalPrice);
        ConversionRef = FirebaseDatabase.getInstance().getReference().child("Chatlist");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayString = sdf.format(d);

        if (getIntent().getStringExtra("from").equals("activity")) {
            mtotalprice.setText("" + mTxtView_TotalPrice.getText());
        } else {
            mtotalprice.setText("" + mTxtView_Total.getText());
        }


        toolbar = findViewById(R.id.appBar);

        toolbar.setTitle("Checkout");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        orderSummaryList = new ArrayList<>();
        mAddressRecyclerView = findViewById(R.id.addressesRecyclerView);
        maddAddre = findViewById(R.id.addAddre);
        msummaryRecyclerView = findViewById(R.id.summaryRecyclerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Order....");
        progressDialog.setCancelable(false);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        mAddressRecyclerView.setLayoutManager(manager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        msummaryRecyclerView.setLayoutManager(layoutManager);


        kuchbhe = new ArrayList<>();
        parseJSON();
        parseAddressJSON();
        SummaryAdapter summaryAdapter = new SummaryAdapter(preferenceList, this);
        msummaryRecyclerView.setAdapter(summaryAdapter);
        maddAddre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermisssion(v);


            }
        });

        mconfirmorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mtimeLayout.getVisibility() == View.VISIBLE && mtimeofdeli.getText().toString().trim().equals("")) {
                    Toast.makeText(OrderSummaryActivity.this, "Please select delivery time", Toast.LENGTH_SHORT).show();
                } else if (mtimeLayout.getVisibility() == View.VISIBLE && mtxtDate.getText().toString().trim().equals("")) {
                    Toast.makeText(OrderSummaryActivity.this, "Please select delivery date", Toast.LENGTH_SHORT).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    progressDialog.show();

                                    Calendar c = Calendar.getInstance();
                                    c.set(Calendar.HOUR, uHour);
                                    c.set(Calendar.MINUTE, uMinut);
                                    c.set(Calendar.YEAR, uYear);
                                    c.set(Calendar.DAY_OF_MONTH, uDOMo);
                                    c.set(Calendar.SECOND, 0);
                                    c.set(Calendar.MILLISECOND, 0);
                                    timebase = String.valueOf(c.getTimeInMillis() / 1000L);

                                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    final String id = reference.child("Chats").push().getKey();
                                    String url = "https://bringo.biz/api/post/up_order";
                                    StringRequest postdata = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            OrdrerID = response;
                                            final HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("message", "new order");
                                            hashMap.put("time", ServerValue.TIMESTAMP);
                                            hashMap.put("seen", false);
                                            hashMap.put("type", "Order");
                                            hashMap.put("chatid", id);
                                            hashMap.put("orderID", OrdrerID);
                                            hashMap.put("storeID", store_ID);
                                            hashMap.put("address", cusaddress);
                                            hashMap.put("totalProduct", preferenceList.size());
                                            hashMap.put("totalPrice", "Rs." + pTotalPrice);
                                            hashMap.put("sender", FirebaseAuth.getInstance().getUid());
                                            hashMap.put("delivery", "not available");
                                            unreadReference = FirebaseDatabase.getInstance().getReference().child("Unread").child(FirebaseAuth.getInstance().getUid()).child(ownerID);
                                            reference.child("Chats").child(FirebaseAuth.getInstance().getUid() + ownerID).child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        final HashMap<String, Object> statusmap = new HashMap<>();
                                                        statusmap.put("time1", ServerValue.TIMESTAMP);
                                                        statusmap.put("status1","Pending");
                                                        FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getUid()).child(OrdrerID).setValue(statusmap);


                                                        helpingMethods.SaveCartCount(0, store_ID);
                                                        helpingMethods.SaveCartTotal("0", store_ID);
                                                        helpingMethods.SaveStoreData(null, null, null, null);
                                                        mycheckList.clear();
                                                        SaveCheckData();
                                                        preferenceList.clear();
                                                        SaveCartData();
                                                        UnreadMessage("\uD83D\uDCE6 New Order");
                                                        sendNotifiaction(ownerName, "\uD83D\uDCE6 New order has been arrived..");
                                                        Intent mintent = new Intent(OrderSummaryActivity.this, DummyOrderActivity.class);
                                                        //Toast.makeText(OrderSummaryActivity.this, "orderid"+OrdrerID, Toast.LENGTH_SHORT).show();
//                                                    mintent.putExtra("user_id", ownerID);
//                                                    mintent.putExtra("check", "one");
//                                                    mintent.putExtra("uName", ownerName);
//                                                    mintent.putExtra("uImage", ownerImage);
//                                                    mintent.putExtra("forward", "one");
//                                                    mintent.putExtra("for", "one");
                                                        progressDialog.cancel();
                                                        Toast.makeText(OrderSummaryActivity.this, "Order has been sent", Toast.LENGTH_SHORT).show();
                                                        startActivity(mintent);
                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(OrderSummaryActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.cancel();
                                            Toast.makeText(OrderSummaryActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }


                                    ) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Date date = new Date();
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                            String tim = dateFormat.format(date).toUpperCase();
                                            HashMap hashMap = new HashMap<>();
                                            hashMap.put("self_pick", self_pick);
                                            hashMap.put("pick_time", String.valueOf(timebase));
                                            for (int a = 0; a < preferenceList.size(); a++) {
                                                hashMap.put("str_prc[" + a + "]", preferenceList.get(a).getP_price());
                                                hashMap.put("ord_qty[" + a + "]", preferenceList.get(a).getP_quantity());
                                                hashMap.put("sp_id[" + a + "]", preferenceList.get(a).getProductid());
                                                hashMap.put("sp_image[" + a + "]", preferenceList.get(a).getP_img());
                                                hashMap.put("act_prc[" + a + "]", preferenceList.get(a).getActual_price());
                                                hashMap.put("str_id", store_ID);
                                                hashMap.put("sp_name[" + a + "]", preferenceList.get(a).getDesc());

                                                hashMap.put("user_id", FirebaseAuth.getInstance().getUid());
                                                hashMap.put("t_price", pTotalPrice);
                                                hashMap.put("time", tim);
                                                hashMap.put("day", dayString);
                                            }

                                            hashMap.put("new_address", cusaddress);
                                            hashMap.put("lat", cuslat);
                                            hashMap.put("lng", cuslng);
                                            return hashMap;
                                        };

                                        {

                                        }
                                    };


                                    postdata.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestHandlerSingleten.getInstance(getBaseContext()).addToRequestQueue(postdata);


                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummaryActivity.this);
                    builder.setMessage("Do you want to send order?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }


            }
        });


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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


        requestQueue = Volley.newRequestQueue(OrderSummaryActivity.this);
        requestQueue.add(request);
    }

    private void kamkicheez(final View v) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OrderSummaryActivity.this);
        if (ActivityCompat.checkSelfPermission(OrderSummaryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderSummaryActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    LayoutInflater li = LayoutInflater.from(OrderSummaryActivity.this);
                    View promptsView = li.inflate(R.layout.editaddress_dialog, null);
                    final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OrderSummaryActivity.this);
                    alertDialogBuilder.setView(promptsView);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    final ConnectionDetector detector = new ConnectionDetector(OrderSummaryActivity.this);
                    muserName = promptsView.findViewById(R.id.userAdd);
                    final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button mselectadd = promptsView.findViewById(R.id.selectadd);
                    Button mCancel = promptsView.findViewById(R.id.cancel);
                    Button mSave = promptsView.findViewById(R.id.save);

                    mselectadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OrderSummaryActivity.this, MapActivity.class);
                            flag = true;
                            intent.putExtra("activity", "order");
                            startActivity(intent);
                        }
                    });

                    mSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (detector.isConnected()) {
                                if (!muserName.getText().toString().trim().equals("")) {
                                    mProgressDialog.show();
                                    String url = "https://bringo.biz/api/post/address";
                                    StringRequest postdata = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            mProgressDialog.dismiss();
                                            alertDialog.cancel();

                                            Toast.makeText(OrderSummaryActivity.this, "Address is added", Toast.LENGTH_SHORT).show();

                                            Intent sumInt = new Intent(OrderSummaryActivity.this, OrderSummaryActivity.class);
                                            sumInt.putExtra("from", getIntent().getStringExtra("from"));

                                            if (getIntent().getStringExtra("from").equals("activity")) {
                                                sumInt.putExtra("totalP", mTxtView_TotalPrice.getText().toString());
                                            } else {
                                                sumInt.putExtra("totalP", mTxtView_Total.getText().toString());
                                            }


                                            sumInt.putExtra("storeid", store_ID);
                                            sumInt.putExtra("stname", ownerName);
                                            sumInt.putExtra("ownerID", ownerID);
                                            sumInt.putExtra("ownerImage", ownerImage);
                                            startActivity(sumInt);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            mProgressDialog.dismiss();
                                            Toast.makeText(OrderSummaryActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

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
                                // Rest of the stuff you need to do with the map

                            }
                        });


                    } catch (Exception e) {
                        Toast.makeText(OrderSummaryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public String getAddress(Double lat, Double lng) {

        geocoder = new Geocoder(this);
        try {
            addresss = null;
            addresses = geocoder.getFromLocation(lat, lng, 1);
            addresss = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return addresss + " " + city;

    }

    private void getPermisssion(final View v) {

        Dexter.withActivity(OrderSummaryActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        kamkicheez(v);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummaryActivity.this);
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

    private void UnreadMessage(String message) {
        Map concMap = new HashMap<>();
        concMap.put("last", message);
        concMap.put("time", ServerValue.TIMESTAMP);
        concMap.put("unread", 1);
        concMap.put("name", ownerName.toLowerCase());
        concMap.put("image", ownerImage);
        concMap.put("status", "sent");
        concMap.put("who", FirebaseAuth.getInstance().getUid());

        Map conc1Map = new HashMap<>();
        conc1Map.put("last", message);
        conc1Map.put("time", ServerValue.TIMESTAMP);
        conc1Map.put("name", ownerName.toLowerCase());
        conc1Map.put("image", ownerImage);
        conc1Map.put("unread", 0);
        conc1Map.put("status", "sent");
        conc1Map.put("who", FirebaseAuth.getInstance().getUid());

        Map message1TextDetail = new HashMap<>();
        message1TextDetail.put(FirebaseAuth.getInstance().getUid() + "/" + ownerID, conc1Map);
        message1TextDetail.put(ownerID + "/" + FirebaseAuth.getInstance().getUid(), concMap);

        ConversionRef.updateChildren(message1TextDetail, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (status.equals("") || status.equals("no")) {
                    unreadReference.child("unread").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            unreadReference.addValueEventListener(unreadListenr);

                        }
                    });
                }

            }
        });
    }

    private void sendNotifiaction(final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Users").child("Owners").child(ownerID);
        tokens.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Data data = new Data(FirebaseAuth.getInstance().getUid(), R.mipmap.ic_launcher, username + ": " + message, message,
                            ownerID);
                    Sender sender = new Sender(data, dataSnapshot.child("token").getValue().toString());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponce>() {
                                @Override
                                public void onResponse(Call<MyResponce> call, retrofit2.Response<MyResponce> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponce> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void parseAddressJSON() {
        addressrequest = new JsonArrayRequest(Get_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {


                    try {
                        jsonObject = response.getJSONObject(i);
                        if (i == 0) {
                            kuchbhe.add(new AddressClass(jsonObject.get("address").toString(), true,jsonObject.get("lat").toString(),jsonObject.get("lng").toString()));
                            cusaddress = jsonObject.get("address").toString();
                            cuslat=jsonObject.get("lat").toString();
                            cuslng=jsonObject.get("lng").toString();

                        } else {
                            kuchbhe.add(new AddressClass(jsonObject.get("address").toString(), false,jsonObject.get("lat").toString(),jsonObject.get("lng").toString()));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                addressAdapter = new AddressAdapter(kuchbhe, true);
                mAddressRecyclerView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
                mProgressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                Toast.makeText(OrderSummaryActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        addressrequestQueue = Volley.newRequestQueue(OrderSummaryActivity.this);
        addressrequestQueue.add(addressrequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


    public static void refresh(int deselect, int select) {
        addressAdapter.notifyItemChanged(deselect);
        addressAdapter.notifyItemChanged(select);
    }

    public void SaveCartData() {
        SharedPreferences sharedPreferences = getSharedPreferences(store_ID+""+ownerName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }

    private void SaveCheckData() {
        SharedPreferences sharedPreferences = getSharedPreferences(store_ID+"Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(store_ID+"Checkcart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("checklist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            mycheckList = gson.fromJson(json, type);

            if (mycheckList == null) {
                mycheckList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(OrderSummaryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(store_ID+""+ownerName, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }
        } catch (Exception e) {
            Toast.makeText(OrderSummaryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    protected void onResume() {
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
            mapFragment.getMapAsync(OrderSummaryActivity.this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void DateDia() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrderSummaryActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mtxtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        uYear = year;
                        uDOMo = dayOfMonth;
                        uMOYear = monthOfYear;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


}

