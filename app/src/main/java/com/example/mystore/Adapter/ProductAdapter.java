package com.example.mystore.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore.AllProductsActivity;
import com.example.mystore.Model.Product;
import com.example.mystore.ProductDetailActivity;
import com.example.mystore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.AllProductsActivity.mChatFab;
import static com.example.mystore.AllProductsActivity.myflag;
import static com.example.mystore.AllProductsActivity.now;
import static com.example.mystore.ui.home.HomeFragment.forWhat;


public class ProductAdapter extends BaseAdapter {

    public static String selected = "no";
    public List<Product> productsList;
    public static List<Product> selectedProducts = new ArrayList<>();
    public List selectedPositions;

    public ProductAdapter(List<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int pos, View view, final ViewGroup viewGroup) {
        final View view1;
        if (view == null) {

            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
            ImageView mImage = view1.findViewById(R.id.product_image);
            TextView mPrice = view1.findViewById(R.id.product_price);
            final TextView mQuantity = view1.findViewById(R.id.quant);
            LinearLayout mQuantLayout = view1.findViewById(R.id.quantityLayout);
            final TextView mCategory = view1.findViewById(R.id.product_category);
            final LinearLayout mSelected = view1.findViewById(R.id.selectedLayout);
            FloatingActionButton mAdd = view1.findViewById(R.id.addQty);
            FloatingActionButton mRemove = view1.findViewById(R.id.removeQty);

            mImage.setImageResource(productsList.get(pos).getProductImage());
            mPrice.setText(""+productsList.get(pos).getProductPrice());
            mCategory.setText(productsList.get(pos).getProductCategory());
            mQuantity.setText("" + productsList.get(pos).getQuantity());
            if (myflag) {
                mQuantLayout.setVisibility(View.VISIBLE);
            } else {
                mQuantLayout.setVisibility(View.GONE);
            }

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!myflag) {
                        if (selected.equals("yes")) {
                            if (mSelected.getVisibility() == View.GONE) {
                                mSelected.setVisibility(View.VISIBLE);
                                selectedProducts.add(new Product(productsList.get(pos).getProductImage(), productsList.get(pos).getProductPrice(), productsList.get(pos).getProductCategory(), pos));
                            } else {
                                for (int i = 0; i < selectedProducts.size(); i++) {
                                    int a = selectedProducts.get(i).getPosition();
                                    if (pos == a) {
                                        selectedProducts.remove(i);
                                        mSelected.setVisibility(View.GONE);
                                        if (selectedProducts.size() == 0) {
                                            mChatFab.hide();
                                            selected = "no";
                                        }

                                    }

                                }
//                                Product product = new Product();
//                                mSelected.setVisibility(View.GONE);
//                                selectedProducts.remove();
//                                if (selectedProducts.size() == 0) {
//                                    mChatFab.hide();
//                                    selected = "no";
//                                }

                            }
                        } else {
                            Intent intent = new Intent(view1.getContext(), ProductDetailActivity.class);
                            view1.getContext().startActivity(intent);
                        }

                    }
                }
            });

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q = productsList.get(pos).getQuantity();
                    q += 1;
                    mQuantity.setText("" + q);
                    productsList.get(pos).setQuantity(q);


                }
            });

            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mQuantity.getText().toString().equals("1")) {
                        int q = productsList.get(pos).getQuantity();
                        q -= 1;
                        mQuantity.setText("" + q);
                        productsList.get(pos).setQuantity(q);
                    }

                }
            });

            view1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (now.equals("yes")) {
                        mSelected.setVisibility(View.VISIBLE);
                        mChatFab.show();
                        selectedProducts.add(new Product(productsList.get(pos).getProductImage(), productsList.get(pos).getProductPrice(), productsList.get(pos).getProductCategory(), productsList.get(pos).getPosition()));
                        selected = "yes";
                    }


                    return false;
                }
            });

        } else {
            view1 = view;
        }
        return view1;
    }


}
