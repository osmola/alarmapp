package com.alarmapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String DEFAULT_ALARM_TEXT = "Alarm has been set!";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text") != "" ? intent.getStringExtra("text") : DEFAULT_ALARM_TEXT;
        //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        boolean vibrate = intent.getBooleanExtra("vibrate", false);
        SendNotification(MainActivity.class, context, text, vibrate);
    }

    private static void SendNotification(Class activityClass, Context context, String title, boolean vibrate) {
      Intent startMainActivity = new Intent(context, activityClass);
      PendingIntent myIntent = PendingIntent.getActivity(context, 0, startMainActivity, 0);

      int currentApiVersion = android.os.Build.VERSION.SDK_INT;

      int NOTIFICATION_ID = 234;
      String CHANNEL_ID = "my_channel_01";
      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

      if (currentApiVersion >= android.os.Build.VERSION_CODES.O) {
        CharSequence name = "my_channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.enableLights(true);
        mChannel.setLightColor(android.graphics.Color.RED);
        if (vibrate) {
          mChannel.enableVibration(true);
          mChannel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        mChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(mChannel);
      }

      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setLights(android.graphics.Color.RED, 3000, 3000);

      if (vibrate) {
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
      }

      if (currentApiVersion >= android.os.Build.VERSION_CODES.KITKAT_WATCH){
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, title, myIntent).build();
        builder.addAction(action);
      } else {
        builder.addAction(R.mipmap.ic_launcher, title, myIntent);
      }

      notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
