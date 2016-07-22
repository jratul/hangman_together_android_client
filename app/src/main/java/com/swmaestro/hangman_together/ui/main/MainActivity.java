package com.swmaestro.hangman_together.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.ui.friend.FriendFragment;
import com.swmaestro.hangman_together.ui.home.HomeFragment;
import com.swmaestro.hangman_together.ui.stash.StashFragment;

import java.util.List;
import java.util.Vector;

public class MainActivity extends FragmentActivity implements HomeFragment.OnFragmentInteractionListener, FriendFragment.OnFragmentInteractionListener, StashFragment.OnFragmentInteractionListener{
    MainPagerAdapter mainPagerAdapter;
    ViewPager mainViewPager;
    List<Fragment> fragmentList;

    private Context mContext;
    private Fragment homeFragment, friendFragment, stashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        setFragmentList();

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(mainPagerAdapter);

        //Toast.makeText(mContext, Util.getPreferences(mContext, HangmanData.KEY_USER_NICKNAME), Toast.LENGTH_SHORT).show();
    }

    private void setFragmentList() {
        fragmentList = new Vector<Fragment>();

        Bundle page = new Bundle();
        page.putString("url", "url");

        homeFragment = new HomeFragment();
        friendFragment = new FriendFragment();
        stashFragment = new StashFragment();

        homeFragment.setArguments(page);
        friendFragment.setArguments(page);
        stashFragment.setArguments(page);

        fragmentList.add(homeFragment);
        fragmentList.add(friendFragment);
        fragmentList.add(stashFragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
