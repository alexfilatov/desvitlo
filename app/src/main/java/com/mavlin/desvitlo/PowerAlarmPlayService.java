package com.mavlin.desvitlo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PowerAlarmPlayService extends Service {
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

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
        }
        return Service.START_STICKY;
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

}



