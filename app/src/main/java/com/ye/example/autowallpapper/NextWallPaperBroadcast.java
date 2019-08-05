package com.ye.example.autowallpapper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.ye.example.autowallpapper.presenters.AutoChangePresenter;
import com.ye.example.autowallpapper.presenters.RandomNextPaperPresenter;
import com.ye.example.autowallpapper.utils.GlobalData;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NextWallPaperBroadcast extends BroadcastReceiver {
    public static final String ACTION_NAME = "com.ye.example.notification_click_broadcast";
    private static boolean sIsDealing = false;

    private RandomNextPaperPresenter mRandomPresenter;
    private AutoChangePresenter mAutoChangePresenter;
    public NextWallPaperBroadcast() {
        mRandomPresenter = new RandomNextPaperPresenter();
        mAutoChangePresenter = new AutoChangePresenter(mRandomPresenter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_NAME.equals(action)) {
            if (sIsDealing) {
                Log.d("yyyy", "ignore action, wall paper is dealing!");
            } else {
                sIsDealing = true;
                mRandomPresenter.dealShowNext(context);
                sIsDealing = false;
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            mAutoChangePresenter.checkAndChange(context);
        }
    }



}
