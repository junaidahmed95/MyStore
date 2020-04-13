package com.example.mystore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;
import static com.example.mystore.SubCatActivity.list;
import static com.example.mystore.SubCatActivity.mProgressDialog;
import static com.example.mystore.SubCatActivity.mtabs;
import static com.example.mystore.SubCatActivity.prolist;

/**
 * A simple {@link Fragment} subclass.
 */

public class CatLvlFragment extends Fragment {


    //private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/product";
    //private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/storeprods";
    private String JSON_URL = "";

    private JsonArrayRequest request;
    private List<CatLvlItemList> originalList;
    private RequestQueue requestQueue;
    private String mTitle;
    private GridView mgridView;


    public CatLvlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_lvl, container, false);
        mgridView = view.findViewById(R.id.gridview);
        originalList = new ArrayList<>();

        //parseJSON();
        //Toast.makeText(getActivity(), "This is your store id"+store_id, Toast.LENGTH_LONG).show();

        return view;
    }


//    private void parseJSON() {
//         JSON_URL = "https://chhatt.com/Cornstr/grocery/api/get/stores/products?str_id="+store_id;
//        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                JSONObject jsonObject = null;
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        jsonObject = response.getJSONObject(i);
//
//                        mTitle = jsonObject.getString("product_name");
//                        String mprice = jsonObject.getString("str_prc");
//                        String mimage = jsonObject.getString("product_image");
//                        store_id = jsonObject.getString("str_id");
//                        String product_id =jsonObject.getString("p_id");
//
//
//
//
//                        prolist.add(new CatLvlItemList(mTitle, mprice,product_id,mimage));
//                        CatLvlAdapter catLvlAdapter = new CatLvlAdapter(prolist, getActivity());
//                        mgridView.setAdapter(catLvlAdapter);
//                        catLvlAdapter.notifyDataSetChanged();
//                        mprogressbar.setVisibility(View.GONE);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//
//        requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(request);
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mtabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    originalList.clear();
                    for (int i = 0; i < prolist.size(); i++) {
                        if (prolist.get(i).getCatName().equals(tab.getText())) {
                            originalList.add(new CatLvlItemList(prolist.get(i).getP_name(), prolist.get(i).getP_price(), prolist.get(i).getProductid(), prolist.get(i).getP_img()));
                        }
                    }
                    CatLvlAdapter catLvlAdapter = new CatLvlAdapter(originalList, getActivity());
                    mgridView.setAdapter(catLvlAdapter);
                    catLvlAdapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }
}