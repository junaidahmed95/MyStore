package com.bringo.home.ui.cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.Adapter.CartAdapter;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.OrderSummaryActivity;
import com.bringo.home.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;



public class CartFragment extends Fragment {

    private RecyclerView mCartRecyclerView, mAddressRecyclerView;
    public static TextView mTxtView_Total;
    public static CardView mcardview1;
    private Button mcheckBtn;
    private List<CatLvlItemList> preferenceList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);


        mTxtView_Total = root.findViewById(R.id.totalPrice);
        mCartRecyclerView = root.findViewById(R.id.cartRecyclerView);
        mcheckBtn = root.findViewById(R.id.checkBtn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mCartRecyclerView.setLayoutManager(layoutManager);
        mcardview1 = root.findViewById(R.id.cardVew1);
        GetCartData();
        CartAdapter cartAdapter = new CartAdapter(preferenceList, getActivity(), "fragment");
        mCartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        if (preferenceList.size() == 0) {
            mcardview1.setVisibility(View.GONE);

        }


        mcheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
                if(connectionDetector.isConnected()){
                    Intent sumInt = new Intent(getActivity(), OrderSummaryActivity.class);
                    sumInt.putExtra("from", "fragement");
                    sumInt.putExtra("totalP",mTxtView_Total.getText().toString());
                    startActivity(sumInt);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else {
                    Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return root;
    }

    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mycart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}