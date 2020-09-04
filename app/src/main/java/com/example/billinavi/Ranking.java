package com.example.billinavi;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

public class Ranking extends Fragment {
    private FragmentActivity mContext;
    static public ViewPager mViewpager;
    private RankingPagerAdapter mAdapter;
    private TabLayout mTap;
    public Ranking() {
    }

    @Override
    public void onAttach(Context context) {
        mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    public static Ranking newInstance() {
        Bundle args = new Bundle();
        Ranking fragment = new Ranking();
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.ranking, container, false);

        //랭킹 페이지 어댑터 시작
        mAdapter = new RankingPagerAdapter(getChildFragmentManager());
        mViewpager = view.findViewById(R.id.ranking_viewpager);
        mViewpager.setTag("rankingView");
        mViewpager.setAdapter(mAdapter);

        mTap = view.findViewById(R.id.ranking_tabs);
        mTap.setupWithViewPager(mViewpager);
        mAdapter.notifyDataSetChanged();
        mViewpager.setOffscreenPageLimit(5);
        //랭킹 페이지 어댑터 끝
        return view;
    }

}
