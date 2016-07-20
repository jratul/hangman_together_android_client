package com.swmaestro.hangman_together.rest.login;


import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginService {
    @GET("/login/{phoneNum}")
    Observable<LoginModel> loginRequest(@Path("phoneNum") String phoneNum);
}
