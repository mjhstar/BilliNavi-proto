package com.example.billinavi;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager mFragmentManager;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Home.newInstance();
            case 1:
                return Ranking.newInstance();
            case 2:
                return Lounge.newInstance();
            case 3:
                return Store.newInstance();
            case 4:
                return MyRoom.newInstance();
            default:
                return null;
        }
    }

    private static int PAGE_NUMBER = 5;

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "홈";
            case 1:
                return "랭킹";
            case 2:
                return "큐 라운지";
            case 3:
                return "매장정보";
            case 4:
                return "마이룸";
            default:
                return null;
        }
    }

}
