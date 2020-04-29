package com.bringo.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bringo.home.Model.HelpingMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class BringoActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private HelpingMethods helpingMethods;
    private String sName, sImage;
    public static TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bringo);

        helpingMethods = new HelpingMethods(BringoActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_bringo);

        final CircleImageView mUserImage = navHeaderView.findViewById(R.id.userImage);
        final TextView mUserName = navHeaderView.findViewById(R.id.userName);
        if (FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("picture").getValue().toString()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)).into(mUserImage);
                        mUserName.setText(dataSnapshot.child("name").getValue().toString().toUpperCase());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("token").setValue(FirebaseInstanceId.getInstance().getToken());

            FirebaseDatabase.getInstance().getReference("Users").child("Owners").child("BrwELMpWZEa057zF77OZdTx63k23").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sName = dataSnapshot.child("full_name").getValue().toString();
                        sImage = dataSnapshot.child("picture").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BringoActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BringoActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be sidered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_wishlist, R.id.nav_wallet, R.id.nav_paymen, R.id.nav_messager
                , R.id.nav_hist, R.id.nav_order, R.id.nav_setting, R.id.nav_contact, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(8).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mintent = new Intent(BringoActivity.this, MessagingActivity.class);
                mintent.putExtra("user_id", "BrwELMpWZEa057zF77OZdTx63k23");
                mintent.putExtra("uName", sName);
                mintent.putExtra("uImage", sImage);
                mintent.putExtra("check", "one");
                mintent.putExtra("forward", "one");
                startActivity(mintent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bringo, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
