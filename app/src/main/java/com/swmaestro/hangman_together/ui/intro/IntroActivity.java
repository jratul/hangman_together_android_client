package com.swmaestro.hangman_together.ui.intro;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.checkid.CheckIdResponse;
import com.swmaestro.hangman_together.rest.checkid.CheckIdService;
import com.swmaestro.hangman_together.rest.join.JoinService;
import com.swmaestro.hangman_together.rest.login.LoginResponse;
import com.swmaestro.hangman_together.rest.login.LoginService;
import com.swmaestro.hangman_together.ui.main.MainActivity;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class IntroActivity extends AppCompatActivity {

    @BindString(R.string.intro_value_nickname_dialog_title) String valueDialogTitle;
    @BindString(R.string.intro_value_nickname_dialog_message) String valueDialogMessage;
    @BindString(R.string.intro_value_nickname_dialog_btn_ok) String valueDialogBtnOk;
    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;
    @BindString(R.string.intro_value_login_failure) String valueLoginFailureMessage;
    @BindString(R.string.intro_value_join_failure) String valueJoinFailureMessage;
    @BindString(R.string.intro_value_permission_should) String valuePermissionShouldMessage;
    @BindString(R.string.intro_value_join_nickname_already_exist) String valueJoinNicknameAlreadyExist;
    @BindString(R.string.intro_value_facebook_login_cancel) String valueFacebookLoginCancelMessage;
    @BindString(R.string.intro_value_facebook_login_failure) String valueFacebookLoginFailureMessage;
    @BindString(R.string.intro_value_no_phone_number) String valueNoPhoneNumberMessage;

    @BindView(R.id.intro_btn_facebook_login) LoginButton btnFacebookLogin;

    static Unbinder butterKnifeUnbinder;

    private Context mContext;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    private static final int READ_SMS = 101;
    private static final int READ_PHONE_STATE = 102;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mContext = getApplicationContext();

        butterKnifeUnbinder = ButterKnife.bind(this);

        checkPermission();

        initActivity();
    }

    private void initActivity() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(mContext,"fb login success", Toast.LENGTH_SHORT).show();
                onBtnLoginClicked();
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext,valueFacebookLoginCancelMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mContext,valueFacebookLoginFailureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_login) void onBtnLoginClicked() {
        checkPermission();

        if(ContextCompat.checkSelfPermission(IntroActivity.this,
                Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telManager = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
            String phoneNum = telManager.getLine1Number();
            if(phoneNum.equals("") || phoneNum.isEmpty()) {
                Toast.makeText(mContext,valueNoPhoneNumberMessage, Toast.LENGTH_SHORT).show();
                loginManager.logOut();
            } else {
                checkIdExistAndMemberProcess(phoneNum);
            }
        }
    }


    private void createNickName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(valueDialogTitle);
        alert.setMessage(valueDialogMessage);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(valueDialogBtnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = input.getText().toString();
                if(value.trim().equals("") || value.isEmpty()) {
                    Toast.makeText(mContext, valueDialogMessage, Toast.LENGTH_SHORT).show();
                } else {
                    TelephonyManager telManager = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
                    String phoneNum = telManager.getLine1Number();
                    requestJoin(phoneNum, value, getLastConnectTime());
                }
            }
        });

        alert.show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_facebook_login) void onBtnFacebookLoginClicked() {
        loginManager.logInWithPublishPermissions(this,null);
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        if(butterKnifeUnbinder!=null) butterKnifeUnbinder.unbind();
    }

    private void startMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void checkIdExistAndMemberProcess(String phoneNum) {
        try {
            CheckIdService checkIdService = RetrofitManager.getInstance().getService(CheckIdService.class);
            Call<JsonObject> call = checkIdService.checkIdRequest(phoneNum);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        CheckIdResponse obj = new CheckIdResponse();
                        obj = gson.fromJson(resultRaw, CheckIdResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(responseString.equals("y")) {
                        requestLogin(phoneNum, getLastConnectTime());
                    } else if(responseString.equals("n")) {
                        createNickName();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
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
                            return;
                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            Log.d("intro", e.getMessage());
        }
    }

    private void requestJoin(String phoneNum, String nickname, String lastConnectTime) {
        try {
            JoinService joinService = RetrofitManager.getInstance().getService(JoinService.class);
            Call<JsonObject> call = joinService.loginRequest(phoneNum, nickname, lastConnectTime);
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
                            Util.savePreferences(mContext, HangmanData.KEY_USER_PHONE_NUM, phoneNum);
                            Util.savePreferences(mContext, HangmanData.KEY_USER_NICKNAME, nickname);

                            startMainActivity();
                            finish();
                        } else if(responseString.equals("a")) {
                            Toast.makeText(mContext, valueJoinNicknameAlreadyExist, Toast.LENGTH_SHORT).show();
                            //loginManager.logOut();
                            return;
                        } else if(responseString.equals("n")) {
                            Toast.makeText(mContext, valueJoinFailureMessage, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            Log.d("intro", e.getMessage());
        }
    }
    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(IntroActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(mContext, valuePermissionShouldMessage, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(IntroActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        READ_SMS);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(IntroActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
/*
        if (ContextCompat.checkSelfPermission(IntroActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(IntroActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
            case READ_PHONE_STATE: {
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }

    @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
