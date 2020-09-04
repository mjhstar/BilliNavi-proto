package com.example.billinavi;


import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class LoungeSub4 extends Fragment {
    private EditText searchEdit;
    private RecyclerView recyclerView;
    private LoungeSub4RecyclerAdapter adapter;
    private Button write;
    private String search;
    private SwipeRefreshLayout layout;

    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> writer = new ArrayList<>();
    public static ArrayList<String> context = new ArrayList<>();
    public static ArrayList<String> day = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> index = new ArrayList<>();
    public static ArrayList<String> userIndex = new ArrayList<>();


    public LoungeSub4() {
    }

    public static LoungeSub4 newInstance() {
        Bundle args = new Bundle();
        LoungeSub4 fragment = new LoungeSub4();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lounge_sub4, container, false);
        {
            searchEdit = view.findViewById(R.id.lounge_sub4_search_edit);
            recyclerView = view.findViewById(R.id.lounge_sub4_recy);
            write = view.findViewById(R.id.writeBtn_lounge4);
            layout = view.findViewById(R.id.swipe_loungeSub4);
        } //선언부

        init();
        getData(1);
        //검색창 돋보기 버튼
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
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        }
        {
            write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.userIndex < 0) {
                        Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), LoungeSub4Write.class);
                        startActivity(intent);
                    }
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
        title.clear();
        writer.clear();
        context.clear();
        day.clear();
        time.clear();
        index.clear();
        userIndex.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LoungeSub4RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(int count) {

        StrictMode.enableDefaults();
        boolean bIndex = false, bTitle = false, bWriter = false, bContext = false, bDay = false, bTime = false, bUserIndex = false;
        String sIndex = null, sTitle = null, sWriter = null, sContext = null, sDay = null, sTime = null;

        if (count == 1) {
            try {
                URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub4_read.asp");

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
                            if (parser.getName().equals("USERINDEX")) {
                                bUserIndex = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if (bIndex) {
                                index.add(parser.getText());
                                bIndex = false;
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
                            if (bUserIndex) {
                                userIndex.add(parser.getText());
                                bUserIndex = false;
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
                URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub4_read_search.asp?search=" + search);

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
                            if (parser.getName().equals("USERINDEX")) {
                                bUserIndex = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if (bIndex) {
                                index.add(parser.getText());
                                bIndex = false;
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
                            if (bUserIndex) {
                                userIndex.add(parser.getText());
                                bUserIndex = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
            }
        }
        for (int i = 0; i < title.size(); i++) {
            LoungeSub4Data data = new LoungeSub4Data();
            data.setTitle(title.get(i));
            data.setUser(writer.get(i));
            data.setDate(day.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}
