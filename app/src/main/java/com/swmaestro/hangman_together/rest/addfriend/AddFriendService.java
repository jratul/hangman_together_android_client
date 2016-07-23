package com.swmaestro.hangman_together.rest.addfriend;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddFriendService {
    @FormUrlEncoded
    @POST("/addfriend")
    Call<JsonObject> addFriendRequest(@Field("phoneNum") String phoneNum, @Field("friendNickname") String friendNickname);
}
