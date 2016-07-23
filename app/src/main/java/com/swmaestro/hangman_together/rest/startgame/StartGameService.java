package com.swmaestro.hangman_together.rest.startgame;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StartGameService {
    @FormUrlEncoded
    @POST("/startgame")
    Call<JsonObject> startGameRequest(@Field("phoneNum") String phoneNum, @Field("lastPlayingTime") String lastPlayingTime);
}
