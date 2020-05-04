package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bringo.home.Adapter.StoresAdapter;

import static com.bringo.home.ui.home.HomeFragment.nearesStoresList;

public class ViewAllStoresActivity extends AppCompatActivity {

    private RecyclerView mallStoresRecyclerView;
    private ImageButton mbtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_stores);
        mbtnBack = findViewById(R.id.btnBack);
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

}
