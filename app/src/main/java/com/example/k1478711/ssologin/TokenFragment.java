package com.example.k1478711.ssologin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k1478711.ssoweb.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TokenFragment extends Fragment {

    private View view;
    private TextView token_Detail;
    private String response;
    private long expiretimeSec;
    private Handler someHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tokenfrag, container, false);
        expiretimeSec = Integer.valueOf(MainActivity.getmainActivity().getSSOAnywhere().getSessionData().get(0));
        response = MainActivity.getmainActivity().getSSOAnywhere().getSessionData().get(2);

        token_Detail = (TextView) view.findViewById(R.id.tokenDetail);
        Log.d("SSOweb expired at", String.valueOf(expiretimeSec));
         someHandler = new Handler(MainActivity.getmainActivity().getMainLooper());

        someHandler.postDelayed(calenderUpdater, 10);
        return view;
    }


    private String getDate(long seconds) {
        String temp = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);
        String time = String.valueOf(calendar.getTime());
        return time;
    }

    public Runnable calenderUpdater = new Runnable() {
        @Override
        public void run() {
            long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            if (expiretimeSec - currentSeconds == 40) {
                Toast.makeText(MainActivity.getmainActivity(), "callRefreshToken", Toast.LENGTH_SHORT).show();
                MainActivity.getmainActivity().showToastDialog("Token is expired, please wait till token is refreshed");
                MainActivity.getmainActivity().getSSOAnywhere().callingRefreshTokenAPI();
            }else if(expiretimeSec < currentSeconds){
                MainActivity.getmainActivity().navigateToLoginFrag();
                someHandler.removeCallbacks(calenderUpdater);
            }else{
                token_Detail.setText("token will expired at: " + getDate(expiretimeSec) + "\n **** \n" + "current time is: " + getDate(currentSeconds) + "\n *** \n\n" + response);
                someHandler.postDelayed(calenderUpdater, 1000);
            }
        }
    };

}
