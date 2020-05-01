package com.bringo.home.Model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandlerSingleten {


    private static RequestHandlerSingleten sInstance;
    private static Context sContext;

    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;

    private RequestHandlerSingleten(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestHandlerSingleten getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RequestHandlerSingleten(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


}