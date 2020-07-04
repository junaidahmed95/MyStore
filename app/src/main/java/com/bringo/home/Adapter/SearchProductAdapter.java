package com.bringo.home.Adapter;

import android.app.Activity;
import android.content.Context;
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

import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bringo.home.MainActivity.MainsetupBadge;
import static com.bringo.home.SearchActivity.SearchsetupBadge;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {

    public void filterList(ArrayList<CatLvlItemList> filteredList) {
        searchlist = filteredList;
        notifyDataSetChanged();
    }

    public List<CatLvlItemList> searchlist;
    private Context mContext;
    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private HelpingMethods helpingMethods;

    public SearchProductAdapter(List<CatLvlItemList> searchlist, Context mContext) {
        this.searchlist = searchlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Activity activity = (Activity) mContext;
        helpingMethods = new HelpingMethods(activity);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext).asBitmap().load(searchlist.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.pImage);
        holder.pName.setText(searchlist.get(position).getP_name());
        holder.pDesc.setText(searchlist.get(position).getDesc());
        holder.pPrice.setText("" + searchlist.get(position).getP_price());

        try {
            if (mycheckList.size() > 0) {
                if (mycheckList.contains(searchlist.get(position).getSimplePID())) {
                    holder.mbtn_add_cart.setVisibility(View.GONE);
                    holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
                } else {
                    holder.mbtn_remove_cart.setVisibility(View.GONE);
                    holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {

        }


        holder.mbtn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpingMethods.GetStoreID() != null) {
                    if (searchlist.get(position).getStoreId().equals(helpingMethods.GetStoreID())) {
                        GetCartData(position);
                        GetCheckData(position);
                        SaveProduct(holder, position);
                    } else {
                        Toast.makeText(mContext, "You cant add products from multiple store.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    GetCartData(position);
                    GetCheckData(position);
                    SaveProduct(holder, position);
                }


            }
        });

        holder.mbtn_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mbtn_remove_cart.setVisibility(View.GONE);
                holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                int finalCount = helpingMethods.GetCartCount(searchlist.get(position).getStoreId()) - 1;
                helpingMethods.SaveCartCount(finalCount, searchlist.get(position).getStoreId());
                int total = helpingMethods.GetCartTotal(searchlist.get(position).getStoreId()) - Integer.parseInt(searchlist.get(position).getP_price());
                helpingMethods.SaveCartTotal(String.valueOf(total), searchlist.get(position).getStoreId());

                SearchsetupBadge();
                MainsetupBadge();

                int a = mycheckList.indexOf(searchlist.get(position).getSimplePID());
                mycheckList.remove(a);
                SaveCheckData(position);
                preferenceList.remove(a);
                SaveCartData(position);
                notifyDataSetChanged();

            }


        });

    }

    private void SaveProduct(ViewHolder holder, int position) {
        holder.mbtn_add_cart.setVisibility(View.GONE);
        holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
        int finalCount = helpingMethods.GetCartCount(searchlist.get(position).getStoreId()) + 1;
        helpingMethods.SaveCartCount(finalCount, searchlist.get(position).getStoreId());
        int total = helpingMethods.GetCartTotal(searchlist.get(position).getStoreId()) + Integer.parseInt(searchlist.get(position).getP_price());
        helpingMethods.SaveCartTotal(String.valueOf(total), searchlist.get(position).getStoreId());


        if (helpingMethods.GetStoreID() != null) {
            if (!helpingMethods.GetStoreID().equals(searchlist.get(position).getStoreId())) {
                helpingMethods.SaveStoreData(searchlist.get(position).getStoreId(), searchlist.get(position).getOwnername(), searchlist.get(position).getOwnerimage(), searchlist.get(position).getOwnerid());
                mycheckList.clear();
                SaveCheckData(position);
                preferenceList.clear();
                SaveCartData(position);
            }
        } else {
            helpingMethods.SaveStoreData(searchlist.get(position).getStoreId(), searchlist.get(position).getOwnername(), searchlist.get(position).getOwnerimage(), searchlist.get(position).getOwnerid());
        }
        SearchsetupBadge();

        MainsetupBadge();
        mycheckList.add(searchlist.get(position).getSimplePID());
        SaveCheckData(position);
        preferenceList.add(new CatLvlItemList(searchlist.get(position).getP_name(), searchlist.get(position).getP_price(), searchlist.get(position).getP_quantity(), searchlist.get(position).getP_img(), position, searchlist.get(position).getProductid(), searchlist.get(position).getStoreId(), searchlist.get(position).getActual_price(), searchlist.get(position).getSimplePID(), searchlist.get(position).getDesc(), searchlist.get(position).getCat_id()));
        SaveCartData(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return searchlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImage, pWish;
        private TextView pName, pPrice, pDesc;
        private Button mbtn_add_cart, mbtn_remove_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.product_image);
            pDesc = itemView.findViewById(R.id.product_desc);
            pName = itemView.findViewById(R.id.product_title);
            pPrice = itemView.findViewById(R.id.product_price);
            pWish = itemView.findViewById(R.id.wis);
            mbtn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            mbtn_remove_cart = itemView.findViewById(R.id.btn_remove_cart);


        }
    }

    private void SaveCheckData(int pos) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(searchlist.get(pos).getStoreId() + "Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData(int pos) {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(searchlist.get(pos).getStoreId() + "Checkcart", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("checklist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            mycheckList = gson.fromJson(json, type);

            if (mycheckList == null) {
                mycheckList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (mContext != null) {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void SaveCartData(int pos) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(searchlist.get(pos).getStoreId() + "" + searchlist.get(pos).getOwnername(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }


    private void GetCartData(int pos) {
        try {

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(searchlist.get(pos).getStoreId() + "" + searchlist.get(pos).getOwnername(), MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cartlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            preferenceList = gson.fromJson(json, type);

            if (preferenceList == null) {
                preferenceList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (mContext != null) {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
