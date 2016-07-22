package com.swmaestro.hangman_together.splash;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.intro.IntroActivity;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    @BindString(R.string.splash_value_error_internet_connection) String valueErrorInternetConnection;

    static Unbinder butterKnifeUnbinder;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        butterKnifeUnbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();

        if(checkInternetConnection()) {
            startIntroActivity();
        } else {
            Toast.makeText(getApplicationContext(), valueErrorInternetConnection, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    public void startIntroActivity() {
        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
        startActivity(intent);
        finish();
    }


    @Override protected void onDestroy() {
        super.onDestroy();

        if(butterKnifeUnbinder!=null) butterKnifeUnbinder.unbind();
    }
}
