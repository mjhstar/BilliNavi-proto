package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Intro extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Button btn_start;
    private boolean INTRO = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        //선언부
        {
            btn_start = findViewById(R.id.start_btn);
            viewPager = findViewById(R.id.intro_viewpager);
            circleIndicator = findViewById(R.id.circleIndicator);
        }
        if (INTRO) {
            viewPager.setAdapter(new IntroPagerAdapter(getSupportFragmentManager()));
            viewPager.setCurrentItem(0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    circleIndicator.selectDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            circleIndicator.setItemMargin(5);
            circleIndicator.createDotPanel(3, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on);

            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intro.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        INTRO = false;
    }
}
