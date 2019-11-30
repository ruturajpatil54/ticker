package com.rrd.ticker.notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rrd.ticker.R;

public class NotificationHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationTypes sound = NotificationTypes.valueOf(intent.getExtras().getString("SOUND"));
        int soundId;
        switch (sound) {
            case BEEP: soundId = R.raw.beep1;
                break;
            case BEEP_TWICE:soundId = R.raw.beep2;
                break;
            case HOUR_BEEP: soundId = R.raw.hour_beep;
                break;
            default: soundId = R.raw.chime2;
                break;
        }
        NotifierService.play(context, soundId);
    }
}
