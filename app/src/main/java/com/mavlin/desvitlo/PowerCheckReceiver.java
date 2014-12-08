package com.mavlin.desvitlo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerCheckReceiver extends BroadcastReceiver {

    public PowerCheckReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Intent play_intent = new Intent(context, PowerAlarmPlayService.class);
            play_intent.putExtra(PowerAlarmPlayService.START_PLAY, true);
            context.startService(play_intent);
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            Intent play_intent = new Intent(context, PowerAlarmPlayService.class);
            context.stopService(play_intent);
        }
    }
}
