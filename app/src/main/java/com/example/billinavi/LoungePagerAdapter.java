package com.example.billinavi;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoungePagerAdapter extends FragmentPagerAdapter {
    private final FragmentManager mFragmentManager;

    public LoungePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LoungeSub1.newInstance();
            case 1:
                return LoungeSub2.newInstance();
            case 2:
                return LoungeSub3.newInstance();
            case 3:
                return LoungeSub4.newInstance();
            case 4:
                return LoungeSub5.newInstance();
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
                return "당구레슨";
            case 1:
                return "베스트영상";
            case 2:
                return "영상자랑";
            case 3:
                return "자유게시판";
            case 4:
                return "대회정보";
            default:
                return null;
        }
    }
}
