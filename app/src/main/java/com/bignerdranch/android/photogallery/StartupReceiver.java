package com.bignerdranch.android.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by olo35 on 07.07.2016.
 */
public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartUpReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Received Broadcast intent:" + intent.getAction());

        boolean isOn = QueryPreferences.isAlaramOn(context);
        PollService.setServiceAlarm(context,isOn);
    }
}
