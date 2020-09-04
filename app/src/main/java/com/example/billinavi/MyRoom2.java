package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRoom2 extends AppCompatActivity {
    private Toolbar toolbar;
    private PieChart pieChart;
    private TextView winrate, shot, average, highrun, interval, lastinning, foul, achieve;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myroom2);

        winrate = findViewById(R.id.winrate_myroom2);
        shot = findViewById(R.id.shot_myroom2);
        average = findViewById(R.id.average_myroom2);
        highrun = findViewById(R.id.hignRun_myroom2);
        interval = findViewById(R.id.interval_myroom2);
        lastinning = findViewById(R.id.lastInning_myroom2);
        foul = findViewById(R.id.foul_myroom2);
        achieve = findViewById(R.id.achive_myroom2);


        setText();
        setToolbar();
        setPieChart();
        setLineChart();
        setLineChart2();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setPieChart();
        setLineChart();
        setLineChart2();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPieChart();
        setLineChart();
        setLineChart2();
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

    public void setText(){
        winrate.setText(MainActivity.winningRate);
        shot.setText(MainActivity.shotSucc);
        average.setText(MainActivity.average);
        highrun.setText(MainActivity.highRun);
        interval.setText(MainActivity.interval);
        lastinning.setText(MainActivity.lastinning);
        foul.setText(MainActivity.foul);
        achieve.setText(MainActivity.achieverate);
    }

    public void setPieChart() {
        int totalInt, winInt, drawInt, loseInt;
        TextView totalText, winText, drawText, loseText;
        totalText = findViewById(R.id.total_pie);
        winText = findViewById(R.id.win_pie);
        drawText = findViewById(R.id.draw_pie);
        loseText = findViewById(R.id.lose_pie);

        totalText.setText(HomeSub3.totalText.getText().toString());
        winText.setText(HomeSub3.winText.getText().toString());
        drawText.setText(HomeSub3.drawText.getText().toString());
        loseText.setText(HomeSub3.loseText.getText().toString());

        totalInt = Integer.parseInt(HomeSub3.totalText.getText().toString());
        winInt = Integer.parseInt(HomeSub3.winText.getText().toString());
        drawInt = Integer.parseInt(HomeSub3.drawText.getText().toString());
        loseInt = Integer.parseInt(HomeSub3.loseText.getText().toString());

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);


        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(80f);
        pieChart.setHoleColor(Color.parseColor("#00000000"));
        pieChart.setTransparentCircleRadius(61f);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(loseInt, "", 0));
        yValues.add(new PieEntry(drawInt, "", 1));
        yValues.add(new PieEntry(winInt, "", 2));

        Description description = new Description();
        description.setText("세계 국가"); //라벨
        description.setTextSize(15);
        description.setEnabled(false);
        // pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "");
        //dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(0);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //return Integer.toString((int)value);
                return "";
            }
        });
        dataSet.setColors(MainActivity.color);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setData(data);
    }

    public void setLineChart2() {
        lineChart = findViewById(R.id.lineChart_shot);
        List<Entry> entries = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries, "최근 샷 성공률");

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("");
        for (int i = 0; i < MainActivity.shotRate.size(); i++) {
            entries.add(new Entry((i + 1), MainActivity.shotRate.get(i)));
            labels.add(MainActivity.shotDate.get(i));
        }
        labels.add("");

        //그래프 선이랑 점
        lineDataSet.setLineWidth(1);
        //lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleColor(Color.parseColor("#f54c37"));
        lineDataSet.setCircleColorHole(Color.parseColor("#f54c37"));
        lineDataSet.setColor(Color.parseColor("#f54c37"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        //각 데이터값
        lineDataSet.setValueTextColor(Color.parseColor("#777777"));
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Float.toString(value);
            }
        });
        lineDataSet.setValueTextSize(8);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        //x축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(6);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(0, 24, 0);


        //y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setLabelCount(5, true);
        yLAxis.setAxisMaxValue(100);
        yLAxis.setAxisMinValue(0);
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawAxisLine(false);//왼쪽 세로줄 없애기
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);//오른쪽 세로줄, 값,그리드 없애기

        lineChart.getLegend().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(null);
        lineChart.setTouchEnabled(false);
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }

    public void setLineChart() {
        lineChart = findViewById(R.id.lineChart_ave);
        List<Entry> entries = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries, "최근 에버리지");

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("");
        for (int i = 0; i < MainActivity.recentAve.size(); i++) {
            entries.add(new Entry((i + 1), MainActivity.recentAve.get(i)));
            labels.add(MainActivity.recentDate.get(i));
        }
        labels.add("");

        //그래프 선이랑 점
        lineDataSet.setLineWidth(1);
        //lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleColor(Color.parseColor("#f54c37"));
        lineDataSet.setCircleColorHole(Color.parseColor("#f54c37"));
        lineDataSet.setColor(Color.parseColor("#f54c37"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        //각 데이터값
        lineDataSet.setValueTextColor(Color.parseColor("#777777"));
        lineDataSet.setValueTextSize(8);
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Float.toString(value);
            }
        });

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        //x축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(6);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(0, 24, 0);


        //y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setLabelCount(5, true);
        yLAxis.setAxisMaxValue(Float.parseFloat((Float.toString(Collections.max(MainActivity.recentAve))).substring(0,3))+0.1f);
        yLAxis.setAxisMinValue(Float.parseFloat((Float.toString(Collections.min(MainActivity.recentAve))).substring(0,3))-0.1f);
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawAxisLine(false);//왼쪽 세로줄 없애기
        yLAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.format("%.3f",value);
            }
        });
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);//오른쪽 세로줄, 값,그리드 없애기

        lineChart.getLegend().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(null);
        lineChart.setTouchEnabled(false);
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }
}
