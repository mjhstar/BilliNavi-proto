package com.example.billinavi;


import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoungeSub3 extends Fragment {
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> writer = new ArrayList<>();
    public static ArrayList<String> context = new ArrayList<>();
    public static ArrayList<String> day = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> index = new ArrayList<>();
    public static ArrayList<String> image = new ArrayList<>();
    public static ArrayList<String> videoUrl = new ArrayList<>();

    private EditText searchEdit;
    private RecyclerView recyclerView;
    private LoungeSub1RecyclerAdapter adapter;
    private String search;
    private SwipeRefreshLayout layout;

    public LoungeSub3() {
    }

    public static LoungeSub3 newInstance() {
        Bundle args = new Bundle();
        LoungeSub3 fragment = new LoungeSub3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lounge_sub3, container, false);
        {
            searchEdit = view.findViewById(R.id.lounge_sub3_search_edit);
            recyclerView = view.findViewById(R.id.lounge_sub3_recy);
            layout = view.findViewById(R.id.swipe_loungeSub3);
        } //선언부

        init();
        getData(1);

        {
            searchEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MainActivity.userIndex < 0) {
                        Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (searchEdit.getRight() - searchEdit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                search = searchEdit.getText().toString();
                                if (!search.equals("")) {
                                    init();
                                    getData(2);
                                    searchEdit.setText("");
                                }
                            }
                        }
                    }
                    return false;
                }
            });
        }
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                getData(1);
                layout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        getData(1);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LoungeSub1RecyclerAdapter(3);
        recyclerView.setAdapter(adapter);
    }

    private void getData(int count) {
        StrictMode.enableDefaults();
        boolean bIndex = false, bImage = false, bTitle = false, bWriter = false, bContext = false, bDay = false, bTime = false, bVideoUrl = false;
        if (count == 1) {
            try {
                URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub3_read.asp");

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
            } catch (Exception e) {

            }
        }
        if (count == 2) {
            try {
                URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub3_read_search.asp?search=" + search);

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
            } catch (Exception e) {

            }
        }
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
