package com.swmaestro.hangman_together.ui.game;

import android.content.Context;
import android.os.Bundle;

import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.ui.base.BaseToolbarActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GameActivity extends BaseToolbarActivity {

    private Context mContext;

    static Unbinder butterKnifeUnbinder;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        butterKnifeUnbinder = ButterKnife.bind(this);

        initActivity();
    }

    private void initActivity() {
        enableHomeAsUp();
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        if (butterKnifeUnbinder != null) butterKnifeUnbinder.unbind();
    }


}
