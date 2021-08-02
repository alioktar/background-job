package com.backgroundjob;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;

import com.backgroundjob.receiver.ActionReceiver;
import com.backgroundjob.service.BackgroundJobEventService;
import com.facebook.react.HeadlessJsTaskService;

import java.lang.reflect.Method;

public class BackgroundJobService extends Service {

    private static final int SERVICE_NOTIFICATION_ID = 12345;
    private static final String CHANNEL_ID = "BACKGROUNDJOBS";
    private String val;

    private Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            System.out.println("Runnable");
            Context context = getApplicationContext();
            Intent intent = new Intent(context, BackgroundJobEventService.class);
            intent.putExtra("Fooo", val);
            context.startService(intent);
            HeadlessJsTaskService.acquireWakeLockNow(context);
            handler.postDelayed(this, 2000);
        }
    };

    public BackgroundJobService() {
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "BACKGROUNDJOBS", importance);
            channel.setDescription("BACKGROUND JOBS CHANNEL");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnableCode);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.val = (String) extras.get("foo");
            System.out.println("on start command : " + val);
        }
        this.handler.post(this.runnableCode);
        createNotificationChannel();
        showNotificationWithButton();
        return START_STICKY;
    }

    private void showNotification(){
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Background Job Service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    private void showNotificationWithButton(){
        Intent notificationIntent = new Intent(getApplicationContext(), ActionReceiver.class);
        notificationIntent.setAction("YES");
        notificationIntent.putExtra("testAction","TestActionName");
        PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Background Job Service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .addAction(R.mipmap.ic_launcher_round,"YES",contentIntent)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);
    }
}
