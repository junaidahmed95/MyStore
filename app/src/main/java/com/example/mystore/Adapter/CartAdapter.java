package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.AllProductsActivity;
import com.example.mystore.CartActivity;
import com.example.mystore.MainActivity;
import com.example.mystore.Model.Cart;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.Category;
import com.example.mystore.Model.Product;
import com.example.mystore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.mystore.Adapter.CatLvlAdapter.quantityflag;
import static com.example.mystore.Adapter.CatLvlAdapter.selectedProducts;
import static com.example.mystore.CartActivity.mTxtView_TotalPrice;
//import static com.example.mystore.ui.cart.CartFragment.mTxtView_TotalPrice;

import static com.example.mystore.CartActivity.mcardview2;
import static com.example.mystore.MainActivity.checklist;
import static com.example.mystore.SubCatActivity.setupBadge;
import static com.example.mystore.ui.cart.CartFragment.mcardview1;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Boolean flag = true;
    private List<CatLvlItemList> cartList;
    private Context mContext;
    private int mTotalPrice = 0;

    public CartAdapter(List<CatLvlItemList> cartList, Context mContext) {
        this.cartList = cartList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, final int pos) {

        viewHolder.setData(cartList.get(pos).getP_img(), cartList.get(pos).getP_name(),cartList.get(pos).getActual_price(), cartList.get(pos).getP_quantity(), cartList.get(pos).getP_price());
    if (flag){

        mTotalPrice += Integer.parseInt(cartList.get(pos).getP_price());
        if (cartList.size() -1 == pos){
            flag = false;
            mTxtView_TotalPrice.setText(""+mTotalPrice+"/-");
        }
    }


        viewHolder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(cartList.get(pos).getP_quantity());
                q += 1;
                viewHolder.mProQuantity.setText("" + q);
                cartList.get(pos).setP_quantity(""+q);
                viewHolder.Mul();
                mTotalPrice += Integer.parseInt(cartList.get(pos).getActual_price());
                mTxtView_TotalPrice.setText(""+mTotalPrice+"/-");
                cartList.get(pos).setP_price(viewHolder.mProTotal.getText().toString());
                selectedProducts.get(pos).setP_quantity(""+q);
                quantityflag = true;
               // quantityget = q;



            }

        });

        viewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.mProQuantity.getText().toString().equals("1")) {
                    int q = Integer.parseInt(cartList.get(pos).getP_quantity());
                    q -= 1;
                    viewHolder.mProQuantity.setText("" + q);
                    cartList.get(pos).setP_quantity(""+q);
                    viewHolder.Mul();
                    mTotalPrice -= Integer.parseInt(cartList.get(pos).getActual_price());
                    mTxtView_TotalPrice.setText(""+mTotalPrice+"/-");
                    cartList.get(pos).setP_price(viewHolder.mProTotal.getText().toString());
                    selectedProducts.get(pos).setP_quantity(""+q);
                    quantityflag = true;


                }

            }
        });

        viewHolder.mDeleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String[] splitit = mTxtView_TotalPrice.getText().toString().split("/");
                    int outPrice = Integer.parseInt(splitit[0]);
                    mTotalPrice = outPrice-Integer.parseInt(cartList.get(pos).getP_price());
                    mTxtView_TotalPrice.setText(""+mTotalPrice+"/-");
                    setupBadge(--MainActivity.mCartItemCount);


                  for (int a = 0; a<checklist.size(); a++){
                      if (checklist.get(a).equals(cartList.get(pos).getP_name())) {


                          cartList.remove(pos);
                          notifyItemRemoved(pos);
                          notifyItemRangeChanged(pos, cartList.size());
                          //int l = checklist.indexOf(cartList.get(pos).getP_name());
                          checklist.remove(a);
                         // selectedProducts.remove(l);

                      }
                  }

                    if(cartList.size() == 0){
                        mcardview2.setVisibility(View.GONE);
                        mcardview1.setVisibility(View.GONE);



                    }
                }catch (Exception ex){

                }


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

}

