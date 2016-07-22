package com.swmaestro.hangman_together.intro;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.main.MainActivity;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.checkid.CheckIdResponse;
import com.swmaestro.hangman_together.rest.checkid.CheckIdService;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class IntroActivity extends AppCompatActivity {

    @BindString(R.string.intro_value_nickname_dialog_title) String valueDialogTitle;
    @BindString(R.string.intro_value_nickname_dialog_message) String valueDialogMessage;
    @BindString(R.string.intro_value_nickname_dialog_btn_ok) String valueDialogBtnOk;
    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;

    static Unbinder butterKnifeUnbinder;

    private Context mContext;

    private static final int READ_SMS = 101;
    private static final int READ_PHONE_STATE = 102;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mContext = getApplicationContext();

        butterKnifeUnbinder = ButterKnife.bind(this);

        checkPermission();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_login) void onBtnLoginClicked() {
        TelephonyManager telManager = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        String phoneNum = telManager.getLine1Number();

        checkIdExistAndMemberProcess(phoneNum);
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
                //TODO 닉네임 체크 후 서버와 통신 후 중복 검사
                startMainActivity();
                finish();
            }
        });

        alert.show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_facebook_login) void onBtnFacebookLoginClicked() {

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
                        Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(responseString.equals("y")) {
                        requestLogin();
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

    private void requestLogin() {
        //TODO : 로그인 요청

        startMainActivity();
    }

    private void requestJoin() {
        //TODO : 가입 요청
    }
    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(IntroActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,
                    Manifest.permission.READ_SMS)) {

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
}
