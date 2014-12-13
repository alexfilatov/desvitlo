package com.mavlin.desvitlo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    private MediaPlayer mediaPlayer;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");

        // creating receiver just for changing text connected/disconnected
        PowerCheckReceiver receiver = new PowerCheckReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                    TextView t = (TextView) findViewById(R.id.textViewPowerState);
                    t.setText(getString(R.string.power_disconnected_value));
                } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                    TextView t = (TextView) findViewById(R.id.textViewPowerState);
                    t.setText(getString(R.string.power_connected_value));
                }
            }
        };
        registerReceiver(receiver, filter);

        // disabling receiver for alarm play
        PackageManager pm = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, PowerCheckReceiver.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                // looks like receiver is not registered
                e.printStackTrace();
            }

            finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        PackageManager pm = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, PowerCheckReceiver.class);

        if (on) {
            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            // stop siren play
            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            // stop play if it is playing
            Intent play_intent = new Intent(getApplicationContext(), PowerAlarmPlayService.class);
            if (play_intent.getBooleanExtra(PowerAlarmPlayService.START_PLAY, true)) {
                stopService(play_intent);
            }
        }
    }

}
