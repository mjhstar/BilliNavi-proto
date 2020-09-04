package com.example.billinavi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class RankingAve extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView winrate, shot, average, highrun, interval, lastinning, foul, achieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_ave);

        winrate = findViewById(R.id.winrate_ave);
        shot = findViewById(R.id.shot_ave);
        average = findViewById(R.id.average_ave);
        highrun = findViewById(R.id.hignRun_ave);
        interval = findViewById(R.id.interval_ave);
        lastinning = findViewById(R.id.lastInning_ave);
        foul = findViewById(R.id.foul_ave);
        achieve = findViewById(R.id.achive_ave);

        setToolbar();
        setText();
    }

    public void setToolbar() {
        //툴바 설정하기
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("나의 기록");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setText() {
        winrate.setText(MainActivity.winningRate);
        shot.setText(MainActivity.shotSucc);
        average.setText(MainActivity.average);
        highrun.setText(MainActivity.highRun);
        interval.setText(MainActivity.interval);
        lastinning.setText(MainActivity.lastinning);
        foul.setText(MainActivity.foul);
        achieve.setText(MainActivity.achieverate);
    }
}
