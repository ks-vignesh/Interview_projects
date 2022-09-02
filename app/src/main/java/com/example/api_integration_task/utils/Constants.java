package com.example.api_integration_task.utils;

import com.android.volley.Request;

public final class Constants {
    public static final String BASE_URL = "https://reqres.in/api/users/";
    public static final String MOBILE = "MOBILE";
    public static final String WIFI = "WIFI";
    public static final String RETRY = "Retry";

    public class RestApi {
        public static final int GET = Request.Method.GET;
        public static final int POST = Request.Method.POST;
        public static final int REST_RETRY = 0;
        public static final int REST_RETRY_MILLISECONDS = 15 * 1000;

    }

    //general Keys
    public static final String DATA = "data";
    public static final String EMAIL = "email";

}
