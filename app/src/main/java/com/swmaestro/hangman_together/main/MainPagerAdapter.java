package com.swmaestro.hangman_together.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter{

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        Fragment fragment = new MainObjectFragment();
        Bundle args = new Bundle();

        args.putInt(MainObjectFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override public int getCount() {
        return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
        return "OBJECT" + (position + 1);
    }
}
