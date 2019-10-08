package com.example.apptimetracker;

import android.app.NotificationChannel;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.provider.AlarmClock;
import android.view.View;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent ;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "GET OFF INSTA", Toast.LENGTH_LONG).show();
        Calendar rightNow = Calendar.getInstance();
        Intent inttent=new Intent(AlarmClock.ACTION_SET_ALARM);
        Toast.makeText(context, "Creating  ", Toast.LENGTH_LONG).show();
//
        intent.putExtra(AlarmClock.EXTRA_HOUR,rightNow.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(AlarmClock.EXTRA_MINUTES,rightNow.get(Calendar.MINUTE)+1);
        Toast.makeText(context, "Created Alarm ", Toast.LENGTH_LONG).show();
        context.startActivity(inttent);




        }

    }
