package com.asalazar.alex.smsrecords;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sjand on 6/26/2016.
 */
public class StartServiceOnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context, SendToDatabaseBackgroundService.class);
        context.startService(startService);
    }
}
