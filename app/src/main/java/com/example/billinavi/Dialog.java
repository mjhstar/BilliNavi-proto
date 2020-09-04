package com.example.billinavi;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class Dialog extends android.app.Dialog {

    public Dialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
    }
}