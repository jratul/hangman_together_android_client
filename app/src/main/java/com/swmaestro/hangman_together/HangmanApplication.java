package com.swmaestro.hangman_together;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class HangmanApplication extends Application {
    private static HangmanApplication appInstance;

    public static HangmanApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        Fresco.initialize(appInstance);
    }
}