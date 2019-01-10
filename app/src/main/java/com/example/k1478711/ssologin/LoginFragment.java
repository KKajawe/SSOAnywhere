package com.example.k1478711.ssologin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.k1478711.ssoweb.R;

public class LoginFragment extends Fragment {

    private EditText edt_usrname, edt_passwrd;

    private TextView txt_connection;
    private TextView txt_error;
    private LinearLayout layedt_password, layedt_username;
    private ImageView usernameIcon, passwordIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        edt_usrname = (EditText) view.findViewById(R.id.edt_username);
        layedt_password = (LinearLayout) view.findViewById(R.id.layoutedt_password);
        layedt_username = (LinearLayout) view.findViewById(R.id.layoutedt_username);
        usernameIcon = (ImageView) view.findViewById(R.id.iconedt_username);
        passwordIcon = (ImageView) view.findViewById(R.id.iconedt_password);
        edt_passwrd = (EditText) view.findViewById(R.id.edt_password);
        edt_usrname.setFocusableInTouchMode(true);
        edt_passwrd.setFocusableInTouchMode(true);

        txt_connection = (TextView) view.findViewById(R.id.txt_connection);
        txt_error = (TextView) view.findViewById(R.id.errorText);


        edt_usrname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                layedt_username.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_red));
                usernameIcon.setImageResource(R.drawable.activeuser);
                layedt_password.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bordergrey));
                passwordIcon.setImageResource(R.drawable.inactivelock);
            }
        });
        edt_passwrd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                layedt_username.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bordergrey));
                usernameIcon.setImageResource(R.drawable.inactiveuser);
                layedt_password.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_red));
                passwordIcon.setImageResource(R.drawable.activelock);
            }
        });
        edt_usrname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_passwrd.requestFocus();
                    layedt_username.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bordergrey));
                    usernameIcon.setImageResource(R.drawable.inactiveuser);
                    layedt_password.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_red));
                    passwordIcon.setImageResource(R.drawable.activelock);
                    return true;
                }
                return false;
            }
        });
        edt_passwrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edt_passwrd.clearFocus();
                    layedt_username.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bordergrey));
                    usernameIcon.setImageResource(R.drawable.inactiveuser);
                    layedt_password.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bordergrey));
                    passwordIcon.setImageResource(R.drawable.inactivelock);
                    if (txt_error.getVisibility() == View.VISIBLE) {
                        txt_error.setVisibility(View.GONE);
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        txt_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (null != edt_usrname && edt_usrname.getText().toString().equals("")) {
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(R.string.enterusername);
                } else if (null != edt_passwrd && edt_passwrd.getText().toString().equals("")) {
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(R.string.enterpassword);
                } else if (txt_error.getVisibility() == View.VISIBLE) {
                    txt_error.setVisibility(View.GONE);
                    loginTask();
                } else {
                    loginTask();
                }

            }
        });

        return view;
    }

    private void loginTask() {
        MainActivity.getmainActivity().showProgressBar();
        MainActivity.getmainActivity().getSSOAnywhere().callingAutherizeAPI(edt_usrname.getText().toString().trim(), edt_passwrd.getText().toString().trim());
    }

}
