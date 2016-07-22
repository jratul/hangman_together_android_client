package com.swmaestro.hangman_together.rest.home;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HomeService {
    @FormUrlEncoded
    @POST("/home")
    Call<JsonObject> homeRequest(@Field("phoneNum") String phoneNum);
}
