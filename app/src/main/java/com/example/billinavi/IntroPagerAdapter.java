package com.example.billinavi;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class IntroPagerAdapter extends FragmentPagerAdapter {
    private static int MAX_PAGE = 3;

    public IntroPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return MAX_PAGE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Intro1();
            case 1:
                return new Intro2();
            case 2:
                return new Intro3();
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
