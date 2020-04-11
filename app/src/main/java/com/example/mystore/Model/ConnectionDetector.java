package com.example.mystore.Model;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sajid on 6/13/2019.
 */

public class ConnectionDetector extends Activity {
    Context context;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    int check = 0;
    public ConnectionDetector(Context context)
    {
        this.context=context;
    }
    public  boolean isConnected()
    {
        ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity !=null)
        {
            NetworkInfo[] netInfo = connectivity.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }

        }
        return  haveConnectedWifi || haveConnectedMobile;
    }

}
