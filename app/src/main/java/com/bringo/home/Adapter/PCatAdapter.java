package com.bringo.home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.ProductDetailActivity;
import com.bringo.home.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bringo.home.SubCatActivity.setupBadge;

public class PCatAdapter extends RecyclerView.Adapter<PCatAdapter.ViewHolder> {

    private List<CatLvlItemList> proLists;
    private List<CatLvlItemList> myFavList;
    private Context mContext;
    Sprite doubleBounce;
    private boolean flag = true;
    private List<String> myWishList;
    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private String sID, ownerID, ownerImage, ownerName,catName;
    private HelpingMethods helpingMethods;

    public PCatAdapter(List<CatLvlItemList> proLists, Context mContext, String sID, String ownerID, String ownerImage, String ownerName,String catName) {
        this.proLists = proLists;
        this.catName=catName;
        this.mContext = mContext;
        this.sID = sID;
        this.ownerID = ownerID;
        this.ownerImage = ownerImage;
        this.ownerName = ownerName;
        GetWishData();
        GetFavData();
        GetCheckData();
        GetCartData();
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
        Glide.with(mContext).asBitmap().load(proLists.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.logo)).into(holder.pImage);
        holder.pName.setText(proLists.get(position).getP_name());
        holder.pDesc.setText(proLists.get(position).getDesc());
        holder.pPrice.setText("" + proLists.get(position).getP_price());
        try {
            if (mycheckList.size() > 0) {
                if (mycheckList.contains(proLists.get(position).getSimplePID())) {
                    holder.mbtn_add_cart.hide();
                    holder.mbtn_remove_cart.show();
                } else {
                    holder.mbtn_remove_cart.hide();
                    holder.mbtn_add_cart.show();
                }
            }

            if (myWishList.size() > 0) {
                if (myWishList.contains(proLists.get(position).getSimplePID())) {
                    holder.pWish.setImageResource(R.drawable.ic_action_fav);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorRed)));
                } else {
                    holder.pWish.setImageResource(R.drawable.ic_like);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.gray)));
                }
            }

        } catch (Exception e) {

        }

        holder.pWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myWishList.contains(proLists.get(position).getSimplePID())) {
                    holder.pWish.setImageResource(R.drawable.ic_like);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.gray)));
                    int a = myWishList.indexOf(proLists.get(position).getSimplePID());
                    myWishList.remove(a);
                    SaveWishData();
                    myFavList.remove(a);
                    SaveFavData();
                } else {
                    holder.pWish.setImageResource(R.drawable.ic_action_fav);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorRed)));
                    myWishList.add(proLists.get(position).getSimplePID());
                    SaveWishData();
                    myFavList.add(new CatLvlItemList(proLists.get(position).getP_name(), proLists.get(position).getP_price(), proLists.get(position).getP_quantity(), proLists.get(position).getP_img(), position,proLists.get(position).getProductid(), proLists.get(position).getStoreId(),proLists.get(position).getActual_price(),proLists.get(position).getSimplePID()));
                    SaveFavData();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("image", proLists.get(position).getP_img());
                intent.putExtra("proLists", position);
                intent.putExtra("sID", proLists.get(position).getStoreId());
                intent.putExtra("pID", proLists.get(position).getProductid());
                intent.putExtra("spID", proLists.get(position).getSimplePID());
                intent.putExtra("oName", ownerName);
                intent.putExtra("catName",catName);
                intent.putExtra("oImage", ownerImage);
                intent.putExtra("oID", ownerID);
                intent.putExtra("name", proLists.get(position).getP_name());
                intent.putExtra("price", proLists.get(position).getP_price());
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) mContext).finish();
            }
        });

        holder.mbtn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mycheckList.contains(proLists.get(position).getSimplePID())) {
                    holder.mbtn_add_cart.hide();
                    holder.mbtn_remove_cart.show();
                    int finalCount = helpingMethods.GetCartCount(proLists.get(position).getStoreId()) + 1;
                    helpingMethods.SaveCartCount(finalCount, proLists.get(position).getStoreId());
                    setupBadge();
                    if (helpingMethods.GetStoreID() != null) {
                        if (!helpingMethods.GetStoreID().equals(proLists.get(position).getStoreId())) {
                            helpingMethods.SaveStoreData(sID, ownerName, ownerImage, ownerID);
                            mycheckList.clear();
                            SaveCheckData();
                            preferenceList.clear();
                            SaveCartData();
                        }
                    } else {
                        helpingMethods.SaveStoreData(sID, ownerName, ownerImage, ownerID);
                    }

                    mycheckList.add(proLists.get(position).getSimplePID());
                    SaveCheckData();
                    //String p_name, String p_price, String p_quantity, String p_img, int pos, String productid, String storeId,String actual_price
                    preferenceList.add(new CatLvlItemList(proLists.get(position).getP_name(), proLists.get(position).getP_price(), proLists.get(position).getP_quantity(), proLists.get(position).getP_img(), position, proLists.get(position).getProductid(), proLists.get(position).getStoreId(),proLists.get(position).getActual_price(),proLists.get(position).getSimplePID()));
                    SaveCartData();
                    notifyDataSetChanged();
                }

            }
        });

        holder.mbtn_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mycheckList.contains(proLists.get(position).getSimplePID())) {
                    holder.mbtn_remove_cart.hide();
                    holder.mbtn_add_cart.show();
                    int a = mycheckList.indexOf(proLists.get(position).getSimplePID());
                    mycheckList.remove(a);
                    SaveCheckData();
                    preferenceList.remove(a);
                    SaveCartData();
                    notifyDataSetChanged();
                    int finalCount = helpingMethods.GetCartCount(proLists.get(position).getStoreId()) - 1;
                    helpingMethods.SaveCartCount(finalCount, proLists.get(position).getStoreId());
                    setupBadge();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return proLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pImage, pWish;
        private TextView pName, pPrice,pDesc;
        private FloatingActionButton mbtn_add_cart, mbtn_remove_cart;

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


    private void SaveFavData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Myfav", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myFavList);
        editor.putString("favlist", json);
        editor.apply();
    }


    private void GetFavData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Myfav", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("favlist", null);
            Type type = new TypeToken<ArrayList<CatLvlItemList>>() {
            }.getType();
            myFavList = gson.fromJson(json, type);

            if (myFavList == null) {
                myFavList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (mContext != null) {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveWishData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mywish", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myWishList);
        editor.putString("wishlist", json);
        editor.apply();
    }


    private void GetWishData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Mywish", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("wishlist", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            myWishList = gson.fromJson(json, type);

            if (myWishList == null) {
                myWishList = new ArrayList<>();
            }


        } catch (Exception e) {
            if (mContext != null) {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void SaveCheckData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
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
            if (mContext != null) {
                Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}


