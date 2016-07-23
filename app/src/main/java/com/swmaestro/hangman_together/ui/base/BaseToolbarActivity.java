package com.swmaestro.hangman_together.ui.base;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.swmaestro.hangman_together.R;

import junit.framework.Assert;

public class BaseToolbarActivity extends AppCompatActivity {

    private TextView tvToolbarTitle;

    /**
     * setContentView 는 최초에 base_activity_toolbar 래이아웃으로 하며
     * 상속 받은 Activity 에서 setContentView 를 호출 할 시에 해당 래이아웃을 inflate 하여
     * {@link #addContentView(View, ViewGroup.LayoutParams)} 한다.
     *
     * @param layoutResID
     */
    @Override public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        View toolbarLayout = getLayoutInflater().inflate(R.layout.activity_base_toolbar, null);
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(toolbarLayout, lp);

        initToolbar();
    }

    private void initToolbar() {
        // set ActionBar as custom Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        initToolbarTitleIfActivityLabelDefined();

        Assert.assertNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // Manifests 에 설정한 Activity 의 label 을 Toolbar 의 title 로 설정한다.
    private void initToolbarTitleIfActivityLabelDefined() {
        CharSequence label = getActivityInfo().loadLabel(getPackageManager());
        setToolbarTitle(label);
    }

    // Toolbar 의 title 을 설정한다.
    protected void setToolbarTitle(CharSequence text) {
        Assert.assertNotNull("Toolbar title view is null", tvToolbarTitle);

        tvToolbarTitle.setText(text);
    }

    // 백 버튼 활성화
    protected void enableHomeAsUp() {
        Assert.assertNotNull("Toolbar is not initialized properly.", getSupportActionBar());

        // TODO PARENT_ACTIVITY 가 정해졌는지 판별 후 정해지지 않으면 강제 Exception 발생 여부 논의 필요.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private ActivityInfo getActivityInfo() {
        try {
            return getPackageManager().getActivityInfo(getComponentName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("ActivityInfo cannot be null");
        }
    }
}
