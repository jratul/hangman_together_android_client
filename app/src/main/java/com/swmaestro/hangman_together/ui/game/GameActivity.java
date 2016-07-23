package com.swmaestro.hangman_together.ui.game;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swmaestro.hangman_together.R;
import com.swmaestro.hangman_together.common.HangmanData;
import com.swmaestro.hangman_together.common.Util;
import com.swmaestro.hangman_together.rest.RetrofitManager;
import com.swmaestro.hangman_together.rest.endgame.EndGameResponse;
import com.swmaestro.hangman_together.rest.endgame.EndGameService;
import com.swmaestro.hangman_together.rest.startgame.StartGameResponse;
import com.swmaestro.hangman_together.rest.startgame.StartGameService;
import com.swmaestro.hangman_together.ui.base.BaseToolbarActivity;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class GameActivity extends BaseToolbarActivity implements View.OnClickListener{
    @BindString(R.string.intro_value_connection_error) String valueConnectionErrorMessage;
    @BindString(R.string.game_value_win_score) String valueWinScoreComment;
    @BindString(R.string.game_value_lose_score) String valueLoseScoreComment;
    @BindString(R.string.game_value_game_end) String valueGameEndMessage;
    @BindString(R.string.game_value_no_candy) String valueNoCandyMessage;
    @BindString(R.string.game_value_game_exit_dialog_title) String valueDialogTitle;
    @BindString(R.string.game_value_game_exit_dialog_message) String valueDialogMessage;
    @BindString(R.string.game_value_game_exit_dialog_btn_ok) String valueDialogBtnOk;
    @BindString(R.string.game_value_game_exit_dialog_btn_continue) String valueDialogBtnContinue;

    @BindView(R.id.game_tv_spell_blank_01) TextView tvBlank_01;
    @BindView(R.id.game_tv_spell_blank_02) TextView tvBlank_02;
    @BindView(R.id.game_tv_spell_blank_03) TextView tvBlank_03;
    @BindView(R.id.game_tv_spell_blank_04) TextView tvBlank_04;
    @BindView(R.id.game_tv_spell_blank_05) TextView tvBlank_05;
    @BindView(R.id.game_tv_spell_blank_06) TextView tvBlank_06;
    @BindView(R.id.game_tv_spell_blank_07) TextView tvBlank_07;
    @BindView(R.id.game_tv_spell_blank_08) TextView tvBlank_08;
    @BindView(R.id.game_tv_spell_blank_09) TextView tvBlank_09;
    @BindView(R.id.game_tv_spell_blank_10) TextView tvBlank_10;

    @BindView(R.id.game_btn_alpha_a) Button btnAlphaA;
    @BindView(R.id.game_btn_alpha_b) Button btnAlphaB;
    @BindView(R.id.game_btn_alpha_c) Button btnAlphaC;
    @BindView(R.id.game_btn_alpha_d) Button btnAlphaD;
    @BindView(R.id.game_btn_alpha_e) Button btnAlphaE;
    @BindView(R.id.game_btn_alpha_f) Button btnAlphaF;
    @BindView(R.id.game_btn_alpha_g) Button btnAlphaG;
    @BindView(R.id.game_btn_alpha_h) Button btnAlphaH;
    @BindView(R.id.game_btn_alpha_i) Button btnAlphaI;
    @BindView(R.id.game_btn_alpha_j) Button btnAlphaJ;
    @BindView(R.id.game_btn_alpha_k) Button btnAlphaK;
    @BindView(R.id.game_btn_alpha_l) Button btnAlphaL;
    @BindView(R.id.game_btn_alpha_m) Button btnAlphaM;
    @BindView(R.id.game_btn_alpha_n) Button btnAlphaN;
    @BindView(R.id.game_btn_alpha_o) Button btnAlphaO;
    @BindView(R.id.game_btn_alpha_p) Button btnAlphaP;
    @BindView(R.id.game_btn_alpha_q) Button btnAlphaQ;
    @BindView(R.id.game_btn_alpha_r) Button btnAlphaR;
    @BindView(R.id.game_btn_alpha_s) Button btnAlphaS;
    @BindView(R.id.game_btn_alpha_t) Button btnAlphaT;
    @BindView(R.id.game_btn_alpha_u) Button btnAlphaU;
    @BindView(R.id.game_btn_alpha_v) Button btnAlphaV;
    @BindView(R.id.game_btn_alpha_w) Button btnAlphaW;
    @BindView(R.id.game_btn_alpha_x) Button btnAlphaX;
    @BindView(R.id.game_btn_alpha_y) Button btnAlphaY;
    @BindView(R.id.game_btn_alpha_z) Button btnAlphaZ;

    @BindView(R.id.game_img_hangman) ImageView imgvHangman;
    @BindView(R.id.game_layout_win_message) LinearLayout layoutWinMessage;
    @BindView(R.id.game_layout_lose_message) LinearLayout layoutLoseMessage;
    @BindView(R.id.game_tv_win_score) TextView tvWinScore;
    @BindView(R.id.game_tv_lose_score) TextView tvLoseScore;

    private Context mContext;

    static Unbinder butterKnifeUnbinder;

    private String gameWord;
    private int wordLength;
    private int restBlank;
    private int score;
    private boolean isPlaying;

    private ArrayList<String> spellList;
    private ArrayList<TextView> tvBlank;
    private ArrayList<Button> spellBtnList;
    private int gameLife;
    private ArrayList<Boolean> pushAvailable;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        butterKnifeUnbinder = ButterKnife.bind(this);
        mContext = getApplicationContext();

        initActivity();
    }

    private void initActivity() {
        enableHomeAsUp();

        isPlaying = true;
        gameLife = 6;
        score = 0;
        spellList = new ArrayList<String>();
        tvBlank = new ArrayList<TextView>();
        //pushAvailable = new LinkedHashMap<String, Boolean>();
        pushAvailable = new ArrayList<Boolean>();
        spellBtnList = new ArrayList<Button>();

        initData();

        for(int i=0;i<26;i++) {
            spellBtnList.get(i).setOnClickListener(this);
        }

        requestStartGame(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM));
    }



    private void requestStartGame(String phoneNum) {
        try {
            StartGameService startGameService = RetrofitManager.getInstance().getService(StartGameService.class);
            Call<JsonObject> call = startGameService.startGameRequest(phoneNum, Util.getCurrentTime());
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        StartGameResponse obj = new StartGameResponse();
                        obj = gson.fromJson(resultRaw, StartGameResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            gameWord = obj.getGameWord();
                            //Toast.makeText(mContext, gameWord, Toast.LENGTH_SHORT).show();
                            wordLength = gameWord.length();
                            restBlank = wordLength;

                            for (int i = wordLength; i < HangmanData.MAXIMUN_WORD_LENGTH; i++) {
                                tvBlank.get(i).setVisibility(View.GONE);
                            }

                            for (int i = 0; i < wordLength; i++) {
                                spellList.add(Character.toString(gameWord.charAt(i)));
                            }
                        } else if(responseString.equals("nc")) {
                            Toast.makeText(mContext, valueNoCandyMessage, Toast.LENGTH_SHORT).show();
                            finish();
                        } else if(responseString.equals("n")) {

                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override protected void onDestroy() {
        super.onDestroy();

        if (butterKnifeUnbinder != null) butterKnifeUnbinder.unbind();
    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                if(isPlaying) {
                    closeGameActivity();
                    return true;
                } else {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    };


    private void checkSpellExistAndProcess(String spell) {
        boolean isExist = false;
        for(int i=0;i<wordLength;i++) {
            if(spellList.get(i).equals(spell)) {
                tvBlank.get(i).setText(spell);
                tvBlank.get(i).setTextColor(Color.RED);
                if(!isExist) isExist = true;
                restBlank--;
                score += HangmanData.PLUS_SCORE;
            }
        }

        if(!isExist) {
            gameLife--;
            score -= HangmanData.MINUS_SCORE;
            setHangmanImage(gameLife);
        }

        if(restBlank == 0) {
            gameWin();
        }

        if(gameLife == 0) {
            gameLose();
        }
    }

    private void setHangmanImage(int gameLife) {
        switch(gameLife) {
            case 5:
                imgvHangman.setImageResource(R.drawable.hang_02);
                break;
            case 4:
                imgvHangman.setImageResource(R.drawable.hang_03);
                break;
            case 3:
                imgvHangman.setImageResource(R.drawable.hang_04);
                break;
            case 2:
                imgvHangman.setImageResource(R.drawable.hang_05);
                break;
            case 1:
                imgvHangman.setImageResource(R.drawable.hang_06);
                break;
            case 0:
                imgvHangman.setImageResource(R.drawable.hang_07);
                break;
        }
    }

    private void gameWin() {
        imgvHangman.setImageResource(R.drawable.hang_vic);
        tvWinScore.setText(score + valueWinScoreComment);
        layoutWinMessage.setVisibility(View.VISIBLE);
        requestEndGame(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM));
    }

    private void gameLose() {
        tvLoseScore.setText((score * (-1)) + valueLoseScoreComment);
        layoutLoseMessage.setVisibility(View.VISIBLE);
        requestEndGame(Util.getPreferences(mContext, HangmanData.KEY_USER_PHONE_NUM));
    }

    private void requestEndGame(String phoneNum) {
        isPlaying = false;
        try {
            EndGameService endGameService = RetrofitManager.getInstance().getService(EndGameService.class);
            Call<JsonObject> call = endGameService.endGameRequest(phoneNum, score);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    String responseString = "n";

                    try {
                        Gson gson = new Gson();
                        String resultRaw = response.body().toString();
                        EndGameResponse obj = new EndGameResponse();
                        obj = gson.fromJson(resultRaw, EndGameResponse.class);
                        responseString = obj.getMessage();
                        //Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();

                        if(responseString.equals("y")) {
                            Toast.makeText(mContext, valueGameEndMessage, Toast.LENGTH_SHORT).show();
                        } else if(responseString.equals("n")) {

                        }
                    } catch(Exception e) {
                        Toast.makeText(mContext, valueConnectionErrorMessage, Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override public void onBackPressed() {
        if(isPlaying) {
            closeGameActivity();
        } else {
            super.onBackPressed();
        }
    }

    void closeGameActivity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(valueDialogTitle);
        alert.setMessage(valueDialogMessage);

        alert.setPositiveButton(valueDialogBtnOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alert.setNegativeButton(valueDialogBtnContinue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alert.show();
    }

    private void initData() {
        tvBlank.add(tvBlank_01);
        tvBlank.add(tvBlank_02);
        tvBlank.add(tvBlank_03);
        tvBlank.add(tvBlank_04);
        tvBlank.add(tvBlank_05);
        tvBlank.add(tvBlank_06);
        tvBlank.add(tvBlank_07);
        tvBlank.add(tvBlank_08);
        tvBlank.add(tvBlank_09);
        tvBlank.add(tvBlank_10);

        for(int i=0;i<26;i++) {
            pushAvailable.add(true);
        }

        spellBtnList.add(btnAlphaA);
        spellBtnList.add(btnAlphaB);
        spellBtnList.add(btnAlphaC);
        spellBtnList.add(btnAlphaD);
        spellBtnList.add(btnAlphaE);
        spellBtnList.add(btnAlphaF);
        spellBtnList.add(btnAlphaG);
        spellBtnList.add(btnAlphaH);
        spellBtnList.add(btnAlphaI);
        spellBtnList.add(btnAlphaJ);
        spellBtnList.add(btnAlphaK);
        spellBtnList.add(btnAlphaL);
        spellBtnList.add(btnAlphaM);
        spellBtnList.add(btnAlphaN);
        spellBtnList.add(btnAlphaO);
        spellBtnList.add(btnAlphaP);
        spellBtnList.add(btnAlphaQ);
        spellBtnList.add(btnAlphaR);
        spellBtnList.add(btnAlphaS);
        spellBtnList.add(btnAlphaT);
        spellBtnList.add(btnAlphaU);
        spellBtnList.add(btnAlphaV);
        spellBtnList.add(btnAlphaW);
        spellBtnList.add(btnAlphaX);
        spellBtnList.add(btnAlphaY);
        spellBtnList.add(btnAlphaZ);
    }

    @Override public void onClick(View view) {
        int viewId = view.getId();
        view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
        view.setClickable(false);

        switch(viewId) {
            case R.id.game_btn_alpha_a:
                checkSpellExistAndProcess("a");
                pushAvailable.set(0, false);
                break;
            case R.id.game_btn_alpha_b:
                checkSpellExistAndProcess("b");
                pushAvailable.set(1, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_c:
                checkSpellExistAndProcess("c");
                pushAvailable.set(2, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_d:
                checkSpellExistAndProcess("d");
                pushAvailable.set(3, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_e:
                checkSpellExistAndProcess("e");
                pushAvailable.set(4, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_f:
                checkSpellExistAndProcess("f");
                pushAvailable.set(5, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_g:
                checkSpellExistAndProcess("g");
                pushAvailable.set(6, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_h:
                checkSpellExistAndProcess("h");
                pushAvailable.set(7, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_i:
                checkSpellExistAndProcess("i");
                pushAvailable.set(8, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_j:
                checkSpellExistAndProcess("j");
                pushAvailable.set(9, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_k:
                checkSpellExistAndProcess("k");
                pushAvailable.set(10, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_l:
                checkSpellExistAndProcess("l");
                pushAvailable.set(11, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_m:
                checkSpellExistAndProcess("m");
                pushAvailable.set(12, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_n:
                checkSpellExistAndProcess("n");
                pushAvailable.set(13, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_o:
                checkSpellExistAndProcess("o");
                pushAvailable.set(14, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_p:
                checkSpellExistAndProcess("p");
                pushAvailable.set(15, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_q:
                checkSpellExistAndProcess("q");
                pushAvailable.set(16, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_r:
                checkSpellExistAndProcess("r");
                pushAvailable.set(17, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_s:
                checkSpellExistAndProcess("s");
                pushAvailable.set(18, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_t:
                checkSpellExistAndProcess("t");
                pushAvailable.set(19, false);
                view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_u:
                checkSpellExistAndProcess("u");
                pushAvailable.set(20, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_v:
                checkSpellExistAndProcess("v");
                pushAvailable.set(21, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_w:
                checkSpellExistAndProcess("w");
                pushAvailable.set(22, false);
                view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_x:
                checkSpellExistAndProcess("x");
                pushAvailable.set(23, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_y:
                checkSpellExistAndProcess("y");
                pushAvailable.set(24, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
            case R.id.game_btn_alpha_z:
                checkSpellExistAndProcess("z");
                pushAvailable.set(25, false);
                //view.setBackgroundResource(R.color.colorBtnAlreadyGaveCandy);
                break;
        }
    }
}
