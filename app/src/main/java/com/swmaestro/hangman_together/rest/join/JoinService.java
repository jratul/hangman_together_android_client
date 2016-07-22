package com.swmaestro.hangman_together.rest.join;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JoinService {
    @FormUrlEncoded
    @POST("/join")
    Call<JsonObject> loginRequest(@Field("phoneNum") String phoneNum, @Field("nickname") String nickname, @Field("lastConnectTime") String lastConnectTime);
}
