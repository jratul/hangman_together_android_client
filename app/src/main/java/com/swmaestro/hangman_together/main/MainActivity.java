package com.swmaestro.hangman_together.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.swmaestro.hangman_together.R;

import java.util.List;

public class MainActivity extends FragmentActivity {
    MainPagerAdapter mainPagerAdapter;
    ViewPager mainViewPager;
    List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(mainPagerAdapter);
    }

    private void setFragmentList() {
        Bundle page  = new Bundle();
    }
}
