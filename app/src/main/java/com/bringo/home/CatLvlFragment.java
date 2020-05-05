package com.bringo.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bringo.home.Adapter.PCatAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.bringo.home.SubCatActivity.mloadingImage;
import static com.bringo.home.SubCatActivity.mtabs;
import static com.bringo.home.SubCatActivity.prolist;

/**
 * A simple {@link Fragment} subclass.
 */

public class CatLvlFragment extends Fragment {


    private String JSON_URL = "";
    private boolean isOneTime;
    private JsonArrayRequest request;
    private List<CatLvlItemList> originalList;
    private RequestQueue requestQueue;
    private String mTitle;
    private RecyclerView mpRecyclerView;
    private String sID, ownerID, ownerImage, ownerName,catName;

    public CatLvlFragment(String sID, String ownerID, String ownerImage, String ownerName,String catName) {
        // Required empty public constructor
        this.catName=catName;
        this.sID = sID;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.ownerImage = ownerImage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_lvl, container, false);
        mpRecyclerView = view.findViewById(R.id.pRecyclerView);
        mpRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        originalList = new ArrayList<>();

        return view;
    }

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
                            //mTitle, mprice,mimage,product_id,str_id,mCat,sim_id
                            originalList.add(new CatLvlItemList(prolist.get(i).getP_name(), prolist.get(i).getP_price(),  prolist.get(i).getP_img(), prolist.get(i).getProductid(),prolist.get(i).getStoreId(),prolist.get(i).getCatName(),prolist.get(i).getSimplePID(),prolist.get(i).getP_price(),prolist.get(i).getDesc()));
                        }
                    }
                    PCatAdapter proAdapter = new PCatAdapter(originalList, getActivity(), sID,ownerID, ownerImage, ownerName,catName,false);
                    mpRecyclerView.setAdapter(proAdapter);
                    proAdapter.notifyDataSetChanged();
                    mloadingImage.setVisibility(View.GONE);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        } catch (Exception e) {
            mloadingImage.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}