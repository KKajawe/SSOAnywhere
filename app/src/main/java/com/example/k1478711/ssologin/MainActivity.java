package com.example.k1478711.ssologin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.k1478711.ssoweb.R;
import com.example.ssoanywhere.controller.AutherizedResponseInterface;
import com.example.ssoanywhere.view.SSOAnywhere;

import java.net.HttpURLConnection;


public class MainActivity extends AppCompatActivity {

    private static MainActivity mainActivity;
    private RefreshTokenWarningFragment dialogFragment;
    private ProgressBar progressbar;
    private SSOAnywhere ssoAnywhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ssoAnywhere = new SSOAnywhere(mainActivity, getString(R.string.clientId), getString(R.string.clientSecret), new AutherizedResponseInterface() {
            @Override
            public void getResponse(String jsonResponse, boolean fromRefreshAPI) {
                hideProgressBar();
                if (fromRefreshAPI) {
                    getSSOAnywhere().deleteSome(getString(R.string.clientId), getString(R.string.clientSecret));
                    getSSOAnywhere().setSSOSharedPreference(jsonResponse);
                    navigateToTimingFrag();
                } else {
                    getSSOAnywhere().setSSOSharedPreference(jsonResponse);
                    navigateToTimingFrag();
                }
            }

            @Override
            public void getErrorResponse(int responseCode) {
                hideProgressBar();
                navigateToLoginFrag();
                if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR)
                    showToastDialog("Internal server error, please try after sometime");
                else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
                    showToastDialog("400 Bad Request,please check username or password may wrong");
                else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED)
                    showToastDialog("Please check username or password may wrong");
                else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND)
                    showToastDialog("404 Data not found error, please try after sometime");
                else
                    showToastDialog("Something went wrong,please try after sometime");
            }
        });
        if (getSSOAnywhere().isLoggedIn()) {
            navigateToTimingFrag();
        } else {
            navigateToLoginFrag();
        }
    }

    public SSOAnywhere getSSOAnywhere() {
        return ssoAnywhere;
    }

    public static MainActivity getmainActivity() {
        return mainActivity;
    }

    public void showToastDialog(String message) {
        dialogFragment = new RefreshTokenWarningFragment(getmainActivity(), message);
        dialogFragment.setCancelable(true);
        dialogFragment.show();
    }

    public void dismissDialog() {
        dialogFragment.dismiss();
    }

    public void showProgressBar() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressbar.setVisibility(View.GONE);
    }


    private void navigateToTimingFrag() {
        TokenFragment tokenFragment = new TokenFragment();
        FragmentTransaction ft = MainActivity.getmainActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_container, tokenFragment);
        ft.commitAllowingStateLoss();
    }

    public void navigateToLoginFrag() {
        getSSOAnywhere().deleteSome(getString(R.string.clientId), getString(R.string.clientSecret));
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frag_container, loginFragment);
        ft.commitAllowingStateLoss();
    }
}
