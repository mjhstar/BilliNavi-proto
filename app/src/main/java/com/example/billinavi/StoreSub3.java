package com.example.billinavi;


import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


public class StoreSub3 extends Fragment {
    private RecyclerView recyclerView;
    private LoungeSub4RecyclerAdapter adapter;

    public StoreSub3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_sub3, container, false);

        recyclerView = view.findViewById(R.id.store_sub3_recy);
        init();
        getData();

        return view;
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LoungeSub4RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> title = Arrays.asList("금천점에서 저랑 한판 하실분~", "오늘은 뭔가");
        List<String> user = Arrays.asList("당구", "고수");
        List<String> date = Arrays.asList("2019.08.17", "2019.08.17");

        for (int i = 0; i < title.size(); i++) {
            LoungeSub4Data data = new LoungeSub4Data();
            data.setTitle(title.get(i));
            data.setUser(user.get(i));
            data.setDate(date.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}
