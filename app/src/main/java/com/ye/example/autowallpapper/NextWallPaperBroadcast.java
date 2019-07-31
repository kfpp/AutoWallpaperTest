package com.ye.example.autowallpapper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_NAME.equals(action)) {
            if (sIsDealing) {
                Log.d("yyyy", "ignore action, wall paper is dealing!");
            } else {
                sIsDealing = true;
                dealShowNext(context);
                sIsDealing = false;
            }
        }
    }

    private void dealShowNext(final Context context) {
        Log.d("yyyy", "received");
        List<String> images = GlobalData.getInstance().getImageList();
        if (images == null || images.size() == 0) {
            return;
        }

        final String nextPath = randomPath(images);


        Single<Bitmap> obserable = Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> emitter) throws Exception {
                long timeStart = System.currentTimeMillis();
                Bitmap image = BitmapFactory.decodeFile(nextPath);
                long tiemEnd = System.currentTimeMillis();
                Log.d("yyyy", "decodeFile cost : " + (tiemEnd - timeStart));
                emitter.onSuccess(image);
            }
        });
        obserable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        WallpaperManager wpm = (WallpaperManager) context.getSystemService(
                                Context.WALLPAPER_SERVICE);
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                wpm.setBitmap(bitmap, null, false);
                            } else {
                                wpm.setBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String randomPath(List<String> images) {
        Random random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(images.size());
        return images.get(randomIndex);
    }

}
