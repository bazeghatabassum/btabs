package com.example.apptimetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
public static Intent service;
public static int JOB_ID=100;
public static JobScheduler jobScheduler;
public static JobInfo jobInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context=getApplicationContext();
        setContentView(R.layout.activity_main);
        MJobScheduler.setContext(context);
        ComponentName componentName=new ComponentName(this, MJobScheduler.class);
        JobInfo.Builder builder=new JobInfo.Builder(JOB_ID,componentName);
//        builder.setPeriodic(10000);
        builder.setOverrideDeadline(20000);
        jobInfo=builder.build();
        jobScheduler =(JobScheduler) getSystemService(context.JOB_SCHEDULER_SERVICE);
    }

    public static String getApplicationLabel(Context context, String packageName) {

        PackageManager        packageManager = context.getPackageManager();
        List<ApplicationInfo> packages       = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String                label          = null;

        for (int i = 0; i < packages.size(); i++) {

            ApplicationInfo temp = packages.get(i);

            if (temp.packageName.equals(packageName))
                label = packageManager.getApplicationLabel(temp).toString();
        }

        return label;
    }

    private static boolean isSTOPPED(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0);
    }

    private static boolean isSYSTEM(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }



    public String  getActiveApps(Context context) {
//
        String value="";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            long currTime = System.currentTimeMillis();
            long startTime = currTime - (1000 * 3600);
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    startTime, currTime);
            if (stats != null) {
                for (UsageStats usageStats : stats) {
                    long TimeInforground = usageStats.getTotalTimeInForeground();
                    String PackageName = usageStats.getPackageName();

                    if (PackageName.equals("com.instagram.android")) {

                        int minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
                        int seconds = (int) (TimeInforground / 1000) % 60;
                        int hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
//                        Toast.makeText(context,hours + "h" + ":" + minutes + "m" + seconds + "s", Toast.LENGTH_LONG).show();                                Log.i("BAC", "PackageName is" + PackageName + "Time is: "

                        Toast.makeText(this, hours + "h" + ":" + minutes + "m" + seconds + "s", Toast.LENGTH_LONG).show();


                    }



                }
            }

        }


//        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
//
//        {
//            for (ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
//                if(processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND|| processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
//                value+=processInfo.processName+"\n";
//            }
//        }


//        PackageManager pm = context.getPackageManager();
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
////        String value = u.dateStamp(); // basic date stamp
////        value += "---------------------------------\n";
////        value += "Active Apps\n";
////        value += "=================================\n";
//        String value = "";
//        for (ApplicationInfo packageInfo : packages) {
//
//            //system apps! get out
//            if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo)) {
//
//                value += getApplicationLabel(context, packageInfo.packageName) + "\n" + packageInfo.packageName + "\n-----------------------\n";
//
//            }
//        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                if  (mode != AppOpsManager.MODE_ALLOWED)
                    value="Permission Nahi hai";
            } catch (PackageManager.NameNotFoundException e) {
                value="Permission to hai";
            }
            long currTime = System.currentTimeMillis();
            long startTime =currTime - (1000*3600);
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    startTime, currTime);


            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = activityManager.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo task : tasks) {
                value+=task.processName+"\n";
            }
//            for (UsageStats usageStats : stats) {
//                String packageName=usageStats.getPackageName();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    value+=packageName+"\n";
//                    if(packageName.equals("com.instagram.android") && usageStatsManager.isAppInactive(packageName)==false)
//                        value+=packageName+" Instagram open";
//
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////                            value+=packageName+":   "+ (new UsageStats(usageStats).getLastTimeForegroundServiceUsed())+"\n";
////                        }
//                }
//            }

        }
        else
            value="Version chota pad gaya";
        return value;
//        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();

    }

        public void sendMessage(View view) {
//            jobScheduler.schedule(jobInfo);
//            Toast.makeText(getApplicationContext(), "Job Scheduled", Toast.LENGTH_LONG).show();
             service = new Intent(getApplicationContext(), MyForegroundService.class);
            startService(service);
////            bindService(service,mConnection,0);
//        System.out.println("APP STARTED IN BACKGROUND");
//        Toast.makeText(getApplicationContext(), "APP STARTED IN BACKGROUND", Toast.LENGTH_LONG).show();
//
//        String apps=getActiveApps(view.getContext());
//            Intent intent = new Intent(this, ShowApps.class);
//            intent.putExtra("R_APPS", apps);
//            startActivity(intent);





//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
//        LinkedList<String> appNames=new LinkedList<String>();
//        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
//            appNames.add(runningAppProcessInfo.get(i).processName);
//            Toast.makeText(getApplicationContext(), runningAppProcessInfo.get(i).processName, Toast.LENGTH_LONG).show();
////            if(runningAppProcessInfo.get(i).processName.equals("com.the.app.you.are.looking.for")) {
////
////            }
//        }





    }

    public void destroy(View view) {
        stopService(service);





//        Toast.makeText(getApplicationContext(), "APP STARTED IN BACKGROUND", Toast.LENGTH_LONG).show();
//
//        String apps=getActiveApps(view.getContext());
//            Intent intent = new Intent(this, ShowApps.class);
//            intent.putExtra("R_APPS", apps);
//            startActivity(intent);





//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
//        LinkedList<String> appNames=new LinkedList<String>();
//        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
//            appNames.add(runningAppProcessInfo.get(i).processName);
//            Toast.makeText(getApplicationContext(), runningAppProcessInfo.get(i).processName, Toast.LENGTH_LONG).show();
////            if(runningAppProcessInfo.get(i).processName.equals("com.the.app.you.are.looking.for")) {
////
////            }
//        }





    }
}
