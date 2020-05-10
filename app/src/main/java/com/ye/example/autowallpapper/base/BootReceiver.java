package com.ye.example.autowallpapper.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.ye.example.autowallpapper.common.Initializer;

/**
 * @author yezhihao 2019-08-05 14:30
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Initializer.initWithStoragePermission(context);
    }
}
