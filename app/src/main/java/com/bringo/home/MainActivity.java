package com.bringo.home;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

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

import java.text.DecimalFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bringo.home.ui.home.HomeFragment.nearesStoresList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private String storeID, storeName;
    private static TextView mtotalAmount;
    private ValueEventListener checkrateListener, valueEventListener;
    public static HelpingMethods helpingMethods;
    Button msignupBtn;
    private Intent vActivityIntent;
    private TextView mUserName, mUserEmail;
    private LinearLayout minLayout, moutLayout;
    private String sName, sImage;
    private ImageView msearchMul;
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
        vActivityIntent = new Intent(this, Verification.class);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = findViewById(R.id.toolbar);
        msearchMul= findViewById(R.id.searchMul);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);


        checkrateListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        storeID = snapshot.getKey();
                        if (snapshot.child("st_name").exists()) {
                            storeName = snapshot.child("st_name").getValue().toString();
                        }

                        break;
                    }
                    showRatingDialog();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mtotalAmount = findViewById(R.id.totalAmount);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_bringo);
        minLayout = navHeaderView.findViewById(R.id.inLayout);
        moutLayout = navHeaderView.findViewById(R.id.outLayout);
        msignupBtn = navHeaderView.findViewById(R.id.signupBtn);
        mUserEmail = navHeaderView.findViewById(R.id.userEmail);
        mUserImage = navHeaderView.findViewById(R.id.userImage);
        mUserName = navHeaderView.findViewById(R.id.userName);

        mtotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpencartActivity();
            }
        });


        msearchMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("search","mulstore");
                startActivity(intent);
            }
        });

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
                    startActivity(vActivityIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawer.closeDrawer(GravityCompat.START);
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


        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            final TextView view = (TextView) navigationView.getMenu().findItem(R.id.nav_order).getActionView();
            FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        view.setText("" + dataSnapshot.getChildrenCount());
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

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
        UpdatePrice();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            minLayout.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).asBitmap().load(helpingMethods.GetUImage()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mUserImage);
            mUserName.setText(helpingMethods.GetUName().toUpperCase());
            if (helpingMethods.GetUEmail() != null) {
                if (!helpingMethods.GetUEmail().equals("")) {
                    if (!helpingMethods.GetUEmail().equals("null")) {
                        mUserEmail.setText(helpingMethods.GetUEmail());
                        mUserEmail.setVisibility(View.VISIBLE);
                    }
                }
            }
            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(checkrateListener);
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(0);
        }

    }

    public static void UpdatePrice() {
        if (helpingMethods.newone(helpingMethods.GetStoreID()) > 0) {
            mtotalAmount.setText("Rs." + helpingMethods.newone(helpingMethods.GetStoreID()) + "/-");
            mtotalAmount.setVisibility(View.VISIBLE);
        } else {
            mtotalAmount.setVisibility(View.GONE);
        }
    }

    private void showRatingDialog() {
        final Dialog dialogRating = new Dialog(MainActivity.this);
        dialogRating.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRating.setContentView(R.layout.rating_dialog);
        ImageView buttonClose = dialogRating.findViewById(R.id.imageView_close);
        RatingBar ratingBar = dialogRating.findViewById(R.id.ratingBar);
        TextView mstNamae = dialogRating.findViewById(R.id.stNamae);
        mstNamae.setText("Please rate your order from " + storeName);
        dialogRating.show();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                final DatabaseReference driverDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(storeID);
                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshotDriver) {
                        driverDatabaseReference.removeEventListener(valueEventListener);
                        if (dataSnapshotDriver.exists()) {
                            Double over_rating = 0.0, count = 0.0;
                            over_rating = Double.parseDouble(dataSnapshotDriver.child("rating").getValue().toString());
                            count = Double.parseDouble(dataSnapshotDriver.child("count").getValue().toString());
                            Double result = ((over_rating * count) + rating) / (count + 1);
                            DecimalFormat df = new DecimalFormat("#.#");
                            result = Double.parseDouble(df.format(result));
                            driverDatabaseReference.child("count").setValue(++count);
                            driverDatabaseReference.child("rating").setValue(result);
                            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).child(storeID).removeValue();
                            dialogRating.dismiss();
                            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).removeEventListener(checkrateListener);
                        } else {
                            HashMap<String, Double> hashMap = new HashMap();
                            hashMap.put("rating", 0.0);
                            hashMap.put("count", 0.0);
                            driverDatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (dataSnapshotDriver.child("rating").exists()) {
                                            Double over_rating = 0.0, count = 0.0;
                                            over_rating = Double.parseDouble(dataSnapshotDriver.child("rating").getValue().toString());
                                            count = Double.parseDouble(dataSnapshotDriver.child("count").getValue().toString());
                                            Double result = ((over_rating * count) + rating) / (count + 1);
                                            DecimalFormat df = new DecimalFormat("#.#");
                                            result = Double.parseDouble(df.format(result));
                                            driverDatabaseReference.child("count").setValue(++count);
                                            driverDatabaseReference.child("rating").setValue(result);
                                            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).child(storeID).removeValue();

                                        } else {
                                            dialogRating.dismiss();
                                        }
                                    }
                                    dialogRating.dismiss();
                                    FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).removeEventListener(checkrateListener);
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                driverDatabaseReference.addListenerForSingleValueEvent(valueEventListener);


            }
        });


        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRating.cancel();
                FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).removeEventListener(checkrateListener);
                FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).child(storeID).removeValue();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).removeEventListener(checkrateListener);
            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName() != null) {
            FirebaseDatabase.getInstance().getReference("HasRate").child(FirebaseAuth.getInstance().getUid()).removeEventListener(checkrateListener);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
