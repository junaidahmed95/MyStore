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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.SubCatActivity;
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
import static com.bringo.home.MainActivity.MainsetupBadge;
import static com.bringo.home.SearchActivity.SearchsetupBadge;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bringo.home.SubCatActivity.setupBadge;

public class PCatAdapter extends RecyclerView.Adapter<PCatAdapter.ViewHolder> {

    public void filterList(ArrayList<CatLvlItemList> filteredList) {
        subcatproLists = filteredList;
        notifyDataSetChanged();
    }

    public static List<CatLvlItemList> subcatproLists;
    private List<CatLvlItemList> myFavList;
    private Context mContext;
    Sprite doubleBounce;
    private boolean isForSearch;
    private List<String> myWishList;
    private List<String> mycheckList;
    private List<CatLvlItemList> preferenceList;
    private String sID, ownerID, ownerImage, ownerName, catName;
    private HelpingMethods helpingMethods;

    public PCatAdapter(List<CatLvlItemList> proLists, Context mContext, String sID, String ownerID, String ownerImage, String ownerName, String catName, boolean isForSearch) {
        this.subcatproLists = proLists;
        this.isForSearch = isForSearch;
        this.catName = catName;
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
        Glide.with(mContext).asBitmap().load(subcatproLists.get(position).getP_img()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.pImage);
        holder.pName.setText(subcatproLists.get(position).getP_name());
        holder.pDesc.setText(subcatproLists.get(position).getDesc());
        holder.pPrice.setText("" + subcatproLists.get(position).getP_price());
        try {
            if (mycheckList.size() > 0) {
                if (mycheckList.contains(subcatproLists.get(position).getSimplePID())) {
                    holder.mbtn_add_cart.setVisibility(View.GONE);
                    holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
                } else {
                    holder.mbtn_remove_cart.setVisibility(View.GONE);
                    holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                }
            }

            if (myWishList.size() > 0) {
                if (myWishList.contains(subcatproLists.get(position).getSimplePID())) {
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
                if (myWishList.contains(subcatproLists.get(position).getSimplePID())) {
                    holder.pWish.setImageResource(R.drawable.ic_like);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.gray)));
                    int a = myWishList.indexOf(subcatproLists.get(position).getSimplePID());
                    myWishList.remove(a);
                    SaveWishData();
                    myFavList.remove(a);
                    SaveFavData();
                } else {
                    holder.pWish.setImageResource(R.drawable.ic_action_fav);
                    ImageViewCompat.setImageTintList(holder.pWish,
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorRed)));
                    myWishList.add(subcatproLists.get(position).getSimplePID());
                    SaveWishData();
                    myFavList.add(new CatLvlItemList(subcatproLists.get(position).getP_name(), subcatproLists.get(position).getP_price(), subcatproLists.get(position).getP_quantity(), subcatproLists.get(position).getP_img(), position, subcatproLists.get(position).getProductid(), subcatproLists.get(position).getStoreId(), subcatproLists.get(position).getActual_price(), subcatproLists.get(position).getSimplePID()));
                    SaveFavData();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("image", subcatproLists.get(position).getP_img());
                intent.putExtra("proLists", position);
                intent.putExtra("from", "subcart");
                intent.putExtra("sID", subcatproLists.get(position).getStoreId());
                intent.putExtra("isSearch", isForSearch);
                intent.putExtra("pID", subcatproLists.get(position).getProductid());
                intent.putExtra("spID", subcatproLists.get(position).getSimplePID());
                intent.putExtra("desc", subcatproLists.get(position).getDesc());
                intent.putExtra("oName", ownerName);
                intent.putExtra("catName", catName);
                intent.putExtra("oImage", ownerImage);
                intent.putExtra("oID", ownerID);
                intent.putExtra("name", subcatproLists.get(position).getP_name());
                intent.putExtra("price", subcatproLists.get(position).getP_price());
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ((Activity) mContext).finish();
            }
        });

        holder.mbtn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mycheckList.contains(subcatproLists.get(position).getSimplePID())) {
                    holder.mbtn_add_cart.setVisibility(View.GONE);
                    holder.mbtn_remove_cart.setVisibility(View.VISIBLE);
                    int finalCount = helpingMethods.GetCartCount(subcatproLists.get(position).getStoreId()) + 1;
                    helpingMethods.SaveCartCount(finalCount, subcatproLists.get(position).getStoreId());
                    int total = helpingMethods.GetCartTotal(subcatproLists.get(position).getStoreId()) + Integer.parseInt(subcatproLists.get(position).getP_price());
                    helpingMethods.SaveCartTotal(total, subcatproLists.get(position).getStoreId());
                    SubCatActivity.mtotalAmount.setText("Rs." + helpingMethods.GetCartTotal(subcatproLists.get(position).getStoreId()) + "/-");
                    SubCatActivity.mtotalAmount.setVisibility(View.VISIBLE);
                    if (isForSearch) {
                        SearchsetupBadge();
                    } else {
                        setupBadge();
                    }
                    MainsetupBadge();
                    if (helpingMethods.GetStoreID() != null) {
                        if (!helpingMethods.GetStoreID().equals(subcatproLists.get(position).getStoreId())) {
                            helpingMethods.SaveStoreData(subcatproLists.get(position).getStoreId(), ownerName, ownerImage, ownerID);
                            mycheckList.clear();
                            SaveCheckData();
                            preferenceList.clear();
                            SaveCartData();
                        }
                    } else {
                        helpingMethods.SaveStoreData(subcatproLists.get(position).getStoreId(), ownerName, ownerImage, ownerID);
                    }

                    mycheckList.add(subcatproLists.get(position).getSimplePID());
                    SaveCheckData();
                    //String p_name, String p_price, String p_quantity, String p_img, int pos, String productid, String storeId,String actual_price
                    preferenceList.add(new CatLvlItemList(subcatproLists.get(position).getP_name(), subcatproLists.get(position).getP_price(), subcatproLists.get(position).getP_quantity(), subcatproLists.get(position).getP_img(), position, subcatproLists.get(position).getProductid(), subcatproLists.get(position).getStoreId(), subcatproLists.get(position).getActual_price(), subcatproLists.get(position).getSimplePID()));
                    SaveCartData();
                    notifyDataSetChanged();
                }

            }
        });

        holder.mbtn_remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mycheckList.contains(subcatproLists.get(position).getSimplePID())) {
                    holder.mbtn_remove_cart.setVisibility(View.GONE);
                    holder.mbtn_add_cart.setVisibility(View.VISIBLE);
                    int finalCount = helpingMethods.GetCartCount(subcatproLists.get(position).getStoreId()) - 1;
                    helpingMethods.SaveCartCount(finalCount, subcatproLists.get(position).getStoreId());
                    int total = helpingMethods.GetCartTotal(subcatproLists.get(position).getStoreId()) - Integer.parseInt(subcatproLists.get(position).getP_price());
                    helpingMethods.SaveCartTotal(total, subcatproLists.get(position).getStoreId());
                    SubCatActivity.mtotalAmount.setText("Rs." + helpingMethods.GetCartTotal(subcatproLists.get(position).getStoreId()) + "/-");
                    if (isForSearch) {
                        SearchsetupBadge();
                    } else {
                        setupBadge();
                    }
                    MainsetupBadge();

                    int a = mycheckList.indexOf(subcatproLists.get(position).getSimplePID());
                    mycheckList.remove(a);
                    SaveCheckData();
                    preferenceList.remove(a);
                    SaveCartData();
                    notifyDataSetChanged();

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return subcatproLists.size();
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(subcatproLists.get(0).getStoreId()+"Checkcart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mycheckList);
        editor.putString("checklist", json);
        editor.apply();
    }


    private void GetCheckData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(subcatproLists.get(0).getStoreId()+"Checkcart", MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(subcatproLists.get(0).getStoreId()+""+ownerName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(preferenceList);
        editor.putString("cartlist", json);
        editor.apply();
    }


    private void GetCartData() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(subcatproLists.get(0).getStoreId()+""+ownerName, MODE_PRIVATE);
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


