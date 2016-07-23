package com.swmaestro.hangman_together.rest.givecandy;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GiveCandyService {
    @FormUrlEncoded
    @POST("/givecandy")
    Call<JsonObject> giveCandyRequest(@Field("phoneNum") String phoneNum, @Field("friendNickname") String friendNickname);
}
