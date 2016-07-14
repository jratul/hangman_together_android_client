package com.swmaestro.hangman_together.intro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.main.MainActivity;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IntroActivity extends AppCompatActivity {

    @BindString(R.string.intro_value_nickname_dialog_title) String valueDialogTitle;
    @BindString(R.string.intro_value_nickname_dialog_message) String valueDialogMessage;
    @BindString(R.string.intro_value_nickname_dialog_btn_ok) String valueDialogBtnOk;

    static Unbinder butterKnifeUnbinder;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        butterKnifeUnbinder = ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.intro_btn_login) void onBtnLoginClicked() {
        //TODO : 서버에서 아이디가 있는지 확인
        createNickName();
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
}
