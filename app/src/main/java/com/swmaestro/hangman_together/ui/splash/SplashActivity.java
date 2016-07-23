package com.swmaestro.hangman_together.ui.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.GCMRegistrationIntentService;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.QuickstartPreferences;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.login.LoginResponse;
import com.swmaestro.hangman_together.rest.login.LoginService;
import com.swmaestro.hangman_together.ui.intro.IntroActivity;
import com.swmaestro.hangman_together.ui.main.MainActivity;

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

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    //mInformationTextView.setVisibility(View.GONE);
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
                    //mInformationTextView.setVisibility(View.VISIBLE);
                    //mInformationTextView.setText(getString(R.string.registering_message_generating));
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    //mRegistrationButton.setText(getString(R.string.registering_message_complete));
                    //mRegistrationButton.setEnabled(false);
                    String token = intent.getStringExtra("token");
                    //mInformationTextView.setText(token);
                }

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        butterKnifeUnbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();

        registBroadcastReceiver();

        getInstanceIdToken();

        if(checkInternetConnection()) {
            String userPhoneNum = Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM);
            if(!userPhoneNum.isEmpty() && !userPhoneNum.equals("")) {
                requestLogin(userPhoneNum, getLastConnectTime(),Util.getPreferences(mContext, HangmanData.KEY_GCM_INSTANCE_ID));
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

    private void requestLogin(String phoneNum, String lastConnectTime, String instanceId) {
        try {
            LoginService loginService = RetrofitManager.getInstance().getService(LoginService.class);
            Call<JsonObject> call = loginService.loginRequest(phoneNum, lastConnectTime, instanceId);
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

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
