package com.bringo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends BaseAdapter {

   List<GirdListView>list;
      Context context;

    public MainAdapter(List<GirdListView> list, Context context) {
        this.list = list;
        this.context = context;
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.griddvieww,parent,false);

        ImageView i = convertView.findViewById(R.id.image_view);
        TextView t1 = convertView.findViewById(R.id.txt1);
        TextView t2 = convertView.findViewById(R.id.txt2);
        TextView t3 = convertView.findViewById(R.id.txt3);




         i.setImageResource(list.get(position).getImg());
         t1.setText(list.get(position).getTxtv1());
         t2.setText(list.get(position).getTxtv2());
         t3.setText(list.get(position).getTxtv3());




        return convertView;

    }
}
