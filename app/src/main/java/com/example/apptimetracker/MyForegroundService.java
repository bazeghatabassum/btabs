package com.example.apptimetracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.LinkedList;

public class MyForegroundService extends Service {
    private static final String MY_TAG = "MT_TAG";
    public Handler handler = null;
    public static Runnable runnable = null;
    public   Thread thread;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public MyForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public boolean checkAppClosed()
    {


        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(getApplicationContext().USAGE_STATS_SERVICE);
        Long currentTime=System.currentTimeMillis();
        Long startTime=currentTime-(1000*50);
        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, currentTime);
        Long maxOpen = -1L;
        Long maxClose = -1L;
        UsageEvents.Event event = new UsageEvents.Event();
        usageEvents.getNextEvent(event);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getPackageName().equals("com.instagram.android")) {
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {

                    Long open = (event.getTimeStamp() ) ;
                    if (open > maxOpen)
                        maxOpen = open;

                }
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    Long close = ((event.getTimeStamp() ) );
                    if (close > maxClose)
                        maxClose = close;

                }
            }
            if(maxClose>=maxOpen) {
                Log.d(MY_TAG,"APP IS CLOSED");
                return true;
            }
        }
        return false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("MY NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        try {


            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.d(MY_TAG, "SERVICE IS STARTING");
                    boolean stop = false;

                    while (stop == false && !thread.isInterrupted()) {
                        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(getApplicationContext().USAGE_STATS_SERVICE);
                        Long currentTime = System.currentTimeMillis();
                        Long startTime = currentTime - (1000 * 60 * 60);
                        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, currentTime);
                        Long maxOpen = -1L;
                        Long maxClose = -1L;
                        UsageEvents.Event event = new UsageEvents.Event();
                        usageEvents.getNextEvent(event);
                        while (usageEvents.hasNextEvent()) {
                            usageEvents.getNextEvent(event);
                            if (event.getPackageName().equals("com.instagram.android")) {
                                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {

                                    Long open = (event.getTimeStamp());
                                    if (open > maxOpen)
                                        maxOpen = open;

                                }
                                if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                                    Long close = ((event.getTimeStamp()));
                                    if (close > maxClose)
                                        maxClose = close;

                                }
                            }
                        }
                        Log.d(MY_TAG, String.valueOf(maxOpen - maxClose));
                        if (maxOpen - maxClose > 0) {
                            long diff = System.currentTimeMillis() - maxOpen;
                            Log.d(MY_TAG, String.valueOf("INSTA OPEN" + diff));
                            if (diff > (1000 *60)) {

                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

                                r.play();

                                stop = true;
                                try {
                                    if (!thread.isInterrupted())
                                        Thread.sleep(5000);
                                    else
                                        break;
                                } catch (InterruptedException e) {
                                    thread.interrupt();
                                    stopForeground(true);
                                    stopSelf();
                                }
                                r.stop();
                            }
                        }
                        if (stop == false) {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                thread.interrupt();
                                stopForeground(true);
                                stopSelf();
                            }
                        }

                    }
                    thread.interrupt();
                    stopForeground(true);
                    stopSelf();
                }
            });

            thread.start();
        }
        catch (Exception e)
        {
            Log.d(MY_TAG,String.valueOf(e));
        }
        return Service.START_NOT_STICKY;





    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void showNotification()
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"channelID");
        builder.setSmallIcon(R.mipmap.ic_launcher).setContentText("This is Service Notification").setContentTitle("Notification");
        Notification notification=builder.build();
        startForeground(123,notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        thread.interrupt();
        stopForeground(true);
        stopSelf();
        Log.d(MY_TAG,"Service Destroyed");
    }
}
