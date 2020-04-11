package com.example.mystore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Model.AddressClass;
import com.example.mystore.R;

import java.util.List;

import static com.example.mystore.OrderSummaryActivity.refresh;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private int selectedPosition;
    public static String cusaddress;

    List<AddressClass> addressLIST;
    int pre = 0;

    public AddressAdapter(List<AddressClass> address) {
        this.addressLIST = address;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.setData(addressLIST.get(position).isCurrentAddress(),position);

//        holder.madd_checkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                cusaddress = address.get(position).getUserAddress();
//                Toast.makeText(holder.madd_checkb.getContext(), ""+cusaddress, Toast.LENGTH_SHORT).show();
//            }
//        });

//        if (address.get(position).isCurrentAddress()) {
//            holder.madd_checkb.setChecked(true);
//        } else {
//            holder.madd_checkb.setChecked(false);
//        }
//        holder.madd_checkb.setChecked(true);
//
//        holder.madd_checkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                selectedPosition = position;
//                for (int i = 0; i<address.size();i++){
//                    if(selectedPosition!=i){
//                        holder.madd_checkb.setChecked(false);
//                    }else {
//                        holder.madd_checkb.setChecked(true);
//                    }
//
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return addressLIST.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox madd_checkb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            madd_checkb = itemView.findViewById(R.id.add_checkb);
        }

        private void setData(boolean check, final int pos){
            madd_checkb.setText(addressLIST.get(pos).getUserAddress());

            if (check) {
                madd_checkb.setChecked(true);
            } else {
                madd_checkb.setChecked(false);
            }

            madd_checkb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pre != pos) {
                        cusaddress = addressLIST.get(pos).getUserAddress();
                        addressLIST.get(pos).setCurrentAddress(true);
                        addressLIST.get(pre).setCurrentAddress(false);
                        refresh(pre, pos);
                        pre = pos;
                    }
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

}
