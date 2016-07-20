package com.swmaestro.hangman_together.intro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.main.MainActivity;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.checkid.CheckIdModel;
import com.swmaestro.hangman_together.rest.checkid.CheckIdService;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IntroActivity extends AppCompatActivity {

    @BindString(R.string.intro_value_nickname_dialog_title) String valueDialogTitle;
    @BindString(R.string.intro_value_nickname_dialog_message) String valueDialogMessage;
    @BindString(R.string.intro_value_nickname_dialog_btn_ok) String valueDialogBtnOk;

    static Unbinder butterKnifeUnbinder;

    private Context mContext;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mContext = getApplicationContext();

        butterKnifeUnbinder = ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_login) void onBtnLoginClicked() {
        //TODO : 서버에서 아이디가 있는지 확인
        TelephonyManager telManager = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        String phoneNum = telManager.getLine1Number();

        if (checkIdExist(phoneNum)) {
            //TODO: 로그인
        } else {
            createNickName();
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

    boolean checkIdExist(String phoneNum) {
        CheckIdService checkIdService = RetrofitManager.getInstance().getService(CheckIdService.class);
        Observable<CheckIdModel> call = checkIdService.checkIdRequest(phoneNum);
        Subscription subscription = call.observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    //TODO : LOADING
                })
                .doOnCompleted(() -> {
                    //TODO : COMPLETE
                })
                .doOnError(throwable -> {
                    //TODO : TOAST, FALSE
                })
                .subscribe(checkModel -> {
                    //Do something with response
                });
        return true;
    }
}
