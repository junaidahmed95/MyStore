package com.bringo.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    public static HelpingMethods helpingMethods;
    Button msignupBtn;
    private TextView mUserName,mtotalAmount;
    private LinearLayout minLayout, moutLayout;
    private String sName, sImage;
    public static TextView textCartItemCount;
    private CircleImageView mUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        helpingMethods = new HelpingMethods(MainActivity.this);
        if (getIntent().getStringExtra("cart") != null) {
            OpencartActivity();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        mtotalAmount  =findViewById(R.id.totalAmount);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_bringo);
        minLayout = navHeaderView.findViewById(R.id.inLayout);
        moutLayout = navHeaderView.findViewById(R.id.outLayout);
        msignupBtn = navHeaderView.findViewById(R.id.signupBtn);
        mUserImage = navHeaderView.findViewById(R.id.userImage);
        mUserName = navHeaderView.findViewById(R.id.userName);
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
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
        } else {
            moutLayout.setVisibility(View.VISIBLE);
            msignupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, Verification.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenProFile();
            }
        });

        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenProFile();
            }
        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_wishlist, R.id.nav_wallet, R.id.nav_paymen, R.id.nav_messager, R.id.nav_order, R.id.nav_hist, R.id.nav_contact, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//
//        navigationView.getMenu().getItem(7).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
//                    drawer.closeDrawer(GravityCompat.START);
//                    ConnectionDetector detector = new ConnectionDetector(MainActivity.this);
//                    if (detector.isConnected()) {
//                        startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    } else {
//                        Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    drawer.closeDrawer(GravityCompat.START);
//                    startActivity(new Intent(MainActivity.this, Verification.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                }
//                return false;
//            }
//        });

        navigationView.getMenu().getItem(8).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
                    drawer.closeDrawer(GravityCompat.START);
                    ConnectionDetector detector = new ConnectionDetector(MainActivity.this);
                    if (detector.isConnected()) {
                        Intent mintent = new Intent(MainActivity.this, MessagingActivity.class);
                        mintent.putExtra("user_id", "BrwELMpWZEa057zF77OZdTx63k23");
                        mintent.putExtra("uName", sName);
                        mintent.putExtra("uImage", sImage);
                        mintent.putExtra("check", "one");
                        mintent.putExtra("forward", "one");
                        startActivity(mintent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, Verification.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                return false;
            }
        });

    }

    private void OpenProFile() {
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, Verification.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void OpencartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("StID", helpingMethods.GetStoreID());
        intent.putExtra("catName", "");
        intent.putExtra("for", "finish");
        intent.putExtra("stname", helpingMethods.GetStoreName());
        intent.putExtra("ownerID", helpingMethods.GetStoreUID());
        intent.putExtra("ownerImage", helpingMethods.GetStoreImage());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bringo, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpingMethods.GetCartCount(helpingMethods.GetStoreID()) > 0) {
                    OpencartActivity();
                }
            }
        });
        MainsetupBadge();
        return true;
    }


    public static void MainsetupBadge() {
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


    @Override
    protected void onResume() {
        super.onResume();
        if(helpingMethods.GetCartTotal(helpingMethods.GetStoreID())>0){
            mtotalAmount.setText("Rs."+helpingMethods.GetCartTotal(helpingMethods.GetStoreID())+"/-");
            mtotalAmount.setVisibility(View.VISIBLE);
        }else {
            mtotalAmount.setVisibility(View.GONE);
        }
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            minLayout.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).asBitmap().load(helpingMethods.GetUImage()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mUserImage);
            mUserName.setText(helpingMethods.GetUName().toUpperCase());
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
