package com.example.mystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.GirdListView;
import com.example.mystore.MainActivity;
import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.Model.Product;
import com.example.mystore.Model.ProductName;
import com.example.mystore.ProductDetailActivity;
import com.example.mystore.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;
import static com.example.mystore.AllProductsActivity.mChatFab;
import static com.example.mystore.MainActivity.checklist;
import static com.example.mystore.MainActivity.flagfroprice;
import static com.example.mystore.SubCatActivity.mfbcart;
import static com.example.mystore.SubCatActivity.setupBadge;

public class CatLvlAdapter extends BaseAdapter {
    // public static List<String> checklist = new ArrayList<>();

    public static List<CatLvlItemList> selectedProducts = new ArrayList<>();
    public static List<CatLvlItemList> list;
    private List<CatLvlItemList> wishlist;
    private List<String> getfavtlist;
    public static boolean quantityflag = false;
     Button mbtn_add_cart;
    List<CatLvlItemList> cartlist = new ArrayList<>();
    int pri;
   public static List<CatLvlItemList> favlist = new ArrayList<>();

    Context context;
    Boolean favflag = true;
    static String selected = "no";



    public CatLvlAdapter(List<CatLvlItemList> list, Context context) {
        this.list = list;
        this.context = context;
        wisddata();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);



        ImageView i = convertView.findViewById(R.id.product_image);
        TextView t1 = convertView.findViewById(R.id.product_category);
        TextView t2 = convertView.findViewById(R.id.product_price);
        final ImageView mwish = convertView.findViewById(R.id.wis);
        mbtn_add_cart = convertView.findViewById(R.id.btn_add_cart);
        final Button mbtn_remove_cart = convertView.findViewById(R.id.btn_remove_cart);
        //final LinearLayout mbtLayout = convertView.findViewById(R.id.btLayout);
        final Button mplusQty = convertView.findViewById(R.id.plusQty);
        final Button mminusQty = convertView.findViewById(R.id.minusQty);
        final TextView mproQuantity = convertView.findViewById(R.id.proQuantity);

        final LinearLayout mSelected = convertView.findViewById(R.id.selectedLayout);

        Glide.with(context).load(list.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(i);
        t1.setText(list.get(position).getP_name());
        t2.setText("" + list.get(position).getP_price());

//        for (int a=0; a<wishlist.size(); a++){
//            if (wishlist.get(a).getProductid().equals(list.get(position).getProductid())){
//                //Toast.makeText(context, "asdasdsad", Toast.LENGTH_SHORT).show();
//                mwish.setImageResource(R.drawable.ic_favorite_black_24dp);
//                ImageViewCompat.setImageTintList(mwish,
//                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)));
//            }
//
//        }

//
//        if (wishlist.size() > 0) {
//            String wisName = wishlist.get(position).getP_name();
//            String favname= favlist.get(position).getP_name();
//            if( wisName==favname ){
//            //if (wishlist.contains(list.get(position).getP_name())) {
//                mwish.setImageResource(R.drawable.ic_favorite_black_24dp);
//                ImageViewCompat.setImageTintList(mwish,
//                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)));
//            } else {
//                mwish.setImageResource(R.drawable.ic_like);
//                ImageViewCompat.setImageTintList(mwish,
//                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray)));
//            }
//
//        }


        mwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!favlist.contains(list.get(position).getP_name())) {
//                    if(!wishlist.contains(list.get(position).getP_name())){
                    favlist.add(new CatLvlItemList(list.get(position).getP_name(), list.get(position).getP_price(), list.get(position).getP_img(), list.get(position).getProductid()));
                    mwish.setImageResource(R.drawable.ic_favorite_black_24dp);
                    ImageViewCompat.setImageTintList(mwish,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)));
                    saveData();
                }
//                }
                else {
//                    if(wishlist.contains(list.get(position).getP_name())){
//                        wishlist.remove(list.get(position).getP_name());
                    favlist.remove(list.get(position).getP_name());
                    mwish.setImageResource(R.drawable.ic_like);
                    ImageViewCompat.setImageTintList(mwish,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray)));
                }
//                }

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (selected.equals("yes")) {
//                    if (mSelected.getVisibility() == View.GONE) {
//                        mSelected.setVisibility(View.VISIBLE);
//                        selectedProducts.add(new Product(productsList.get(pos).getProductImage(), productsList.get(pos).getProductPrice(), productsList.get(pos).getProductCategory(), pos));
//                    } else {
//                        for (int i = 0; i < selectedProducts.size(); i++) {
//                            int a = selectedProducts.get(i).getPosition();
//                            if (pos == a) {
//                                selectedProducts.remove(i);
//                                mSelected.setVisibility(View.GONE);
//                                if (selectedProducts.size() == 0) {
//                                    mChatFab.hide();
//                                    selected = "no";
//                                }
//
//                            }
//
//                        }
////                                Product product = new Product();
////                                mSelected.setVisibility(View.GONE);
////                                selectedProducts.remove();
////                                if (selectedProducts.size() == 0) {
////                                    mChatFab.hide();
////                                    selected = "no";
////                                }
//
//                    }
//                } else {

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("image", list.get(position).getP_img());
                intent.putExtra("name", list.get(position).getP_name());
                intent.putExtra("price", list.get(position).getP_price());
                context.startActivity(intent);
//                }
//
            }
        });

//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                  mSelected.setVisibility(View.VISIBLE);
//                mfbcart.show();
//
//
//                if (checklist.contains(list.get(position).getP_name())) {
//
//                    int a = checklist.indexOf(list.get(position).getP_name());
//
//                    int quantity = selectedProducts.get(a).getP_quantity();
//                    selectedProducts.get(a).setP_quantity(++quantity);
//                    int p = selectedProducts.get(a).getP_price();
//                    selectedProducts.get(a).setP_price(p + list.get(position).getP_price());
//
//
//                } else {
//                    checklist.add(list.get(position).getP_name());
//                    selectedProducts.add(new CatLvlItemList(list.get(position).getP_name(), list.get(position).getP_price(), list.get(position).getP_quantity(), list.get(position).getP_img(), position, list.get(position).getP_price()));
//                }
//
//
//                return false;
//            }
//        });

//        if (quantityflag){
//            if (checklist.contains(list.get(position).getP_name())) {
//                mbtLayout.setVisibility(View.VISIBLE);
//                int a = checklist.indexOf(list.get(position).getP_name());
//                mproQuantity.setText("" + selectedProducts.get(a).getP_quantity());
//                mbtn_add_cart.setVisibility(View.GONE);
//                setupBadge(++mCartItemCount);
//                quantityflag = false;
//            }
//        }
        if (checklist.contains(list.get(position).getP_name())) {
            mbtn_remove_cart.setVisibility(View.VISIBLE);
        } else {
            mbtn_remove_cart.setVisibility(View.GONE);
        }

        mbtn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbtn_add_cart.setVisibility(View.GONE);
                mbtn_remove_cart.setVisibility(View.VISIBLE);


                checklist.add(list.get(position).getP_name());



                selectedProducts.add(new CatLvlItemList(list.get(position).getP_name(), list.get(position).getP_price(),list.get(position).getP_quantity(), list.get(position).getP_img(),position,list.get(position).getP_price(),list.get(position).getProductid()));
                setupBadge(selectedProducts.size());

                //saveData();
            }
        });

        mbtn_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbtn_remove_cart.setVisibility(View.GONE);
                mbtn_add_cart.setVisibility(View.VISIBLE);
                setupBadge(--MainActivity.mCartItemCount);
                if (checklist.contains(list.get(position).getP_name())) {

                    int a = checklist.indexOf(list.get(position).getP_name());
                    checklist.remove(a);
                    selectedProducts.remove(a);

                    notifyDataSetChanged();

                }
            }
        });

//        mplusQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                                if (checklist.contains(list.get(position).getP_name())) {
//
//                    int a = checklist.indexOf(list.get(position).getP_name());
//
//                    int quantity = selectedProducts.get(a).getP_quantity();
//                    selectedProducts.get(a).setP_quantity(++quantity);
//                    int p = selectedProducts.get(a).getP_price();
//                    selectedProducts.get(a).setP_price(p + list.get(position).getP_price());
//                                    int q = list.get(position).getP_quantity();
//                                    if (quantityflag){
//                                        q = selectedProducts.get(position).getP_quantity();
//                                    }
//                                    else{
//                                        q = list.get(position).getP_quantity();
//                                        q += 1;
//                                    }
//
//                                    mproQuantity.setText("" + q);
//                                    list.get(position).setP_quantity(q);
//
//
//                }
//                                else {
//                    checklist.add(list.get(position).getP_name());
//                    selectedProducts.add(new CatLvlItemList(list.get(position).getP_name(), list.get(position).getP_price(), list.get(position).getP_quantity(), list.get(position).getP_img(), position, list.get(position).getP_price()));
//                }
//
//
//            }
//        });

//        mminusQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!mproQuantity.getText().toString().equals("1")) {
//                    int q = list.get(position).getP_quantity();
//                    q -= 1;
//                    mproQuantity.setText("" + q);
//                    list.get(position).setP_quantity(q);
//                }else if (mproQuantity.getText().toString().equals("1")){
//                    setupBadge(--mCartItemCount);
//                    mbtn_add_cart.setVisibility(View.VISIBLE);
//                    mbtLayout.setVisibility(View.GONE);
//
//x
//                }
//
//            }
//        });


        return convertView;

    }



    public void filterList(ArrayList<CatLvlItemList> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favlist);
        editor.putString("list", json);
        editor.apply();
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
    }


    private void wisddata() {
//        try{
//            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = sharedPreferences.getString("list", null);
//            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
//            }.getType();
//            wishlist = gson.fromJson(json, type);
//
//            if (wishlist == null) {
//                wishlist = new ArrayList<>();
//            }
//        }
//        catch (Exception e)
//
//        {
//            Toast.makeText(mbtn_add_cart.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }


    }

}

