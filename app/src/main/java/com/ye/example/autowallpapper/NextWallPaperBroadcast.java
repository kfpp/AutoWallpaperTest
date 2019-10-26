package com.ye.example.autowallpapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ye.example.autowallpapper.presenters.AutoChangePresenter;
import com.ye.example.autowallpapper.presenters.RandomNextPaperPresenter;
import com.ye.example.autowallpapper.presenters.WorkingDayModePresenter;
import com.ye.example.autowallpapper.presenters.base.IBaseShowPresenter;

public class NextWallPaperBroadcast extends BroadcastReceiver {
    public static final String ACTION_CLICKED = "com.ye.example.notification_click_broadcast";
    private static boolean sIsDealing = false;

    private RandomNextPaperPresenter mRandomPresenter;
    private AutoChangePresenter mAutoChangePresenter;
    public NextWallPaperBroadcast() {
        mRandomPresenter = new RandomNextPaperPresenter();
        IBaseShowPresenter presenter = new WorkingDayModePresenter();
        mAutoChangePresenter = new AutoChangePresenter(mRandomPresenter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_CLICKED.equals(action)) {
            if (sIsDealing) {
                Log.d("yyyy", "ignore action, wall paper is dealing!");
            } else {
                sIsDealing = true;
                mAutoChangePresenter.changeImmediately();
                sIsDealing = false;
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            mAutoChangePresenter.checkAndChange();
        }
    }



}
