package com.swmaestro.hangman_together.rest;

import java.util.HashMap;

import retrofit2.Retrofit;

public class ServiceSupplier {
    final Retrofit retrofit;
    final HashMap<String, Object> serviceHolder;

    ServiceSupplier(Retrofit retrofit) {
        this.retrofit = retrofit;
        serviceHolder = new HashMap<>();
    }

    <T> T get(Class<T> serviceClass) {
        String holderKey = serviceClass.getName();

        T service;
        if (serviceHolder.containsKey(holderKey)) {
            service = (T) serviceHolder.get(holderKey);
        } else {
            service = retrofit.create(serviceClass);
            serviceHolder.put(holderKey, service);
        }

        return service;
    }
}

