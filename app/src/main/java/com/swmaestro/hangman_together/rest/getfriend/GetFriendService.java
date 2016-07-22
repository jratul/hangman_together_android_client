package com.swmaestro.hangman_together.rest.getfriend;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetFriendService {
    @FormUrlEncoded
    @POST("/getfriend")
    Call<JsonObject> getFriendRequest(@Field("phoneNum") String phoneNum);
}
