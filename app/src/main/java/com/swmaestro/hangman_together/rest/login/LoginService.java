package com.swmaestro.hangman_together.rest.login;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("/login")
    Call<JsonObject> loginRequest(@Field("phoneNum") String phoneNum);
}
