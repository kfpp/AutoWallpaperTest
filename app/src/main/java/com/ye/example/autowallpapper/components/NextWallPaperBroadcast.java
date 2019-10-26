package com.ye.example.autowallpapper.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ye.example.autowallpapper.proxy.broadcastevent.BroadCastEventProxy;

public class NextWallPaperBroadcast extends BroadcastReceiver {
    public static final String ACTION_CLICKED = "com.ye.example.notification_click_broadcast";
    private BroadCastEventProxy mProxy;

    public NextWallPaperBroadcast() {
        mProxy = new BroadCastEventProxy();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_CLICKED.equals(action)) {
            mProxy.onClickedEvent();
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            mProxy.onUserPresentEvent();
        }
    }



}
