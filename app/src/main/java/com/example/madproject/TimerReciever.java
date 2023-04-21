package com.example.madproject;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class TimerReciever extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, Pomodoro.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TimerNotification")
                .setSmallIcon(R.mipmap.launch_icon)
                .setContentTitle("Timer Update!!!")
                .setContentText("Scheduled Pomodoro Completed")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.BADGE_ICON_LARGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(101, builder.build());
        }


    }
}
