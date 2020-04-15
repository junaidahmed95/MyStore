package com.example.mystore.ui.cart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Adapter.AddressAdapter;
import com.example.mystore.Adapter.CartAdapter;
import com.example.mystore.Adapter.SummaryAdapter;
import com.example.mystore.Model.Cart;
import com.example.mystore.Model.ConnectionDetector;
import com.example.mystore.Model.OrderSummary;
import com.example.mystore.OrderSummaryActivity;
import com.example.mystore.ProfileActivity;
import com.example.mystore.R;
import com.example.mystore.videoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;


public class CartFragment extends Fragment {

    private RecyclerView mCartRecyclerView, mAddressRecyclerView;
    public static CartAdapter cartAdapter;
    public static TextView mTxtView_Total;
    public static CardView mcardview1;
    private Button mcheckBtn;
    private BottomSheetDialog checkOutDialog;

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

        cartAdapter = new CartAdapter(selectedProducts, getActivity(),"fragment");
        mCartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        Bundle bundle = new Bundle();

        if (selectedProducts.size() == 0) {
            mcardview1.setVisibility(View.GONE);

        }


        mcheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), OrderSummaryActivity.class));
            }
        });


        return root;
    }

}