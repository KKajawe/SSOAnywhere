package com.example.ssoanywhere.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ssoanywhere.R;
import com.example.ssoanywhere.controller.AutherizedResponseInterface;
import com.example.ssoanywhere.controller.VolleyRequestQueueManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volley_Refresh {
    private AutherizedResponseInterface autherizedResponseInterface;
    private Context context;
    private String clientId, clientSecret, refreshToken;

    public Volley_Refresh(Context context, String refresh_Token, AutherizedResponseInterface autherizedResponseInterface, String clientId, String clientSecret) {
        this.context = context;
        this.autherizedResponseInterface = autherizedResponseInterface;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refresh_Token;
    }

    public void refreshTokenAPICall() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.authorize_refreshTokenUrl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Access_Token = jsonObject.getString("access_token");
                            String Refresh_Token = jsonObject.getString("refresh_token");
                            if (Access_Token.equals("")) {
                                new Volley_Refresh(context, refreshToken, autherizedResponseInterface, clientId, clientSecret).refreshTokenAPICall();
                            } else {
                                autherizedResponseInterface.getResponse(response, true);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            autherizedResponseInterface.getErrorResponse(error.networkResponse.statusCode);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("grant_type", "refresh_token");
                postDataParams.put("client_id", clientId);
                postDataParams.put("client_secret", clientSecret);
                postDataParams.put("refresh_token", refreshToken);

                return postDataParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        VolleyRequestQueueManager.getInstance(context).addToRequestQueue(postRequest);
    }
}

