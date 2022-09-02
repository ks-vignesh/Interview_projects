package com.example.api_integration_task.screens;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.api_integration_task.R;
import com.example.api_integration_task.interfaces.MasterInterface;
import com.example.api_integration_task.utils.Constants;
import com.example.api_integration_task.utils.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MasterInterface.VolleyResult {

    TextView emailID1, emailID2, emailID3;
    private MasterInterface.VolleyRequest request;
    private Context mContext;
    int counter = 0;
    int[] apiPageRoutes = new int[]{1, 3, 10};

    // ArrayList<Integer> apiPageRoutes = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailID1 = (TextView) findViewById(R.id.emailAddress1);
        emailID2 = (TextView) findViewById(R.id.emailAddress2);
        emailID3 = (TextView) findViewById(R.id.emailAddress3);
        mContext = this;

        getRequestforUserEmailAddress();
    }

    public void getRequestforUserEmailAddress() {
        Timber.i("API Call Happen");
        JSONObject jsonObject = new JSONObject();
        String URL = Constants.BASE_URL + apiPageRoutes[counter];
        Timber.i("API Call URL =" + URL.toString());
        request = new RequestAPI(mContext, this);
        request.getRequest(URL, jsonObject);
    }

    @Override
    public void volleySuccess(String flag, String requestUrl, JSONObject response) {
        try {
            JSONObject responseObj = response.getJSONObject(Constants.DATA).getJSONObject(Constants.DATA);
            String emailAddress = responseObj.getString(Constants.EMAIL);
            if (counter == 0) {
                emailID1.setText(emailAddress.toString());
            } else if (counter == 1) {
                emailID2.setText(emailAddress.toString());
            } else if (counter == 2) {
                emailID3.setText(emailAddress.toString());
            } else {
                Timber.e("Error on setting values to Textview");
            }
            counter++;
            if (counter < 3) {
                getRequestforUserEmailAddress();
            }

        } catch (Exception e) {
            Timber.e("Exception on Volley Success", e.getMessage());
        }
    }

    @Override
    public void volleyError(String flag, String requestUrl, JSONArray error) {
        Timber.i("Volley Error" + error.toString());
    }

    @Override
    public void offline(String flag, String requestUrl) {

    }
}