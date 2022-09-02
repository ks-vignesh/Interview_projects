package com.example.api_integration_task.interfaces;


import org.json.JSONArray;
import org.json.JSONObject;

public interface MasterInterface {

    interface VolleyResult {
        void volleySuccess(String flag, String requestUrl, JSONObject response);

        void volleyError(String flag, String requestUrl, JSONArray error);

        void offline(String flag, String requestUrl);
    }

    interface VolleyRequest {

        void getRequest(String requestUrl, JSONObject headerObject);

        void cancelPendingRequests();

    }


}
