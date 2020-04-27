package com.bringo.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.bringo.home.Model.Data;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.MyResponce;
import com.bringo.home.Model.RequestHandlerSingleten;
import com.bringo.home.Model.Sender;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bringo.home.Adapter.AddressAdapter.cusaddress;
import static com.bringo.home.CartActivity.mTxtView_TotalPrice;
import static com.bringo.home.MessagingActivity.unreadListenr;
import static com.bringo.home.ui.cart.CartFragment.mTxtView_Total;


public class OrderSummaryActivity extends AppCompatActivity {
    APIService apiService;
    private RecyclerView mAddressRecyclerView, msummaryRecyclerView;
    private static List<CatLvlItemList> orderSummaryList;
    private Button mconfirmorder_btn;
    private FloatingActionButton maddAddre;
    private Toolbar toolbar;
    private String store_ID, ownerID, ownerImage, ownerName;
    private List<String> mycheckList;
    private String pTotalPrice;
    private HelpingMethods helpingMethods;
    private ProgressDialog mProgressDialog;
    private List<CatLvlItemList> preferenceList;
    private JsonArrayRequest addressrequest;
    private RequestQueue addressrequestQueue;
    private static AddressAdapter addressAdapter;
    private String address;
    String OrdrerID;
    private String status = "";
    String dayString;
    private final String Get_URL = " https://chhatt.com/Cornstr/grocery/api/get/address?user_id=" + FirebaseAuth.getInstance().getUid();
    List<AddressClass> kuchbhe;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    TextView mtotalprice;

    private DatabaseReference unreadReference, checkReference, ConversionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Getting addresses...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        helpingMethods = new HelpingMethods(OrderSummaryActivity.this);
        GetCartData();
        GetCheckData();
        pTotalPrice = getIntent().getStringExtra("totalP");
        store_ID = helpingMethods.GetStoreID();
        ownerName = helpingMethods.GetStoreName();
        ownerImage = helpingMethods.GetStoreImage();
        ownerID = helpingMethods.GetStoreUID();


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

        SummaryAdapter summaryAdapter = new SummaryAdapter(preferenceList, this);
        msummaryRecyclerView.setAdapter(summaryAdapter);

        parseAddressJSON();


        mconfirmorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                progressDialog.show();

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                final String id = reference.child("Chats").push().getKey();
                                final DatabaseReference orderreference = FirebaseDatabase.getInstance().getReference();
                                OrdrerID = reference.child("OrderDetail").push().getKey();
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
                                String url = "https://chhatt.com/Cornstr/grocery/api/post/up_order";
                                StringRequest postdata = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        unreadReference = FirebaseDatabase.getInstance().getReference().child("Unread").child(FirebaseAuth.getInstance().getUid()).child(ownerID);
                                        reference.child("Chats").child(FirebaseAuth.getInstance().getUid() + ownerID).child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    helpingMethods.SaveCartCount(0,store_ID);
                                                    helpingMethods.SaveStoreData(null, null, null, null);
                                                    mycheckList.clear();
                                                    SaveCheckData();
                                                    preferenceList.clear();
                                                    SaveCartData();
                                                    UnreadMessage("\uD83D\uDCE6 New Order");
                                                    sendNotifiaction(ownerName, "\uD83D\uDCE6 New order has been arrived..");
                                                    Intent mintent = new Intent(OrderSummaryActivity.this, MessagingActivity.class);
                                                    mintent.putExtra("user_id", ownerID);
                                                    mintent.putExtra("check", "one");
                                                    mintent.putExtra("uName", ownerName);
                                                    mintent.putExtra("uImage", ownerImage);
                                                    mintent.putExtra("forward", "one");
                                                    mintent.putExtra("for", "one");
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



                                        for (int a = 0; a < preferenceList.size(); a++) {
                                            hashMap.put("str_prc[" + a + "]", preferenceList.get(a).getP_price());
                                            hashMap.put("ord_qty[" + a + "]", preferenceList.get(a).getP_quantity());
                                            hashMap.put("sp_id[" + a + "]", preferenceList.get(a).getProductid());
                                            hashMap.put("sp_image[" + a + "]", preferenceList.get(a).getP_img());
                                            hashMap.put("act_prc[" + a + "]", preferenceList.get(a).getActual_price());
                                            hashMap.put("str_id", store_ID);
                                            hashMap.put("sp_name[" + a + "]", preferenceList.get(a).getP_name());
                                            hashMap.put("new_address", cusaddress);
                                            hashMap.put("user_id", FirebaseAuth.getInstance().getUid());
                                            hashMap.put("t_price", pTotalPrice);
                                            hashMap.put("time", tim);
                                            hashMap.put("ord_id", OrdrerID);
                                            hashMap.put("day", dayString);


                                        }


                                        return hashMap;
                                    }

                                    ;

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
        });


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
                            kuchbhe.add(new AddressClass(jsonObject.get("address").toString(), true));
                            cusaddress = jsonObject.get("address").toString();
                        } else {
                            kuchbhe.add(new AddressClass(jsonObject.get("address").toString(), false));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                addressAdapter = new AddressAdapter(kuchbhe);
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
        SharedPreferences sharedPreferences = getSharedPreferences("Mycart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }

    private void SaveCheckData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Checkcart", MODE_PRIVATE);
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
            SharedPreferences sharedPreferences = getSharedPreferences("Mycart", MODE_PRIVATE);
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

}
