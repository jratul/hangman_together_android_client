package com.swmaestro.hangman_together.rest.checkid;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface CheckIdService {
    @GET("/checkid/{phoneNum}")
    Observable<CheckIdModel> checkIdRequest(@Path("phoneNum") String phoneNum);
}
