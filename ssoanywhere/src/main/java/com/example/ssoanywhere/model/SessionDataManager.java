package com.example.ssoanywhere.model;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import java.util.concurrent.TimeUnit;

public class SessionDataManager {
    private static String TOKEN_EXPIRED_TIME = "expiredTime";
    private static String TOKEN_JSONRESPONSE = "accessTokenResponse";
    private static String ACCESSTOKEN = "accessToken";
    private static String REFRESHTOKEN = "refreshToken";
    private static String CLIENTID = "clientId";
    private static String CLIENTSECRET = "clientSecret";

    protected SessionDataManager(Context context) {
        Hawk.init(context).build();
    }
///////setters//////////
    protected void setExpiredTime(long expiredTime) {
        Hawk.put(TOKEN_EXPIRED_TIME, expiredTime);
    }

    protected void setClientId(String clientId) {
        Hawk.put(CLIENTID, clientId);
    }
    protected void setClientSecret(String clientSecret) {
        Hawk.put(CLIENTSECRET, clientSecret);
    }

    private void setTokenResponse(String jsonResponse) {
        Hawk.put(TOKEN_JSONRESPONSE, jsonResponse);
    }
    protected void setAccessToken(String accessToken) {
        Hawk.put(ACCESSTOKEN, accessToken);
    }
    protected void setRefreshToken(String refreshToken) {
        Hawk.put(REFRESHTOKEN, refreshToken);
    }

///////getters//////

    protected long getExpiredTime() {
        return Long.valueOf(Hawk.get(TOKEN_EXPIRED_TIME).toString());
    }
    protected String getClientId() {
        return Hawk.get(CLIENTID, "");
    }


    protected String getClientSecret() {
        return Hawk.get(CLIENTSECRET, "");
    }

    protected String getTokenresponse() {
        return Hawk.get(TOKEN_JSONRESPONSE, "");
    }

    protected String getAccessToken() {
        return Hawk.get(ACCESSTOKEN, "");
    }


    protected String getRefreshToken() {
        return Hawk.get(REFRESHTOKEN, "");
    }
    ///////////other methos//////////


    public boolean isLoggedIn() {
        long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if(checkKeyInSeesion(TOKEN_EXPIRED_TIME)) {
            if (getExpiredTime() == 0 || getExpiredTime() < currentSeconds || getExpiredTime() - currentSeconds == 60)
                return false;
            else
                return true;
        }else{
            return false;
        }
    }

    private boolean checkKeyInSeesion(String key){
        return  (Hawk.contains(key)) ? true : false;
    }

    private void deleteKeyInSeesion(String key){
        if(Hawk.contains(key))
            Hawk.delete(key);
    }

    public void deleteSome(String clientId,String clientSecret) {
        Hawk.put(CLIENTID,clientId);
        Hawk.put(CLIENTSECRET,clientSecret);
        deleteKeyInSeesion(ACCESSTOKEN);
        deleteKeyInSeesion(REFRESHTOKEN);
        deleteKeyInSeesion(TOKEN_EXPIRED_TIME);
        deleteKeyInSeesion(TOKEN_JSONRESPONSE);
    }

    protected String init_TokenResponseSessionData(String jsonResponse){
        if(checkKeyInSeesion(TOKEN_JSONRESPONSE)) {
            if (getTokenresponse().equals(""))
                setTokenResponse(jsonResponse);
            else
                jsonResponse = getTokenresponse();
        }
        else{
            setTokenResponse(jsonResponse);
        }
        return  jsonResponse;
    }

}
