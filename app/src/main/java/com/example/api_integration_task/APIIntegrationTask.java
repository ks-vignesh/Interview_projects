package com.example.api_integration_task;

import android.app.Application;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.text.TextUtils;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.api_integration_task.utils.DataConnectionReceiver;
import com.example.api_integration_task.utils.Filler;

import java.util.ArrayList;


import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class APIIntegrationTask extends Application {
    // private AppLogger logger;
    private static APIIntegrationTask mInstance;
    private ArrayList<String> volleyArray;
    private RequestQueue mRequestQueue;
    private DataConnectionReceiver receiver;

    public synchronized static APIIntegrationTask getInstance() {
        return mInstance;
    }

    public ArrayList<String> getvolleyArray() {
        return volleyArray;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        volleyArray = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new DataConnectionReceiver();
        registerReceiver(receiver, intentFilter);

        //to print the logs in logcat
        Timber.plant(new Timber.DebugTree());
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void removeVollyReq(ArrayList<String> volleyArray) {
        if (!Filler.isObjEmpty(volleyArray)) {
            for (int i = 0; i < volleyArray.size(); i++) {
                getRequestQueue().cancelAll(volleyArray.get(i));
            }
        }
    }
}
