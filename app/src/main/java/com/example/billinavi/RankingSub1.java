package com.example.billinavi;


import android.content.ContentValues;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RankingSub1 extends Fragment {
    private RecyclerView recyclerView;
    private RankingRecyclerAdapter adapter;
    static public LinearLayout rankingSub1_user;
    static public TextView userName, userRate, winRate;
    static public ImageView userImage, userImage_non;
    private ArrayList<String> listID = new ArrayList<>(), listWin=new ArrayList<>();
    private SwipeRefreshLayout layout;

    public RankingSub1() {
    }

    public static RankingSub1 newInstance() {
        Bundle args = new Bundle();
        RankingSub1 fragment = new RankingSub1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranking_sub1, container, false);
        {

            recyclerView = view.findViewById(R.id.ranking_sub1_recy);
            rankingSub1_user = view.findViewById(R.id.ranking_sub1_userInfo);
            userImage_non = view.findViewById(R.id.rankSub1_Image_non);
            userImage_non.setBackground(new ShapeDrawable(new OvalShape()));
            userImage_non.setClipToOutline(true);
            layout = view.findViewById(R.id.swipe_rankingSub1);
        }
        init();
        getData();

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                getData();
                layout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        getData();
    }

    private void init() {
        listID.clear();
        listWin.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RankingRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<Integer> listNumID = Arrays.asList(R.drawable.star_gold, R.drawable.star_silver, R.drawable.star_bronz,R.drawable.star_gold, R.drawable.star_silver, R.drawable.star_bronz,R.drawable.star_gold, R.drawable.star_silver, R.drawable.star_bronz,R.drawable.star_gold);
        List<Integer> listChangeID = Arrays.asList(R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix,R.drawable.ranking_fix);
        StrictMode.enableDefaults();
        boolean bNick = false, bWinRate = false;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/RankingSub1_read.asp");

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("NICKNAME")) {
                            bNick = true;
                        }
                        if (parser.getName().equals("WINRATE")) {
                            bWinRate = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bNick) {
                            listID.add(parser.getText());
                            bNick = false;
                        }
                        if (bWinRate) {
                            listWin.add(parser.getText());
                            bWinRate = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {

        }
        for (int i = 0; i < listID.size(); i++) {
            RankingData data = new RankingData();
            data.setID(listID.get(i));
            data.setRate(listWin.get(i));
            data.setNumId(listNumID.get(i));
            data.setChangeId(listChangeID.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}
