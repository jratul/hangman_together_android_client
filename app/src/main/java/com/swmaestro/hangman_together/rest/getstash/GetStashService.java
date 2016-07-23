package com.swmaestro.hangman_together.rest.getstash;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetStashService {
    @FormUrlEncoded
    @POST("/getstash")
    Call<JsonObject> getStashRequest(@Field("phoneNum") String phoneNum);
}
