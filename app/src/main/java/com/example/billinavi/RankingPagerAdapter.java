package com.example.billinavi;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RankingPagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager mFragmentManager;

    public RankingPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RankingSub1.newInstance();
            case 1:
                return RankingSub2.newInstance();
            case 2:
                return RankingSub3.newInstance();
            default:
                return null;
        }
    }

    private static int PAGE_NUMBER = 3;

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "4구 모드";
            case 1:
                return "3구 일반룰";
            case 2:
                return "3구 대회룰";
            default:
                return null;
        }
    }
}

