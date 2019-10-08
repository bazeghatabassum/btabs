package com.example.apptimetracker;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.*;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import android.os.CountDownTimer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static long openTime = 0;
    public static long closeTime = 0;
    public static long currentUsageTime = 0;
    public static boolean startTimer=false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();


        Toast.makeText(this, "Service running", Toast.LENGTH_SHORT).show();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Service running", Toast.LENGTH_SHORT).show();
//                handler.postDelayed(this, 5000);
//                checkAppStatus();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                    long currTime = System.currentTimeMillis();
                    long startTime = currTime - (1000 * 3600);
                    UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, currTime);
                    LinkedList<Long> opened = new LinkedList<>();
                    LinkedList<Long> closed = new LinkedList<>();
                    Long maxOpen = -1L;
                    Long maxClose = -1L;
                    String val = "";

                    UsageEvents.Event event = new UsageEvents.Event();
                    usageEvents.getNextEvent(event);
                    long start=System.currentTimeMillis();
                    while (usageEvents.hasNextEvent()) {
                        usageEvents.getNextEvent(event);
                        if (event.getPackageName().equals("com.instagram.android")) {
                            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                                val += event.getPackageName() + " ";
                                Long open = (event.getTimeStamp() ) ;
                                String t = String.valueOf(event.getTimeStamp() );
                                val += "Opened : " + t + "\n";
                                if (open > maxOpen)
                                    maxOpen = open;

                            }
                            if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                                val += event.getPackageName() + " ";
                                String t = String.valueOf(event.getTimeStamp() );
                                val += "Closed : " + t + "\n";
                                Long close = ((event.getTimeStamp() ) );
                                if (close > maxClose)
                                    maxClose = close;

                            }
                        }
                    }
                    Toast.makeText(context, String.valueOf(maxOpen-maxClose) + " ---- " + String.valueOf(System.currentTimeMillis()-maxOpen), Toast.LENGTH_LONG).show();
                    if (maxOpen - maxClose >0) {
                        long diff=System.currentTimeMillis()-maxOpen;
                        if (diff > (1000*50)) {
                            Toast.makeText(context, String.valueOf(diff/1000), Toast.LENGTH_LONG).show();
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            r.stop();

                        }
                    }
//                    long end=System.currentTimeMillis();
//                    Toast.makeText(context, String.valueOf((end-start))+"Start: "+start+" END: "+end, Toast.LENGTH_LONG).show();



//                    Toast.makeText(context, val, Toast.LENGTH_LONG).show();
//
//

                }
                handler.postDelayed(this, 15000);

            }


//
        };
        handler.postDelayed(runnable,5000);
    }
    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacksAndMessages(null);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Do your task here
        return START_STICKY;
    }
    @Override
    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//
//
//        super.onStart(intent, startid);
//        Toast.makeText(this, "Service running", Toast.LENGTH_SHORT).show();
//
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, "Service running", Toast.LENGTH_SHORT).show();
////                handler.postDelayed(this, 5000);
//                checkAppStatus();
////                handler.postDelayed(this, 5000);
//
//            }
//        };
//        handler.postDelayed(runnable, 5000);


//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, "Service running", Toast.LENGTH_SHORT).show();
//
//                checkAppStatus();
//
//            }
//        }, 10000);


    }

    public void checkAppStatus() {
        boolean check=false;
        Toast.makeText(context, "Checking app status", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            long currTime = System.currentTimeMillis();
            long startTime = currTime - (1000 * 3600);
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//            Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(startTime, currTime);
//            if (stats != null) {
//                UsageStats usageStats = stats.get("com.instagram.android");
////                for (UsageStats usageStats : stats) {
//                long TimeInforground = usageStats.getTotalTimeInForeground();
//                long lastTime = usageStats.getLastTimeUsed();
//                String PackageName = usageStats.getPackageName();
//
//                if (PackageName.equals("com.instagram.android")) {
//
//                    int minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
//                    int seconds = (int) (TimeInforground / 1000) % 60;
//                    int hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
//
////                        Toast.makeText(context,hours + "h" + ":" + minutes + "m" + seconds + "s", Toast.LENGTH_LONG).show();                                Log.i("BAC", "PackageName is" + PackageName + "Time is: "
//
//                    Toast.makeText(this, hours + "h" + ":" + minutes + "m" + seconds + "s" + " ms:" + TimeInforground, Toast.LENGTH_LONG).show();
////                        Toast.makeText(context,String.valueOf(lastTime) , Toast.LENGTH_LONG).show();
//
//                }
//            if(((openTime-closeTime)/(1000*60))>=1)
//            {
//                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//                r.play();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                r.stop();
//            }

            if(((currentUsageTime-openTime)/(1000*60))>=1)
            {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r.stop();
            }
            UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, currTime);
            long start=0;
            long end=0;
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getPackageName().equals("com.instagram.android")) {
                    switch (event.getEventType()) {

                        case UsageEvents.Event.MOVE_TO_FOREGROUND:

                            if(startTimer==false) {
                                openTime=System.currentTimeMillis();
                                startTimer = true;
                                currentUsageTime=System.currentTimeMillis();
                            }
                            else {
                                currentUsageTime=System.currentTimeMillis();
                            }
                            Toast.makeText(context,"INSTA MOVED TO FOREGROUND" +String.valueOf((openTime/1000)%60)+" closeTime :"+String.valueOf((currentUsageTime/1000)%60), Toast.LENGTH_LONG).show();
//                            Toast.makeText(context,String.valueOf(check) +String.valueOf(openTime), Toast.LENGTH_LONG).show();


                            break;
                        case UsageEvents.Event.MOVE_TO_BACKGROUND:
                            openTime=System.currentTimeMillis();
                            startTimer=false;
                            Toast.makeText(context,"INSTA CLOSED openTime:" +String.valueOf((openTime/1000)%60)+" closeTime :"+String.valueOf((currentUsageTime/1000)%60), Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

            }

        }
//        handler.post(runnable);
    }


