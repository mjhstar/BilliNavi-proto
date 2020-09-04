package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class SettingSub6 extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout linearLayout, linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sub6);
        {
            toolbar = findViewById(R.id.toolbar_sub);
            TextView title = findViewById(R.id.toolbar_title);//title설정하려고
            title.setText("이용안내");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            linearLayout = findViewById(R.id.sub6_1);
            linearLayout2 = findViewById(R.id.sub6_2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                finish();
                return true; //메니페스트 수정할거있슴다
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub6_1:
                Intent intent = new Intent(SettingSub6.this, SettingSub6_1.class);
                startActivity(intent);
                return;
            case R.id.sub6_2:
                Intent intent2 = new Intent(SettingSub6.this, SettingSub6_2.class);
                startActivity(intent2);
                return;
        }
    }
}
