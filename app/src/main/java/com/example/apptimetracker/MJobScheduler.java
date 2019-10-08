package com.example.apptimetracker;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.widget.Toast;


public class MJobScheduler extends JobService {
    public static Context context;
    public MJobExecutor mJobExecutor;
    public static void setContext(Context c)
    {
        context=c;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        mJobExecutor =new MJobExecutor(){
            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                Toast.makeText(context,"Scheduled Job Run",Toast.LENGTH_LONG).show();
                jobFinished(params,true);
            }
        };
        mJobExecutor.setContext(context);
        Toast.makeText(context,"Starting Job",Toast.LENGTH_LONG).show();
        mJobExecutor.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobExecutor.cancel(true);
        return true;
    }
}
