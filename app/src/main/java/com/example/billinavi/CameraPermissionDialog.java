package com.example.billinavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

public class CameraPermissionDialog extends android.app.Dialog {
    static public Button takePhoto_btn, album_btn, cancel_btn;
    public CameraPermissionDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_permission_dialog);

        //선언부
        {
            takePhoto_btn = findViewById(R.id.takePhoto_dia);
            album_btn = findViewById(R.id.album_dia);
            cancel_btn = findViewById(R.id.cancel_dia);
        }
    }
}