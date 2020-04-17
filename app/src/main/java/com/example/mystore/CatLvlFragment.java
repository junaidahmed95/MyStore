package com.example.mystore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Adapter.PCatAdapter;
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
import static com.example.mystore.MainActivity.checklist;
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
    private boolean isLoad = false;
    private JsonArrayRequest request;
    private List<CatLvlItemList> originalList;
    private RequestQueue requestQueue;
    private String mTitle;
    private PCatAdapter proAdapter;
    private RecyclerView mpRecyclerView;


    public CatLvlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_lvl, container, false);
        mpRecyclerView = view.findViewById(R.id.pRecyclerView);
        mpRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        originalList = new ArrayList<>();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isLoad){
            mpRecyclerView.setAdapter(proAdapter);
            proAdapter.notifyDataSetChanged();
        }
        isLoad = true;

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
                    proAdapter = new PCatAdapter(originalList, getActivity());
                    mpRecyclerView.setAdapter(proAdapter);
                    proAdapter.notifyDataSetChanged();
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