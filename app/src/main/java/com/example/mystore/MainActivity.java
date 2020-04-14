package com.example.mystore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore.Model.CatLvlItemList;
import com.example.mystore.ui.cart.CartFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private String lol = "ass";

    TextView mtxt_voice;
    public static boolean flagfroprice = true;
    public static TextView textCartItemCount;
    public static int mCartItemCount = 0;


    public static List<CatLvlItemList> favlist = new ArrayList<>();
    public static List<String> checklist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        Toolbar toolbar = findViewById(R.id.appBar);
        toolbar.setTitle("Corner Store");
        setSupportActionBar(toolbar);

        String mForcart=getIntent().getStringExtra("forcart");

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

       if (mForcart != null || !lol.equals("")){

         if (mForcart != null){
             AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                     R.id.navigation_account)
                     .build();
             NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
             NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
             NavigationUI.setupWithNavController(navView, navController);
         }
         else{
             AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                     R.id.navigation_home, R.id.navigation_messages, R.id.navigation_cart, R.id.navigation_account)
                     .build();
             NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
             NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
             NavigationUI.setupWithNavController(navView, navController);
         }

       }







    }











}
