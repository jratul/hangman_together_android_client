package com.swmaestro.hangman_together.rest.endgame;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EndGameService {
    @FormUrlEncoded
    @POST("/endgame")
    Call<JsonObject> endGameRequest(@Field("phoneNum") String phoneNum, @Field("score") int score);
}
