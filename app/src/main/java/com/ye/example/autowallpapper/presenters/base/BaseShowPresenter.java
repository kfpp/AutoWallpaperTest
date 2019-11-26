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

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
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
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        WallpaperManager wpm = (WallpaperManager) YEApp.getInstance().getApplicationContext().getSystemService(
                                Context.WALLPAPER_SERVICE);
//                        Drawable sourceDrawable = wpm.getDrawable();

                        int result = RESULT_FAILED;
                            if (Build.VERSION.SDK_INT >= 24) {
                                //不加最后面的flag参数，会导致锁屏图片重置为初始值/华为的杂志锁屏暂停并显示默认图片
                                result = wpm.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//                                InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//                                result =wpm.setStream(isBm, null, false, WallpaperManager.FLAG_SYSTEM);
                            } else {
                                wpm.setBitmap(bitmap);
                                result = 1;
                            }

                        if (result != RESULT_FAILED) {
//                            ((BitmapDrawable) sourceDrawable).getBitmap().recycle();
                            setShowedFlag();
                        }
                    }
                });
    }

    private void setShowedFlag() {
        SpUtil.put(Constant.SpId.lastTimeShow, System.currentTimeMillis());
    }

}
