package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SignSub6 extends AppCompatActivity {
    private Toolbar toolbar;
    private Button findEmail, findPW;
    private SignSub6_1 dialog1;
    private SignSub6_2 dialog2;
    Spinner spinnerYear, spinnerMonth, spinnerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_sub6);
        findEmail = findViewById(R.id.find_email_btn);
        findPW = findViewById(R.id.find_pw_btn);
        dialogSet();
        setToolbar();
        {
            findEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1.show();
                    //dialog2.show();
                }
            });
            findPW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        setSpinnerYear();
        setSpinnerMonth();
        setSpinnerDay();
    }

    public void dialogSet() {
        dialog1 = new SignSub6_1(SignSub6.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);//다이얼로그 타이틀바 없애기
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//배경 투명하게

        dialog2 = new SignSub6_2(SignSub6.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);//다이얼로그 타이틀바 없애기
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//배경 투명하게
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("로그인");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void find_dialog_dismiss(View v) {
        switch (v.getId()) {
            case R.id.email_check_btn:
                dialog1.dismiss();
                break;
            case R.id.email_check_btn2:
                dialog2.dismiss();
                break;
        }
    }

    public void setSpinnerYear() {
        spinnerYear = findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("년");
        for (int i = 1900; i < 2019; i++) {
            list.add(i + "년");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
    }

    public void setSpinnerMonth() {
        spinnerMonth = findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("월");
        for (int i = 1; i < 13; i++) {
            list.add(i + "월");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
    }

    public void setSpinnerDay() {
        spinnerDay = findViewById(R.id.spinnerDay);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("일");
        for (int i = 1; i < 32; i++) {
            list.add(i + "일");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);
    }
}
