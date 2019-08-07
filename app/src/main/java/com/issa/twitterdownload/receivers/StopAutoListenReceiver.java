package com.issa.twitterdownload.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopAutoListenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.getApplicationContext().stopService(new Intent(context,AutoListenService.class));
    }
}

