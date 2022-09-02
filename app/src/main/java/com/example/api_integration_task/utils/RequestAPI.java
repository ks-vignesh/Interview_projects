package com.example.api_integration_task.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.api_integration_task.APIIntegrationTask;
import com.example.api_integration_task.R;
import com.example.api_integration_task.interfaces.MasterInterface;


import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;


public class RequestAPI implements MasterInterface.VolleyRequest {

    private MasterInterface.VolleyResult mVolley = null;
    private Context mContext;
    private Filler mFiller;
    private APIIntegrationTask emailIntegration;


    public RequestAPI(Context context, Object mInterface) {
        mContext = context;
        if (mInterface instanceof MasterInterface.VolleyResult) {
            mVolley = (MasterInterface.VolleyResult) mInterface;
        }
        mFiller = new Filler(mContext);
        emailIntegration = APIIntegrationTask.getInstance();

    }

    @Override
    public void cancelPendingRequests() {
        if (!emailIntegration.getvolleyArray().isEmpty()) {
            for (int i = 0; i < emailIntegration.getvolleyArray().size(); i++) {
                emailIntegration.getRequestQueue().cancelAll(emailIntegration.getvolleyArray().get(i));
            }
        }
    }

    private void finishDialog(Dialog dialog) {
        ((AppCompatActivity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void getRequest(String requestUrl, JSONObject headerObject) {
        makeJsonObjectReq(requestUrl, Constants.RestApi.GET, headerObject, null);
    }

    public void makeJsonObjectReq(final String url, int method, final JSONObject headerObject, final JSONObject bodyObject) {

        ((AppCompatActivity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (method == 1) {
            method = Constants.RestApi.POST;
        } else {
            method = Constants.RestApi.GET;
        }

        if (window != null) {
            ColorDrawable colorDrawable = new ColorDrawable(android.graphics.Color.TRANSPARENT);
            window.setBackgroundDrawable(colorDrawable);
        }
        int finalMethod = method;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                emailIntegration.getvolleyArray().remove(url);
                finishDialog(dialog);
                String s = url;
                Timber.i("Response For " + s + ": " + response.toString());
                if (mVolley != null) {
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put ("data",response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mVolley.volleySuccess(url, url, jsonObj);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finishDialog(dialog);
                NetworkResponse networkResponse = error.networkResponse;
                Timber.e("Vollery Error", error);
                if (networkResponse != null && networkResponse.data != null) {
                    byte[] data = networkResponse.data;
                    String message = new String(data);
                    try {
                        emailIntegration.getvolleyArray().remove(url);
                        if (mVolley != null) {
                            JSONObject errorJsonObj = new JSONObject(message);
                            Timber.e("URL: " + url + "\nvolleyError:" + errorJsonObj);
                            mVolley.volleySuccess(url, url, errorJsonObj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Timber.e("Volley Error :", e);
                    }
                } else {
                     mFiller.showSnackBar("Server Not Found");
                }
            }
        }) {
            public int getMethod() {
                return finalMethod;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(Constants.RestApi.REST_RETRY_MILLISECONDS, Constants.RestApi.REST_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        emailIntegration.addToRequestQueue(req, url);
        emailIntegration.getvolleyArray().add(url);

    }

}
