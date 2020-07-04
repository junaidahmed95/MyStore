package com.bringo.home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bringo.home.MainCatActivity;
import com.bringo.home.Model.Categories;
import com.bringo.home.Model.Category;
import com.bringo.home.Model.Fruit;
import com.bringo.home.Model.Listmmh;
import com.bringo.home.R;
import com.bringo.home.StoreInfoActivity;
import com.bringo.home.SubCatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;


import java.util.List;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    @NonNull

    //private List<Category> list;
    private List<Categories> list1;
    private List<Fruit> fruitList;
    private List<Fruit> veglist;
    private List<Listmmh> listmmhList;
    private  String val ="";
    private Context mcontext;
    private Activity activity;


    public MainCategoryAdapter(List<Categories> list1,List<Listmmh> Listmmh,List<Fruit> fruitList,List<Fruit> veglist, Context mcontext,String sa) {
        this.mcontext = mcontext;
        val=sa;
        if (sa.equals("1")){
            this.list1 = list1;
        }
        else if(sa.equals("2")){
            this.listmmhList = Listmmh;
        } else if(sa.equals("3")) {
            this.fruitList = fruitList;
        }


    }




    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maincatitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(val.equals("1")){
            holder.mtxt.setText(list1.get(position).getM_name());
            Glide.with(mcontext).asBitmap().load(list1.get(position).getThumbnail()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).dontAnimate().into(holder.mimg);
            holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, MainCatActivity.class);
                    intent.putExtra("cat_id",list1.get(position).getId());
                    intent.putExtra("store_name", list1.get(position).getM_name());
                    mcontext.startActivity(intent);
                }
            });
        }else if (val.equals("2")){

            holder.mtxt.setText(listmmhList.get(position).getProduct_name());
            //Picasso.get().load("http://bringo.biz/public/img/cat_a/electronic3.jpg").into(holder.mimg);
            Glide.with(mcontext).asBitmap().load(listmmhList.get(position).getProduct_image()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).dontAnimate().into(holder.mimg);
//            holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mcontext, MainCatActivity.class);
//                    intent.putExtra("cat_id",listmmhList.get(position).getP_id());
//                    mcontext.startActivity(intent);

            holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, SubCatActivity.class);
                    intent.putExtra("storeid", listmmhList.get(position).getStr_id());
                    intent.putExtra("catName", listmmhList.get(position).getM_name());
                    intent.putExtra("stname",listmmhList.get(position).getStr_name());
                    intent.putExtra("ownerID",listmmhList.get(position).getU_id());
                    intent.putExtra("ownerImage",listmmhList.get(position).getThumbnail());
                    mcontext.startActivity(intent);
//                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });


        }else if (val.equals("3")){
            holder.mtxt.setText(fruitList.get(position).getTitle());
            //Picasso.get().load("http://bringo.biz/public/img/cat_a/electronic3.jpg").into(holder.mimg);
            Glide.with(mcontext).asBitmap().load(fruitList.get(position).getThumbnail()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).dontAnimate().into(holder.mimg);
            holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, MainCatActivity.class);
                    intent.putExtra("cat_id",fruitList.get(position).getCat_a());
                    intent.putExtra("store_name",fruitList.get(position).getTitle());
                    mcontext.startActivity(intent);
                }
            });
        }else if (val.equals("4")){
            holder.mtxt.setText(veglist.get(position).getTitle());
            //Picasso.get().load("http://bringo.biz/public/img/cat_a/electronic3.jpg").into(holder.mimg);
            Glide.with(mcontext).asBitmap().load(veglist.get(position).getThumbnail()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).dontAnimate().into(holder.mimg);
            holder.mcat_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, MainCatActivity.class);
                    intent.putExtra("cat_id",veglist.get(position).getCat_a());
                    intent.putExtra("store_name",veglist.get(position).getTitle());
                    mcontext.startActivity(intent);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
       if(val.equals("1")){
           return list1.size();
       }else if (val.equals("2")){
           return listmmhList.size();
       }else if (val.equals("3")){
           return fruitList.size();
       } else {
           return 0;
       }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtxt;
        ImageView mimg;
        LinearLayout mcat_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mimg = itemView.findViewById(R.id.img);
            mtxt = itemView.findViewById(R.id.txt);
            mcat_layout = itemView.findViewById(R.id.cat_layout);
        }
    }
}
