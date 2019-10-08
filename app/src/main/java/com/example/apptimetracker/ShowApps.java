package com.example.apptimetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.AlarmManager;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import 	java.util.Calendar;


import android.app.PendingIntent ;


public class ShowApps extends AppCompatActivity {
    private static PendingIntent pendingAlarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_apps);
        TextView textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String message = intent.getStringExtra("R_APPS");
        textView.setText(message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Context context=getApplicationContext();
        if(message.equals("Instagram open"))
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

////            Toast.makeText(context, "GET OFF INSTA", Toast.LENGTH_LONG).show();
//            Calendar rightNow = Calendar.getInstance();
//
////            Toast.makeText(context, String.valueOf(rightNow.get(Calendar.MINUTE)), Toast.LENGTH_LONG).show();
//            Intent inttent=new Intent(AlarmClock.ACTION_SET_ALARM);
//
////
//            intent.putExtra(AlarmClock.EXTRA_HOUR,rightNow.get(Calendar.HOUR_OF_DAY));
//            rightNow.add(Calendar.MINUTE,2);
//            Toast.makeText(context, String.valueOf(rightNow.get(Calendar.MINUTE)), Toast.LENGTH_LONG).show();
//            intent.putExtra(AlarmClock.EXTRA_MINUTES,rightNow.get(Calendar.MINUTE));
//            intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
//            startActivity(inttent);
//
////            Toast.makeText(context, "Created Alarm ", Toast.LENGTH_LONG).show();
////            startAlarm(context);
        }



    }
    public void setAlarm(Context  context,int type, Calendar calendar, long timeInMillis){

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(type, calendar.getTimeInMillis(), timeInMillis, pendingAlarmIntent);


//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
//                AlarmManager.INTERVAL_HALF_HOUR, pendingAlarmIntent);


        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        1000, pendingAlarmIntent);
    }
    public void startAlarm(Context context) {
        Toast.makeText(context, "SETTING ALARM", Toast.LENGTH_LONG).show();
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Date dat = new Date();
//        Calendar cal_alarm = Calendar.getInstance();
//        Calendar cal_now = Calendar.getInstance();
//        Calendar rightNow = Calendar.getInstance();
//        cal_now.setTime(dat);
//        cal_alarm.setTime(dat);
//        cal_alarm.set(Calendar.HOUR_OF_DAY,rightNow.get(Calendar.HOUR_OF_DAY));
//        cal_alarm.set(Calendar.MINUTE,rightNow.get(Calendar.MINUTE));
//        cal_alarm.set(Calendar.MINUTE,rightNow.get(Calendar.SECOND)+5);
//
//        if(cal_alarm.before(cal_now)){
//            cal_alarm.add(Calendar.DATE,1);
//        }
//
//        Intent myIntent = new Intent(context, AlarmReceiver.class);
//        pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//
//        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingAlarmIntent);


        Calendar alarmCalender = Calendar.getInstance();
        Calendar rightNow = Calendar.getInstance();
        alarmCalender.setTimeInMillis(System.currentTimeMillis());
        alarmCalender.set(Calendar.HOUR_OF_DAY, rightNow.get(Calendar.HOUR_OF_DAY));       // hour=07
        alarmCalender.set(Calendar.MINUTE, rightNow.get(Calendar.MINUTE));          // minute=01
        alarmCalender.set(Calendar.SECOND, rightNow.get(Calendar.SECOND)+5);          // second=0

        setAlarm(context,AlarmManager.RTC_WAKEUP, alarmCalender, AlarmManager.INTERVAL_DAY);
    }

}
