package com.example.billinavi;


import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Store extends Fragment {
    LinearLayout linerCall;
    public static TextView store_call, store_title, store_address;
    public static String service, locateX, locateY, index;
    private SwipeRefreshLayout layout;

    public Store() {
    }

    public static Store newInstance() {
        Bundle args = new Bundle();
        Store fragment = new Store();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store, container, false);
        //전화걸기 시작
        linerCall = view.findViewById(R.id.line_store_call);
        store_call = view.findViewById(R.id.store_call);
        store_title = view.findViewById(R.id.myStore_title);
        store_address = view.findViewById(R.id.myStore_address);


        linerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!store_call.getText().equals("") && MainActivity.userIndex > -1) {
                    String call = store_call.getText().toString();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + call));
                    startActivity(intent);
                } else if (MainActivity.userIndex > -1)
                    Toast.makeText(getContext(), "관심매장을 설정해주세요", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }); //전화걸기 끝
        return view;
    }
}