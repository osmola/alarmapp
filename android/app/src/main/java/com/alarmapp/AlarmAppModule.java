package com.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.Calendar;

public class AlarmAppModule extends ReactContextBaseJavaModule {
    public AlarmAppModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native
    }

    @Override
    public String getName() {
        return "AlarmApp";
    }

    @ReactMethod
    public void removeAlarm(String requestId,
                            String hours,
                            String minutes,
                            boolean repeating,
                            boolean vibrate,
                            String text) {

        Context context = getReactApplicationContext();
        Intent broadcastedIntent = new Intent(context, AlarmReceiver.class);
        broadcastedIntent.putExtra("vibrate", vibrate);
        broadcastedIntent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(requestId), broadcastedIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes));
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    @ReactMethod
    public void setAlarm(String requestId,
                         String hours,
                         String minutes,
                         boolean repeating,
                         boolean vibrate,
                         String text) {

        Context context = getReactApplicationContext();
        Intent broadcastedIntent = new Intent(context, AlarmReceiver.class);
        broadcastedIntent.putExtra("vibrate", vibrate);
        broadcastedIntent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(requestId), broadcastedIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes));
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (repeating == true) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
