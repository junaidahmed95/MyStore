package com.bringo.home.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.OrderSummaryActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import static com.bringo.home.CartActivity.mTxtView_TotalPrice;
//import static com.example.mystore.ui.cart.CartFragment.mTxtView_TotalPrice;

import static com.bringo.home.CartActivity.mcardview2;
import static com.bringo.home.SubCatActivity.setupBadge;
import static com.bringo.home.ui.cart.CartFragment.mTxtView_Total;
import static com.bringo.home.ui.cart.CartFragment.mcardview1;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CatLvlItemList> cartList;
    private Context mContext;
    private List<String> MecheckList;
    private boolean flag = false;
    private List<CatLvlItemList> preferenceList;
    private String fromWhere;
    private boolean isUpdate;
    private HelpingMethods helpingMethods;
    private int mTotalPrice = 0;

    public CartAdapter(List<CatLvlItemList> cartList, Context mContext, String fromWhere) {
        this.cartList = cartList;
        this.fromWhere = fromWhere;
        this.mContext = mContext;
        GetCartData();
        GetCheckData();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Activity activity = (Activity) mContext;
        helpingMethods = new HelpingMethods(activity);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, final int pos) {
        viewHolder.setData(cartList.get(pos).getP_img(), cartList.get(pos).getP_name(), cartList.get(pos).getP_price(), cartList.get(pos).getP_quantity(), cartList.get(pos).getActual_price());

        if (!flag){
            mTotalPrice += Integer.parseInt(cartList.get(pos).getActual_price());
            if (fromWhere.equals("activity")) {
                mTxtView_TotalPrice.setText("" + mTotalPrice + "/-");
                mcardview2.setVisibility(View.VISIBLE);
            } else {
                mTxtView_Total.setText("" + mTotalPrice + "/-");
                mcardview1.setVisibility(View.VISIBLE);
            }

        }




        viewHolder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(cartList.get(pos).getP_quantity());
                q += 1;
                viewHolder.mProQuantity.setText("" + q);
                cartList.get(pos).setP_quantity("" + q);
                preferenceList.get(pos).setP_quantity("" + q);
                viewHolder.Mul();
                cartList.get(pos).setActual_price(viewHolder.mProTotal.getText().toString());
                preferenceList.get(pos).setActual_price(viewHolder.mProTotal.getText().toString());

                mTotalPrice += Integer.parseInt(cartList.get(pos).getP_price());
                if (fromWhere.equals("activity")) {
                    mTxtView_TotalPrice.setText("" + mTotalPrice + "/-");
                } else {
                    mTxtView_Total.setText("" + mTotalPrice + "/-");
                }
                SaveCartData();
            }

        });

        viewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.mProQuantity.getText().toString().equals("1")) {
                    int q = Integer.parseInt(cartList.get(pos).getP_quantity());
                    q -= 1;
                    viewHolder.mProQuantity.setText("" + q);
                    cartList.get(pos).setP_quantity("" + q);
                    preferenceList.get(pos).setP_quantity("" + q);
                    viewHolder.Mul();
                    cartList.get(pos).setActual_price(viewHolder.mProTotal.getText().toString());
                    preferenceList.get(pos).setActual_price(viewHolder.mProTotal.getText().toString());
                    mTotalPrice -= Integer.parseInt(cartList.get(pos).getP_price());
                    if (fromWhere.equals("activity")) {
                        mTxtView_TotalPrice.setText("" + mTotalPrice + "/-");
                    } else {
                        mTxtView_Total.setText("" + mTotalPrice + "/-");
                    }

                    SaveCartData();
                }

            }
        });

        viewHolder.mDeleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                try {
                                    mTotalPrice -= Integer.parseInt(cartList.get(pos).getActual_price());
                                    int finalCount = helpingMethods.GetCartCount(cartList.get(pos).getStoreId()) - 1;
                                    helpingMethods.SaveCartCount(finalCount,cartList.get(pos).getStoreId());
                                    if (fromWhere.equals("activity")) {
                                        mTxtView_TotalPrice.setText("" + mTotalPrice + "/-");
                                        setupBadge();
                                    } else {
                                        mTxtView_Total.setText("" + mTotalPrice + "/-");
                                    }

                                    int a = MecheckList.indexOf(cartList.get(pos).getSimplePID());
                                    MecheckList.remove(a);
                                    SaveCheckData();
                                    preferenceList.remove(a);
                                    SaveCartData();
                                    cartList.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, cartList.size());
                                    flag = true;


                                    if (cartList.size() == 0) {
                                        if (fromWhere.equals("activity")) {
                                            mcardview2.setVisibility(View.GONE);
                                        } else {
                                            mcardview1.setVisibility(View.GONE);
                                        }


                                    }


                                } catch (Exception ex) {
                                    Toast.makeText(mContext, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();

                                }


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Do you want to remove this product?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();







            }
        });

    }


    @Override

    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mProImage;
        private Button mAddButton, mRemoveButton;
        private TextView mProName, mProPrice, mProQuantity, mProTotal;
        private FloatingActionButton mDeleFab;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mDeleFab = itemView.findViewById(R.id.deleteFab);
            mProImage = itemView.findViewById(R.id.proImage);
            mProName = itemView.findViewById(R.id.proName);
            mProPrice = itemView.findViewById(R.id.proPrice);
            mProQuantity = itemView.findViewById(R.id.proQuantity);
            mProTotal = itemView.findViewById(R.id.proTotal);
            mAddButton = itemView.findViewById(R.id.addQty);
            mRemoveButton = itemView.findViewById(R.id.removeQty);

        }

        private void setData(String image, String name, String price, String qty, String total) {

            Glide.with(mContext).load(image).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(mProImage);
            mProName.setText(name);
            mProPrice.setText("" + price);
            mProQuantity.setText("" + qty);
            mProTotal.setText("" + total);
        }

        private void Mul() {
            int p = Integer.parseInt(mProPrice.getText().toString());
            int c = Integer.parseInt(mProQuantity.getText().toString());
            int result = p * c;
            mProTotal.setText("" + result);
        }

    }

    private void SaveCartData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mycart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }


    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mycart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }



        } catch (Exception e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveCheckData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MecheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Checkcart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("checklist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            MecheckList = gson.fromJson(json, type);

            if (MecheckList == null) {
                MecheckList = new ArrayList<>();
            }


        } catch (Exception e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}


