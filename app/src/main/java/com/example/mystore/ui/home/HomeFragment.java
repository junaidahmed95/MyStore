package com.example.mystore.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mystore.Adapter.CatLvlAdapter;
import com.example.mystore.Adapter.CategoryAdapter;
import com.example.mystore.Adapter.ProductAdapter;
import com.example.mystore.Adapter.SliderAdapter;
import com.example.mystore.AllStoresActivity;
import com.example.mystore.GirdListView;
import com.example.mystore.MainAdapter;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.Category;
import com.example.mystore.Model.Product;
import com.example.mystore.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SliderView sliderView;
    private List<Category> productList;
    private RecyclerView categoryRecyclerView;
    private ScrollView mScrollView;
    public static String forWhat = "All";
    GridView gridView;
    List<GirdListView> list;
    private final String JSON_URL = "https://chhatt.com/Cornstr/grocery/api/maincat";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    ProgressBar mprogressbar;

    Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

btn = root.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllStoresActivity.class);
                startActivity(intent);
            }
        });
        sliderView = root.findViewById(R.id.imageSlider);
        categoryRecyclerView = root.findViewById(R.id.cat_recyclerView);
        mprogressbar = root.findViewById(R.id.progressbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        final SliderAdapter adapter = new SliderAdapter(getActivity());
        adapter.setCount(6);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        productList = new ArrayList<>();

        parseJSON();


//        gridView = root.findViewById(R.id.gd1);
//        list = new ArrayList<>();
//
//        list.add(new GirdListView("2019 new autumn kids shoes","PKR 1,174.23","688 Sold",R.drawable.cocomo));
//
//
//        gridView.setAdapter(new MainAdapter(list,getActivity()));

        return root;
    }

    private void parseJSON() {
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        String catTitle = jsonObject.getString("m_name");
                        String catimage = jsonObject.getString("thumbnail");
                        productList.add(new Category(catimage, catTitle));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                CategoryAdapter categoryAdapter = new CategoryAdapter(productList, getActivity());
                categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
                mprogressbar.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


    }

}


