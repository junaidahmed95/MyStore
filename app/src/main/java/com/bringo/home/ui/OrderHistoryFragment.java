package com.bringo.home.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
public class OrderHistoryFragment extends Fragment {
    private final String JSON_URL = "https://bringo.biz/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    private List<OrderHistory> historylist;
    private List<OrderHistory> products_list;
    private ProgressDialog mProgressDialog;
    RecyclerView mhis_recycler;
    private HelpingMethods helpingMethods;
    private Button mbtnSiglo, mbtnRetry;
    private TextView mnoOrder;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order_history, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mnoOrder = mView.findViewById(R.id.noOrder);
        helpingMethods = new HelpingMethods(getActivity());
        mbtnSiglo = mView.findViewById(R.id.btnSiglo);
        historylist = new ArrayList<>();
        products_list = new ArrayList<>();
        mhis_recycler = mView.findViewById(R.id.his_recycler);
        mbtnRetry = mView.findViewById(R.id.btnRetry);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mhis_recycler.setLayoutManager(linearLayoutManager);

        mbtnSiglo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Verification.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
            if (connectionDetector.isConnected()) {
                parseJSON();
            } else {
                mProgressDialog.cancel();
                Toast.makeText(getActivity(), "Check your internet", Toast.LENGTH_SHORT).show();
                mbtnRetry.setVisibility(View.VISIBLE);
            }

        } else {
            mProgressDialog.cancel();
            mbtnSiglo.setVisibility(View.VISIBLE);
        }


        mbtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
                if (connectionDetector.isConnected()) {
                    mProgressDialog.show();
                    mbtnRetry.setVisibility(View.GONE);
                    parseJSON();
                } else {
                    mProgressDialog.cancel();
                    Toast.makeText(getActivity(), "Check your internet", Toast.LENGTH_SHORT).show();
                    mbtnRetry.setVisibility(View.VISIBLE);
                }
            }
        });


        return mView;

    }

    private void parseJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://bringo.biz/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray array = response.getJSONArray("Data");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject data = array.getJSONObject(i);


                                String str_name = data.getString("str_name");
                                String id = data.getString("id");
                                String ord_id = data.getString("ord_id");
                                String t_price = data.getString("t_price");
                                String created_at = data.getString("created_at");
                                String address = data.getString("ord_id");
                                String user_thumb = data.getString("thumbnail");
                                historylist.add(new OrderHistory(str_name, id, ord_id, t_price, created_at, address, user_thumb));

                            }

                            if (historylist.size() > 0) {
                                StatusAdapter statusAdapter = new StatusAdapter(historylist, getActivity());
                                mhis_recycler.setAdapter(statusAdapter);
                                statusAdapter.notifyDataSetChanged();
                            } else {
                                mnoOrder.setVisibility(View.VISIBLE);
                            }
                            mProgressDialog.cancel();

                        } catch (JSONException e) {
                            mProgressDialog.cancel();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        mProgressDialog.cancel();
                        Toast.makeText(getActivity(), "Error" + error, Toast.LENGTH_SHORT).show();
                        mbtnRetry.setVisibility(View.VISIBLE);

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }
}
