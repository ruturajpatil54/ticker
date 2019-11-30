package com.rrd.ticker.notifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


public class NotifierService {
    static MediaPlayer player;
    static final String logTag = "NotifierService";
    public static void play(Context context, int resourceId) {
        try{
            if (player != null) {
                player.reset();
            }
            player = MediaPlayer.create(context, resourceId);
            player.start();
        } catch (Exception e) {
            Log.e(logTag, e.getLocalizedMessage());
        }
    }

    public void scheduleAlarms(int interval, final Context context) {
        AsyncTask schedulerTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                int interval = (int) objects[0];
                Context context = (Context) objects[1];

                // schedule notification after every interval minutes until midnight
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                int currentDate = calendar.get(Calendar.DATE);
                int currentMin = calendar.get(Calendar.MINUTE);
                int nextIntervalMin = currentMin - (currentMin % interval) + interval;
                if (nextIntervalMin == 60) {
                    calendar.add(Calendar.HOUR, 1);
                    nextIntervalMin = 0;
                }

                calendar.set(Calendar.MINUTE, nextIntervalMin);

                while (currentDate == calendar.get(Calendar.DATE)){
                    NotificationTypes sound = NotificationTypes.BEEP;
                    currentMin = calendar.get(Calendar.MINUTE);
                    if (currentMin == 0 || currentMin == 60) {
                        sound = NotificationTypes.HOUR_BEEP;
                    } else if (currentMin == 30) {
                        sound = NotificationTypes.BEEP_TWICE;
                    }
                    setAlarm(calendar.getTimeInMillis(), sound.toString(), context);
                    Log.d(logTag, "setAlarm @" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                    calendar.add(Calendar.MINUTE, interval);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            }
        };
        schedulerTask.execute(interval, context);
    }
    public void setAlarm(long time, String sound, Context context) {
        Intent receiverIntent = new Intent(context, NotificationHandler.class);
        receiverIntent.putExtra("SOUND", sound);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, Long.valueOf(time/1000).intValue(), receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time,alarmIntent);
        }
    }
}
