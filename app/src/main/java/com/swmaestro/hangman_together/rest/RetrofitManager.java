package com.swmaestro.hangman_together.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class RetrofitManager {
    protected static final String BASE_URL = "http://52.78.70.54:5000";

    private static RetrofitManager instance;

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }

        return instance;
    }

    private final ServiceSupplier serviceSupplier;

    protected RetrofitManager() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        serviceSupplier = new ServiceSupplier(retrofit);
    }

    public <T> T getService(Class<T> serviceClass) {
        return serviceSupplier.get(serviceClass);
    }
}