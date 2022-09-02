package com.example.api_integration_task.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.api_integration_task.APIIntegrationTask;
import com.google.android.material.snackbar.Snackbar;


public class Filler {

    private Context mContext;
    public Filler(Context context) {
        this.mContext = context;
    }

    public static boolean isObjEmpty(Object object) {
        return object == null || object.toString().isEmpty();
    }

    public void showSnackBar(String message) {
        if (mContext instanceof Activity) {
            View view = ((Activity) mContext).findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}

