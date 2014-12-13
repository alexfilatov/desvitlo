package com.mavlin.desvitlo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class PowerAlarmPlayService extends Service {
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private int FOREGROUND_ID = 1337;

    public static String START_PLAY = "START_PLAY";

    public PowerAlarmPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(START_PLAY, false)) {
            play();
//            startForeground(FOREGROUND_ID, buildForegroundNotification(getString(R.string.alarm_notification_message)));
            generateNotification(getApplicationContext(), getString(R.string.alarm_notification_message));
        }
        return Service.START_STICKY;
    }


    private static void generateNotification(Context context, String message){
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.alarming))
                .setContentIntent(intent)
                .setPriority(5) //private static final PRIORITY_HIGH = 5;
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void play() {
        if (!isPlaying) {
            isPlaying = true;

            mediaPlayer = MediaPlayer.create(this, R.raw.bomb_siren); // change this for your file
            mediaPlayer.setLooping(true); // this will make it loop forever
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private void stop() {
        if (isPlaying) {
            isPlaying = false;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    private Notification buildForegroundNotification(String message) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setOngoing(true);

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        b.setContentTitle(getString(R.string.alarming))
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setTicker(getString(R.string.alarming));

        return (b.build());
    }

}



