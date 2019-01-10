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

public class Volley_Authorize {
    private AutherizedResponseInterface autherizedResponseInterface;
    private Context context;
    private String clientId, clientSecret, username, password;

    public Volley_Authorize(Context context, String username, String password, AutherizedResponseInterface autherizedResponseInterface, String clientId, String clientSecret) {
        this.context = context;
        this.autherizedResponseInterface = autherizedResponseInterface;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
    }

    public void authorizeAPICall() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.authorize_refreshTokenUrl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Access_Token = jsonObject.getString("access_token");
                            String Refresh_Token = jsonObject.getString("refresh_token");
                            if (Access_Token.equals("")) {
                                new Volley_Refresh(context, Refresh_Token, autherizedResponseInterface, clientId, clientSecret).refreshTokenAPICall();
                            } else {
                                autherizedResponseInterface.getResponse(response, false);
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
                postDataParams.put("grant_type", "password");
                postDataParams.put("client_id", clientId);
                postDataParams.put("client_secret", clientSecret);
                postDataParams.put("username", username);
                postDataParams.put("password", password);

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
