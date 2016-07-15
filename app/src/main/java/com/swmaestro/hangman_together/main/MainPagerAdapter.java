package com.swmaestro.hangman_together.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private String[] title;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MainObjectFragment();
        Bundle args = new Bundle();

        args.putInt(MainObjectFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "홈 화면";
            case 1:
                return "친구 목록";
            case 2:
                return "보관함";
        }
        return "";
    }
}