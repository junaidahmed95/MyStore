package com.bringo.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(mUser!=null){
                        startActivity(new Intent(SplashActivity.this, BringoActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, Verification.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }

                }
            }
        };

        thread.start();

    }
}
