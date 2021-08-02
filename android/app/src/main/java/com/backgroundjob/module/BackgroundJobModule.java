package com.backgroundjob.module;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.backgroundjob.BackgroundJobService;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.jstasks.HeadlessJsTaskRetryPolicy;
import com.facebook.react.jstasks.LinearCountingRetryPolicy;

import javax.annotation.Nonnull;

public class BackgroundJobModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "BackgroundJob";
    private static Intent service;
    private static Bundle bundle;

    public BackgroundJobModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        this.service = new Intent(reactContext, BackgroundJobService.class);
        putExtras();
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startService() {
        System.out.println(REACT_CLASS + " started!");
        getReactApplicationContext().startService(this.service);
    }

    @ReactMethod
    public void stopService() {
        System.out.println(REACT_CLASS + " stopped!");
        getReactApplicationContext().stopService(this.service);
    }

    private void putExtras(){
        bundle = new Bundle();
        bundle.putString("foo", "bar");
        this.service.putExtras(bundle);
    }

    @ReactMethod
    private void putExtras(String name, String value){
        this.service.putExtra(name,value);
    }
}
