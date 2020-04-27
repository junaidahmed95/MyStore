package com.bringo.home.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bringo.home.Adapter.OrderAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends Fragment {
    private List<CatLvlItemList> myFavList;
    RecyclerView mwishrecyclerView;

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_wish_list, container, false);

        mwishrecyclerView = mView.findViewById(R.id.wishrecyclerView);
        GetFavData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mwishrecyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(myFavList, getActivity(), false, true);
        mwishrecyclerView.setAdapter(orderAdapter);

        return mView;
    }

    private void GetFavData() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Myfav", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("favlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            myFavList = gson.fromJson(json, type);

            if (myFavList == null) {
                myFavList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
