package com.example.billinavi;


import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class StoreSub2 extends Fragment {
    public static ArrayList<String> index = new ArrayList<String>(), title = new ArrayList<>(), addr = new ArrayList<>(), call = new ArrayList<>(), locateX = new ArrayList<>(), locateY = new ArrayList<>(), service = new ArrayList<>(), distance = new ArrayList<>();
    private RecyclerView recyclerView;
    private StoreSub1RecyclerAdapter adapter;
    private EditText searchEdit;
    private String search;
    private TextView countText;
    private SwipeRefreshLayout layout;

    public StoreSub2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_sub2, container, false);
        recyclerView = view.findViewById(R.id.store_sub2_recy);
        searchEdit = view.findViewById(R.id.store_sub2_search_edit);
        countText = view.findViewById(R.id.store_count_sub2);
        //layout = view.findViewById(R.id.swipe_storeSub2);
        //url입력

        searchEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                            countText.setText("" + title.size());
                        }
                    }
                }
                return false;
            }
        });

        init();
        getData(1);
        countText.setText("" + title.size());
        /*layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                getData(1);
                layout.setRefreshing(false);
                countText.setText(""+title.size());
            }
        });*/

        return view;
    }

    private void init() {
        index.clear();
        title.clear();
        addr.clear();
        service.clear();
        locateX.clear();
        locateY.clear();
        call.clear();
        distance.clear();
        Log.i("asdfasdfasdf", MainActivity.locateX+"        "+MainActivity.locateY);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StoreSub1RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(int count) {
        StrictMode.enableDefaults();
        boolean bIndex = false, bTitle = false, bAddr = false, bCall = false, bLocateX = false, bLocateY = false, bService = false, bDistance = false;
        String sTitle = null, sAddr = null, sCall = null, sLocateX = null, sLocateY = null, sService = null;
        if (count == 1) {
            try {
                URL url = new URL("http://192.168.0.145:8088/xml/StoreSub1_read.asp?locateX=" + MainActivity.locateX + "&locateY=" + MainActivity.locateY);

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
                            if (parser.getName().equals("ADDRESS")) {
                                bAddr = true;
                            }
                            if (parser.getName().equals("CALL")) {
                                bCall = true;
                            }
                            if (parser.getName().equals("LOCATEX")) {
                                bLocateX = true;
                            }
                            if (parser.getName().equals("LOCATEY")) {
                                bLocateY = true;
                            }
                            if (parser.getName().equals("SERVICETIME")) {
                                bService = true;
                            }
                            if (parser.getName().equals("DISTANCE")) {
                                bDistance = true;
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
                            if (bAddr) {
                                addr.add(parser.getText());
                                bAddr = false;
                            }
                            if (bCall) {
                                call.add(parser.getText());
                                bCall = false;
                            }
                            if (bLocateX) {
                                locateX.add(parser.getText());
                                bLocateX = false;
                            }
                            if (bLocateY) {
                                locateY.add(parser.getText());
                                bLocateY = false;
                            }
                            if (bService) {
                                service.add(parser.getText().substring(0, 8));
                                bService = false;
                            }
                            if (bDistance) {
                                distance.add(parser.getText());
                                bDistance = false;
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
                URL url = new URL("http://192.168.0.145:8088/xml/StoreSub2_read.asp?search=" + search + "&locateX=" + MainActivity.locateX + "&locateY=" + MainActivity.locateY);

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
                            if (parser.getName().equals("ADDRESS")) {
                                bAddr = true;
                            }
                            if (parser.getName().equals("CALL")) {
                                bCall = true;
                            }
                            if (parser.getName().equals("LOCATEX")) {
                                bLocateX = true;
                            }
                            if (parser.getName().equals("LOCATEY")) {
                                bLocateY = true;
                            }
                            if (parser.getName().equals("SERVICETIME")) {
                                bService = true;
                            }
                            if (parser.getName().equals("DISTANCE")) {
                                bDistance = true;
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
                            if (bAddr) {
                                addr.add(parser.getText());
                                bAddr = false;
                            }
                            if (bCall) {
                                call.add(parser.getText());
                                bCall = false;
                            }
                            if (bLocateX) {
                                locateX.add(parser.getText());
                                bLocateX = false;
                            }
                            if (bLocateY) {
                                locateY.add(parser.getText());
                                bLocateY = false;
                            }
                            if (bService) {
                                service.add(parser.getText().substring(0, 8));
                                bService = false;
                            }
                            if (bDistance) {
                                distance.add(parser.getText());
                                bDistance = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
            }
        }

        for (int i = 0; i < title.size(); i++) {
            StoreSub1Data data = new StoreSub1Data();
            data.setIndex(index.get(i));
            data.setTitle(title.get(i));
            data.setAddr(addr.get(i));
            data.setCall(call.get(i));
            data.setLocateX(locateX.get(i));
            data.setLocateY(locateY.get(i));
            data.setService(service.get(i));
            data.setDistance(distance.get(i));
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}