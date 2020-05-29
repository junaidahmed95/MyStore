package com.bringo.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    String get_status = "https://bringo.biz/api/get/order/status?ord_id=9bGqUshRJrS8ZHPk";
    private final String JSON_URL = "https://bringo.biz/api/get/order?user_id=" + FirebaseAuth.getInstance().getUid();
    List<OrderHistory> historylist;
    private Button mbtnSiglo, mbtnRetry;
    private TextView mnoOrder;
    private ProgressDialog mProgressDialog;
    private HelpingMethods helpingMethods;
    List<OrderHistory> products_list;
    RecyclerView mstatus_recycler;
    private List<String> orderIDList;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Getting orders...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        orderIDList = new ArrayList<>();

        mbtnRetry = view.findViewById(R.id.btnRetry);

        mnoOrder = view.findViewById(R.id.noOrder);
        historylist = new ArrayList<>();
        products_list = new ArrayList<>();
        helpingMethods = new HelpingMethods(getActivity());
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
                    mbtnRetry.setVisibility(View.GONE);
                    mProgressDialog.show();
                    parseJSON();
                } else {
                    mProgressDialog.cancel();
                    Toast.makeText(getActivity(), "Check your internet", Toast.LENGTH_SHORT).show();
                    mbtnRetry.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
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
                                String user_thumb = data.getString("user_thumb");
                                historylist.add(new OrderHistory(str_name, id, ord_id, t_price, created_at, address, user_thumb));

                            }

                            if (historylist.size() > 0) {
                                StatusAdapter statusAdapter = new StatusAdapter(historylist, getActivity());
                                mstatus_recycler.setAdapter(statusAdapter);
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