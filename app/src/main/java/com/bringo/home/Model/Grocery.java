package com.bringo.home.Model;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

public class Grocery extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

}
