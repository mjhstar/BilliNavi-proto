package com.example.billinavi;

import android.content.Context;
import android.os.Bundle;

public class LoginDialog extends android.app.Dialog {

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
    }
}