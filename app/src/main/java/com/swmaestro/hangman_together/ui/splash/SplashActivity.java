package com.swmaestro.hangman_together.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.ui.intro.IntroActivity;
import com.swmaestro.hangman_together.ui.main.MainActivity;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.login.LoginResponse;
import com.swmaestro.hangman_together.rest.login.LoginService;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class SplashActivity extends AppCompatActivity {

    @BindString(R.string.splash_value_error_internet_connection) String valueErrorInternetConnection;
    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;
    @BindString(R.string.intro_value_login_failure) String valueLoginFailureMessage;

    static Unbinder butterKnifeUnbinder;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        butterKnifeUnbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();

        if(checkInternetConnection()) {
            String userPhoneNum = Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM);
            if(!userPhoneNum.isEmpty() && !userPhoneNum.equals("")) {
                requestLogin(userPhoneNum, getLastConnectTime());
            } else {
                startIntroActivity();
            }
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

    private void requestLogin(String phoneNum, String lastConnectTime) {
        try {
            LoginService loginService = RetrofitManager.getInstance().getService(LoginService.class);
            Call<JsonObject> call = loginService.loginRequest(phoneNum, lastConnectTime);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        LoginResponse obj = new LoginResponse();
                        obj = gson.fromJson(resultRaw, LoginResponse.class);
                        responseString = obj.getMessage();

                        if(responseString.equals("y")) {
                            String userPhoneNum = obj.getPhoneNum();
                            String userNickname = obj.getNickname();
                            System.out.println("usernickname : " + userNickname);
                            Util.savePreferences(mContext, HangmanData.KEY_USER_PHONE_NUM, userPhoneNum);
                            Util.savePreferences(mContext, HangmanData.KEY_USER_NICKNAME, userNickname);

                            startMainActivity();
                            finish();
                        } else if(responseString.equals("n")) {
                            Toast.makeText(mContext, valueLoginFailureMessage, Toast.LENGTH_SHORT).show();
                            Util.removePreferences(mContext, HangmanData.KEY_USER_PHONE_NUM);
                            Util.removePreferences(mContext, HangmanData.KEY_USER_NICKNAME);
                            startIntroActivity();
                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage + " exception", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }catch(Exception e) {
            Log.d("intro", e.getMessage());
        }
    }

    private String getLastConnectTime() {
        Calendar calendar = Calendar.getInstance();
        String strNow = calendar.get(Calendar.YEAR) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" +
                calendar.get(Calendar.SECOND);

        return strNow;
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override protected void onDestroy() {
        super.onDestroy();

        if(butterKnifeUnbinder!=null) butterKnifeUnbinder.unbind();
    }
}
