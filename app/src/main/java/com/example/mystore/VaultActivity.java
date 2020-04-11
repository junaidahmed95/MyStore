package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class VaultActivity extends AppCompatActivity {

    private CircleImageView mProfile;
    private TextView mName;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        toolbar = findViewById(R.id.appBar);
        toolbar.setTitle("My Vault");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProfile = findViewById(R.id.userProfile);
        mName = findViewById(R.id.userName);
        Glide.with(getApplicationContext()).asBitmap().load(getIntent().getStringExtra("image")).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mProfile);
        mName.setText(getIntent().getStringExtra("name"));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
