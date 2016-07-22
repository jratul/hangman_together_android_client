package com.swmaestro.hangman_together.rest.checkid;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CheckIdService {
    //@GET("/checkid/{phoneNum}")
    //Observable<CheckIdModel> checkIdRequest(@Path("phoneNum") String phoneNum);
    //Call<CheckIdModel> checkId(@Field("phoneNum") String phoneNum);
    //Observable<CheckIdModel> checkIdRequest(@Field("phoneNum") String phoneNum);
    @FormUrlEncoded
    @POST("/checkid")
    Call<JsonObject> checkIdRequest(@Field("phoneNum") String phoneNum);
}
