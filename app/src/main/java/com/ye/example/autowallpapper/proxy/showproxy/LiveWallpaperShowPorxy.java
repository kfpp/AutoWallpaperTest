package com.ye.example.autowallpapper.proxy.showproxy;

import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.ye.example.autowallpapper.base.YEApp;
import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.components.live.SimpleWallpaperAPI;
import com.ye.example.autowallpapper.proxy.showproxy.base.IWallpaperShowProxy;
import com.ye.example.autowallpapper.utils.Logger;
import com.ye.example.autowallpapper.utils.SpUtil;

import java.time.Year;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.ye.example.autowallpapper.presenters.base.BaseShowPresenter.RESULT_FAILED;

public class LiveWallpaperShowPorxy implements IWallpaperShowProxy {
    @Override
    public void showImage(final String path) {
        Single.just(path).observeOn(Schedulers.io()).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String s) throws Exception {
                return getMediaUriFromPath(YEApp.getInstance().getApplicationContext(), path);
            }
        }).subscribe(new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) throws Exception {
                Logger.i("yzh", "uri : " + uri);
                SimpleWallpaperAPI.getInstance().setWallPaper(YEApp.getInstance().getApplicationContext(), uri);
                setShowedFlag();
            }
        });
    }

    private void setShowedFlag() {
        SpUtil.put(Constant.SpId.lastTimeShow, System.currentTimeMillis());
    }

    /***
     * 将指定路径的图片转uri
     * @param context
     * @param path ，指定图片(或文件)的路径
     * @return
     */
    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);
        Logger.i("yzh", "getMediaUriFromPath " + path);
        Uri uri = Uri.EMPTY;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                uri = ContentUris.withAppendedId(mediaUri,
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
            }
            cursor.close();
            Logger.i("yzh", "getMediaUriFromPath uri" + uri);
        }
        if (uri == Uri.EMPTY) {
            uri = Uri.parse(path);
            Logger.i("yzh", "getMediaUriFromPath empty uri" + uri);
        }
        return uri;
    }
}
