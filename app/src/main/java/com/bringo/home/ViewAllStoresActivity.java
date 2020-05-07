package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bringo.home.Adapter.StoresAdapter;
import com.bringo.home.Model.HelpingMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.w3c.dom.Text;

import static com.bringo.home.ui.home.HomeFragment.nearesStoresList;

public class ViewAllStoresActivity extends AppCompatActivity {

    private RecyclerView mallStoresRecyclerView;
    private ImageButton mbtnBack;
    private HelpingMethods helpingMethods;
    private TextView textCartItemCount;
    private ImageView mbasketImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_stores);
        helpingMethods  =new HelpingMethods(this);
        mbtnBack = findViewById(R.id.btnBack);
        textCartItemCount = findViewById(R.id.cart_badge);
        mbasketImageView = findViewById(R.id.basketImageView);

        mbasketImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helpingMethods.GetCartCount(helpingMethods.GetStoreID())>0){
                    Intent intent = new Intent(ViewAllStoresActivity.this, CartActivity.class);
                    intent.putExtra("StID", helpingMethods.GetStoreID());
                    intent.putExtra("catName", "");
                    intent.putExtra("for", "finish");
                    intent.putExtra("stname", helpingMethods.GetStoreName());
                    intent.putExtra("ownerID", helpingMethods.GetStoreUID());
                    intent.putExtra("ownerImage", helpingMethods.GetStoreImage());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });

        mallStoresRecyclerView = findViewById(R.id.allStoresRecyclerView);
        mallStoresRecyclerView.setLayoutManager(new GridLayoutManager(ViewAllStoresActivity.this,3));
        StoresAdapter storesAdapter = new StoresAdapter(nearesStoresList,ViewAllStoresActivity.this,true);
        mallStoresRecyclerView.setAdapter(storesAdapter);
        storesAdapter.notifyDataSetChanged();

        mbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BadgeCounter();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void BadgeCounter() {
        if (helpingMethods.GetStoreID() != null) {
            if (helpingMethods.GetCartCount(helpingMethods.GetStoreID()) == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText("" + helpingMethods.GetCartCount(helpingMethods.GetStoreID()));
                //textCartItemCount.setText(""+2);
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }

            }

        }
    }


}
