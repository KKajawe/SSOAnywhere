package com.example.ssoanywhere.controller;


public interface AutherizedResponseInterface {
    void getResponse(String result, boolean fromRefreshAPI);
    void getErrorResponse(int responseCode);
}
