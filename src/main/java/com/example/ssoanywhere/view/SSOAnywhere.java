package com.example.ssoanywhere.view;


import android.content.Context;

import com.example.ssoanywhere.controller.AutherizedResponseInterface;
import com.example.ssoanywhere.model.SessionDataManager;
import com.example.ssoanywhere.model.Volley_Authorize;
import com.example.ssoanywhere.model.Volley_Refresh;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SSOAnywhere extends SessionDataManager {

    private AutherizedResponseInterface autherizedResponseInterface;
    private Context context;

    public SSOAnywhere(Context context, String clientId, String clientSecret, AutherizedResponseInterface autherizedResponseInterface) {
        super(context);
        this.context = context;
        setClientId(clientId);
        setClientSecret(clientSecret);
        this.autherizedResponseInterface = autherizedResponseInterface;
    }

    public void callingAutherizeAPI(String userName, String password) {
        new Volley_Authorize(context, userName, password, autherizedResponseInterface, getClientId(), getClientSecret()).authorizeAPICall();
    }

    public void callingRefreshTokenAPI() {
        new Volley_Refresh(context, getRefreshToken(), autherizedResponseInterface, getClientId(), getClientSecret()).refreshTokenAPICall();
    }

    public void setSSOSharedPreference(String jsonResponse) {
        long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        try {
            init_TokenResponseSessionData(jsonResponse);
            if (!jsonResponse.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String expireTime = jsonObject.getString("expires_in");
                long expireSeconds = currentSeconds + Long.valueOf(expireTime);
                setAccessToken(jsonObject.getString("access_token"));
                setRefreshToken(jsonObject.getString("refresh_token"));
                setExpiredTime(expireSeconds);
            } else {
                setAccessToken("");
                setRefreshToken("");
                setExpiredTime(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getSessionData() {
        ArrayList<String> sessionDataList = new ArrayList<>();
        sessionDataList.add(0, String.valueOf(getExpiredTime()));
        sessionDataList.add(1, getAccessToken());
        sessionDataList.add(2, getTokenresponse());
        return sessionDataList;
    }

}
