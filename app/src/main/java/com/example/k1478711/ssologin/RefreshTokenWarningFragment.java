package com.example.k1478711.ssologin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.k1478711.ssoweb.R;

public class RefreshTokenWarningFragment extends Dialog {


    private String message;

    public RefreshTokenWarningFragment(Context context, String message) {
        super(context);
        this.message = message;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.refresh_warning);
        TextView messageText = findViewById(R.id.messageText);
        messageText.setText(message);

    }




}
