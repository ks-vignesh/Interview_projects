package com.example.api_integration_task.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import timber.log.Timber;

public class DataConnectionReceiver extends BroadcastReceiver {

    private Filler mFiller;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        try {
            mFiller = new Filler(context);
            int connectivityStatus = getConnectivityStatus(context);
            Timber.i("Network State: " + connectivityStatus);
            switch (connectivityStatus) {
                case -1:
                    mFiller.showSnackBar("No Internet Connection");
                    break;
                case 0:
                    Timber.i("ConnectionStatus: " + Constants.MOBILE);

                    break;
                case 1:
                    Timber.i("ConnectionStatus: " + Constants.WIFI);
                    break;
                default:
                    Timber.i("Undefined Connectivity Status: " + connectivityStatus);
                    break;
            }
        } catch (Exception e) {
            Timber.e("DataConnectionReceiver: ", e);
        }
    }

    public int getConnectivityStatus(Context context) throws Exception {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return ConnectivityManager.TYPE_WIFI;
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return ConnectivityManager.TYPE_MOBILE;
            }
        }
        return -1;
    }
}
