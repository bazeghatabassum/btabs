package com.example.apptimetracker;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.util.LinkedList;

public class MJobExecutor extends AsyncTask<Void,Void,Void> {

    public  static Context context;

    public void setContext(Context c)
    {
        context=c;
    }


    @Override
    protected Void doInBackground(Void ... voids) {


        Toast.makeText(context, "Async Task Started", Toast.LENGTH_LONG).show();

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
            long start = System.currentTimeMillis();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getPackageName().equals("com.instagram.android")) {
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        val += event.getPackageName() + " ";
                        Long open = (event.getTimeStamp());
                        String t = String.valueOf(event.getTimeStamp());
                        val += "Opened : " + t + "\n";
                        if (open > maxOpen)
                            maxOpen = open;

                    }
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                        val += event.getPackageName() + " ";
                        String t = String.valueOf(event.getTimeStamp());
                        val += "Closed : " + t + "\n";
                        Long close = ((event.getTimeStamp()));
                        if (close > maxClose)
                            maxClose = close;

                    }
                }
            }
            Toast.makeText(context, String.valueOf(maxOpen - maxClose) + " ---- " + String.valueOf(System.currentTimeMillis() - maxOpen), Toast.LENGTH_LONG).show();
            if (maxOpen - maxClose > 0) {
                long diff = System.currentTimeMillis() - maxOpen;
                if (diff > (1000 * 60)) {
                    Toast.makeText(context, String.valueOf(diff / 1000), Toast.LENGTH_LONG).show();
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                    r.play();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    r.stop();

                }
            }



        }
        return null;
    }

}

