package com.ye.example.autowallpapper.presenters.base;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.ye.example.autowallpapper.base.YEApp;
import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.utils.SpUtil;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseShowPresenter implements IBaseShowPresenter {

    private static final int RESULT_FAILED = 0;

    protected abstract List<String> getDataList();

    protected abstract String getNextShowImgPath();

    @Override
    public void showNextWallPaper() {
        String nextImgPath = getNextShowImgPath();
        if (!TextUtils.isEmpty(nextImgPath)) {
            setWallPapper(nextImgPath);
        }
    }

    protected void setWallPapper(final String path) {
        Single<Bitmap> obserable = Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> emitter) throws Exception {
                long timeStart = System.currentTimeMillis();
                Bitmap image = BitmapFactory.decodeFile(path);
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
                        WallpaperManager wpm = (WallpaperManager) YEApp.getInstance().getApplicationContext().getSystemService(
                                Context.WALLPAPER_SERVICE);
                        int result = RESULT_FAILED;
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                //不加最后面的flag参数，会导致锁屏图片重置为初始值/华为的杂志锁屏暂停并显示默认图片
                                result = wpm.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                            } else {
                                wpm.setBitmap(bitmap);
                                result = 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (result != RESULT_FAILED) {
                            setShowedFlag();
                        }
                    }
                });
    }

    private void setShowedFlag() {
        SpUtil.put(Constant.SpId.lastTimeShow, System.currentTimeMillis());
    }

}
