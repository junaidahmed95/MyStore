package com.bringo.home.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.HistoryAdapter;
import com.bringo.home.Model.OrderHistory;
import com.bringo.home.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PendingOrderFragment extends Fragment {

    private final String JSON_URL = "https://bringo.biz/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    private List<OrderHistory> historylist;
    private List<OrderHistory> products_list;
    private ProgressDialog mProgressDialog;
    private RecyclerView mhis_recycler;


    public PendingOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_pending_order, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        historylist = new ArrayList<>();
        products_list = new ArrayList<>();
        mhis_recycler = mView.findViewById(R.id.his_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mhis_recycler.setLayoutManager(linearLayoutManager);


        parseJSON();

        return mView;
    }

    private void parseJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    JSONArray storeOrders = response.getJSONArray(0);
                    JSONObject storeOrdersDetail = response.getJSONObject(1);


                    for (int i = 0; i < storeOrders.length(); i++) {

                        JSONObject storeOrder = storeOrders.getJSONObject(i);


                        String storeName = storeOrder.getString("str_name");
                        String storeOrderId = storeOrder.getString("ord_id");
                        String storeimg = storeOrder.getString("user_thumb");
                        JSONArray storeOrderDetails = storeOrdersDetail.getJSONArray(storeOrderId);

                        for (int j = 0; j < storeOrderDetails.length(); j++) {

                            JSONObject storeObject = storeOrderDetails.getJSONObject(j);
                            if (storeObject.getString("status").equals("null")) {
                                String pname = storeObject.getString("sp_name");
                                String actprice = storeObject.getString("act_prc");
                                String address = storeObject.getString("new_address");
                                String proimage = storeObject.getString("sp_image");
                                String pqty = storeObject.getString("ord_qty");
                                String tprice = storeObject.getString("t_price");
                                String datetime = storeObject.getString("created_at");
                                String uid = storeObject.getString("user_id");
                                String tpprice = storeObject.getString("str_prc");
                                String status = storeObject.getString("status");
                                products_list.add(new OrderHistory(actprice, pqty, storeName, datetime, proimage, pname, uid, address, status, tprice, tpprice));

                            }


                        }
                        if(products_list.size()>0){
                            historylist.add(new OrderHistory(storeOrderId, storeimg, new ArrayList<OrderHistory>(products_list)));
                            HistoryAdapter historyadp = new HistoryAdapter(historylist, getActivity());
                            mhis_recycler.setAdapter(historyadp);
                            historyadp.notifyDataSetChanged();
                            products_list.clear();
                        }




                        mProgressDialog.cancel();
                    }


                } catch (JSONException e) {
                    mProgressDialog.cancel();
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.cancel();
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


}