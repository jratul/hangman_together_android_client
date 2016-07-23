package com.swmaestro.hangman_together.rest.takecandy;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TakeCandyService {
    @FormUrlEncoded
    @POST("/takecandy")
    Call<JsonObject> takeCandyRequest(@Field("stashIdx") String stashIdx);
}
