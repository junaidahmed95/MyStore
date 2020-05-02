package com.bringo.home.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bringo.home.Adapter.StatusAdapter;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.OrderHistory;
import com.bringo.home.R;
import com.bringo.home.Verification;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    String get_status = "http://bringo.biz/api/get/order/status?ord_id=9bGqUshRJrS8ZHPk";
    private final String JSON_URL = "http://bringo.biz/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    List<OrderHistory> historylist;
    private Button mbtnSiglo;
    private HelpingMethods helpingMethods;
    List<OrderHistory> products_list;
    RecyclerView mstatus_recycler;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        historylist = new ArrayList<>();
        products_list = new ArrayList<>();
        mstatus_recycler = view.findViewById(R.id.status_recycler);
        mbtnSiglo = view.findViewById(R.id.btnSiglo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mstatus_recycler.setLayoutManager(linearLayoutManager);

        mbtnSiglo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Verification.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
            if(connectionDetector.isConnected()){
                parseJSON();
            }else {
                Toast.makeText(getActivity(), "Check your internet", Toast.LENGTH_SHORT).show();
            }

        } else {
            mbtnSiglo.setVisibility(View.VISIBLE);
        }


        return view;
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
                            if (!storeObject.getString("status").equals("null")) {
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
                        if (products_list.size() > 0) {

                            historylist.add(new OrderHistory(storeOrderId, storeimg, new ArrayList<OrderHistory>(products_list)));
                            StatusAdapter statusAdapter = new StatusAdapter(historylist, getActivity());
                            mstatus_recycler.setAdapter(statusAdapter);
                            statusAdapter.notifyDataSetChanged();
                            products_list.clear();

                        }


                        // mProgressDialog.cancel();
                    }


                } catch (JSONException e) {
                    // mProgressDialog.cancel();
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mProgressDialog.cancel();
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

}