package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyRoom3 extends AppCompatActivity {
    private Toolbar toolbar;
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> writer = new ArrayList<>();
    public static ArrayList<String> context = new ArrayList<>();
    public static ArrayList<String> day = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> index = new ArrayList<>();
    public static ArrayList<String> image = new ArrayList<>();
    public static ArrayList<String> videoUrl = new ArrayList<>();
    private RecyclerView recyclerView;
    private LoungeSub1RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myroom3);
        setToolbar();
        recyclerView = findViewById(R.id.myRoom3_recy);
        init();
        getData();
    }

    public void setToolbar() {
        //툴바 설정하기
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("나의 영상");
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

    @Override
    public void onResume() {
        super.onResume();
        init();
        getData();
    }

    private void init() {
        index.clear();
        image.clear();
        title.clear();
        writer.clear();
        context.clear();
        day.clear();
        time.clear();
        videoUrl.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LoungeSub1RecyclerAdapter(4);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        StrictMode.enableDefaults();
        boolean bIndex = false, bImage = false, bTitle = false, bWriter = false, bContext = false, bDay = false, bTime = false, bVideoUrl = false;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub3_read_myVideo.asp?userindex=" + MainActivity.userIndex);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("INDEX")) {
                            bIndex = true;
                        }
                        if (parser.getName().equals("IMAGE")) {
                            bImage = true;
                        }
                        if (parser.getName().equals("TITLE")) {
                            bTitle = true;
                        }
                        if (parser.getName().equals("WRITER")) {
                            bWriter = true;
                        }
                        if (parser.getName().equals("CONTEXT")) {
                            bContext = true;
                        }
                        if (parser.getName().equals("DAY")) {
                            bDay = true;
                        }
                        if (parser.getName().equals("TIME")) {
                            bTime = true;
                        }
                        if (parser.getName().equals("VIDEOURL")) {
                            bVideoUrl = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            index.add(parser.getText());
                            bIndex = false;
                        }
                        if (bImage) {
                            image.add(parser.getText());
                            bImage = false;
                        }
                        if (bTitle) {
                            title.add(parser.getText());
                            bTitle = false;
                        }
                        if (bWriter) {
                            writer.add(parser.getText());
                            bWriter = false;
                        }
                        if (bContext) {
                            context.add(parser.getText());
                            bContext = false;
                        }
                        if (bDay) {
                            day.add(parser.getText());
                            bDay = false;
                        }
                        if (bTime) {
                            time.add(parser.getText().substring(0, 8));
                            bTime = false;
                        }
                        if (bVideoUrl) {
                            videoUrl.add(parser.getText());
                            bVideoUrl = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {        }

        for (int i = 0; i < title.size(); i++) {
            LoungeSub1Data data = new LoungeSub1Data();
            data.setImage(image.get(i));
            data.setTitle(title.get(i));
            data.setContent(context.get(i));
            data.setUser(writer.get(i));
            data.setDate(day.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}
