package com.example.stopwatch_sw;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class StopWatchService extends Service {

    private final int REFRESH_RATE = 100;
    Handler handler = new Handler();
    Intent intent = new Intent("com.stopwatch.timer");
    private long startTimer, elapsedTime;
    private String hours, minutes, seconds;
    private long secs, mins, hrs;
    private boolean isStopped = false;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTimer;
            stopWatchService(elapsedTime);
            handler.postDelayed(this, REFRESH_RATE);
        }
    };

    public void stopWatchService(float time) {

        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

        intent.putExtra("hours", hours);
        intent.putExtra("minutes", minutes);
        intent.putExtra("seconds", seconds);
        sendBroadcast(intent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotificationAndStartForeGround();
        if (isStopped) {
            startTimer = System.currentTimeMillis() - elapsedTime;
        } else {
            startTimer = System.currentTimeMillis();
        }
        handler.removeCallbacks(task);
        handler.postDelayed(task, 0);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(task);
        isStopped = true;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotificationAndStartForeGround() {
        createChannel();
        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(this, "Stopwatch")
                .setContentTitle("Stopwatch Running...")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = null;
        notification = notificationBuilder.build();
        startForeground(120, notification);
    }

    /*
    Create noticiation channel if OS version is greater than or eqaul to Oreo
    */
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Stopwatch", "StopWatch", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Stopwatch Notifications");
            Objects.requireNonNull(this.getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }
}