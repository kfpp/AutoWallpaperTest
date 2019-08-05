package com.ye.example.autowallpapper.presenters;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.utils.GlobalData;
import com.ye.example.autowallpapper.utils.SpUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yezhihao 2019-08-05 15:04
 */
public class RandomNextPaperPresenter {

    public void dealShowNext(final Context context) {
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
                        int result = 0;
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
                        if (result != 0) {
                            setShowedFlag();
                        }
                    }
                });
    }

    private String randomPath(List<String> images) {
        Random random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(images.size());
        return images.get(randomIndex);
    }

    private void setShowedFlag() {
        SpUtil.put(Constant.SpId.lastTimeShow, System.currentTimeMillis());
    }

}
