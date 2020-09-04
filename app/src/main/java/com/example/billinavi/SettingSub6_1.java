package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class SettingSub6_1 extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sub6_1);
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("이용약관");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
