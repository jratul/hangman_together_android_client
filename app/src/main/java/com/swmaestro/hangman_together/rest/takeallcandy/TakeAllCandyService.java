package com.swmaestro.hangman_together.rest.takeallcandy;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TakeAllCandyService {
    @FormUrlEncoded
    @POST("/takeallcandy")
    Call<JsonObject> takeAllCandyRequest(@Field("phoneNum") String phoneNum);
}
