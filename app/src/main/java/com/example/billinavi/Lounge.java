package com.example.billinavi;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

public class Lounge extends Fragment {
    private FragmentActivity mContext;
    static public ViewPager mViewpager;
    LoungePagerAdapter mAdapter;
    TabLayout mTap;

    public Lounge() {
    }

    public static Lounge newInstance() {
        Bundle args = new Bundle();
        Lounge fragment = new Lounge();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    //탭 레이아웃 유지
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lounge, container, false);

        //라운지 페이지 어댑터 시작
        mViewpager = view.findViewById(R.id.lounge_viewpager);
        mTap = view.findViewById(R.id.lounge_tabs);
        mAdapter = new LoungePagerAdapter(getChildFragmentManager());
        setPager(0);
        //라운지 페이지 어댑터 끝

        return view;
    }

    public void setPager(int i) {
        mViewpager.setAdapter(mAdapter);
        mTap.setupWithViewPager(mViewpager);
        mAdapter.notifyDataSetChanged();
        mViewpager.setOffscreenPageLimit(5);
        mViewpager.setCurrentItem(i);
    }
}