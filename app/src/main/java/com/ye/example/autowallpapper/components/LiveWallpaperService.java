package com.ye.example.autowallpapper.components;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ye.example.autowallpapper.components.live.LiveWallpaperManager;
import com.ye.example.autowallpapper.utils.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LiveWallpaperService extends WallpaperService {
    public static final String TAG = LiveWallpaperService.class.getSimpleName();

    public static final int ACTION_INIT = 0;
    public static final int ACTION_UPDATE = 1;

    public static final String ACTION_WALLPAPER_CHANGED = "action_wallpaper_changed";
    public static final String KEY_NEW_WALLPAPER = "key_new_wallpaper";

    public static void wallpaperChanged(Context context, Uri uri) {
        Logger.i(TAG, "wallpaperChanged: " + uri);
        Intent i = new Intent(ACTION_WALLPAPER_CHANGED);
        i.putExtra(KEY_NEW_WALLPAPER, uri);
        context.sendBroadcast(i);
    }

    public static void setToWallPaper(Context context, Uri uri) {
        Logger.i(TAG, "setToWallPaper: " + (uri == null ? "null " : uri));
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, LiveWallpaperService.class)
        );
//        SimpleLiveWallpaperModel model = new SimpleLiveWallpaperModel(context);
//        if (uri != null) {
//            model.setWallpaperUri(uri);
//        } else{
//            model.setWallpaperUri(Uri.EMPTY);
//        }
        intent.putExtra(KEY_NEW_WALLPAPER, uri == null ? Uri.EMPTY : uri);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            appCompatActivity.startActivityForResult(intent, 1);
        } else {
            context.startActivity(intent);
        }
    }

    private Uri mWallpaperUrl;
    private Bitmap mWallpaperBitmap;
    private BroadcastReceiver mBroadcastReceiver;
    private ExecutorService mSingleExecutor;
    private Handler mMainHandler;
    private boolean mIsBroadcastRegistered;

    private boolean mIsInit;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.w(TAG, "LiveWallpaperService: onCreate ");
        mIsInit = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWallpaperUrl = Uri.EMPTY;
        if (intent.hasExtra(KEY_NEW_WALLPAPER)) {
            mWallpaperUrl = intent.getParcelableExtra(KEY_NEW_WALLPAPER);
        }
        Logger.i(TAG, "onStartCommand : wallpaper uri  " + mWallpaperUrl);
        return START_STICKY;
    }

    @Override
    public Engine onCreateEngine() {
        return new LiveWallpaperEngine();
    }

    private class LiveWallpaperEngine extends Engine {

        private Rect mSrcRect, mDstRect;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            Logger.w(TAG, "LiveWallpaperEngine: onCreate ");
            mSingleExecutor = Executors.newSingleThreadExecutor();

            mSrcRect = new Rect();
//            mDstRect = new Rect(0, 0, DrawUtils.sRealWidthPixels, DrawUtils.sRealHeightPixels);
            mDstRect = new Rect(0, 0, 1080, 1920);

            IntentFilter intentFilter = new IntentFilter(ACTION_WALLPAPER_CHANGED);
            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Logger.i(TAG, "wallpaperChanged: onReceive ");
                    mWallpaperUrl = intent.getParcelableExtra(KEY_NEW_WALLPAPER);
                    loadWallpaperAsyc(true);
                }
            };
            if (!mIsBroadcastRegistered) {
                registerReceiver(mBroadcastReceiver, intentFilter);
                mIsBroadcastRegistered = true;
            }

            initHandler();

            loadWallpaperAsyc(false);
        }

        @SuppressLint("HandlerLeak")
        private void initHandler() {
            mMainHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    drawBitmap();
                    if (msg.what == ACTION_UPDATE) {
//                        Toast.makeText(AppEnv.INSTANCE.getApplicationContext(), R.string.set_successful, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("action_simple_live_wallpaper_done");
                        intent.putExtra("extra_result", (boolean) msg.obj);
                        sendBroadcast(intent);
                    }
                }
            };
        }

        private void loadWallpaperAsyc(final boolean update) {
            if (!update && isGuideStart()) {
                mWallpaperBitmap = LiveWallpaperManager.getDevicesLocalWallpaper(getApplicationContext());
                Logger.i(TAG, "loadWallpaperAsyc:init success ");
                if (mWallpaperBitmap != null) {
                    onSuccess(false);
                } else {
                    onFail(false);
                }
            } else {
                mSingleExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mWallpaperBitmap = load(mWallpaperUrl).submit(1080, 1920).get();

                            Logger.i(TAG, "wallpaperChanged: glide load uri bitmap success ");
                            onSuccess(update);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            onFail(update);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            onFail(update);
                        }
                    }
                });
            }
        }

        private RequestBuilder<Bitmap> load(Uri uri) {
            if (uri.toString().startsWith("/storage/")) {
                return Glide.with(getApplicationContext()).asBitmap()
                        .load(uri.toString());
            } else {
                return Glide.with(getApplicationContext()).asBitmap()
                        .load(uri);
            }
        }

        private void onSuccess(boolean update) {
            Message message = mMainHandler.obtainMessage(update ? ACTION_UPDATE : ACTION_INIT);
            message.obj = true;
            message.sendToTarget();
        }

        private void onFail(boolean update) {
            Message message = mMainHandler.obtainMessage(update ? ACTION_UPDATE : ACTION_INIT);
            message.obj = false;
            message.sendToTarget();
        }

        private Bitmap getWallpaperBitmap() {
            if (mWallpaperBitmap == null) {
                return LiveWallpaperManager.getDevicesLocalWallpaper(getApplicationContext());
            } else {
                return mWallpaperBitmap;
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            drawBitmap();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mIsBroadcastRegistered) {
                try {
                    unregisterReceiver(mBroadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mIsBroadcastRegistered = false;
            }
            Logger.w(TAG, "LiveWallpaperEngine: onDestroy ");
        }

        private void drawBitmap() {
            Canvas canvas = getSurfaceHolder().lockCanvas();

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Bitmap bitmap = getWallpaperBitmap();
            mSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            checkSourceRect();
            canvas.drawBitmap(bitmap, mSrcRect, mDstRect, null);
            getSurfaceHolder().unlockCanvasAndPost(canvas);
        }

        /**
         * 根据图片比例，和屏幕比例，处理 等比缩放 的问题，主要是判断并剪裁掉 不合比例的部分
         * 具体策略：centerCrop
         */
        private void checkSourceRect() {
            //图片比例
            float srcScale = (float) mSrcRect.height() / (float) mSrcRect.width();
            //屏幕比例
            float dstScale = (float) mDstRect.height() / (float) mDstRect.width();

            Logger.w("yzh_", "屏幕尺寸 ： " + mDstRect.width() + ":" + mDstRect.height());
            if (srcScale == dstScale || Math.abs(srcScale - dstScale) <= 0.1f) {
                //图片比例 和屏幕比例几乎相同，直接等比缩放显示就可以，不需要修正
                Logger.i("yzh_", "图片比例 和屏幕比例几乎相同，直接等比缩放显示就可以，不需要修正");
                return;
            } else if (srcScale > dstScale) {
                //图片比例 高度偏高， 需要裁掉部分高度

                //按宽度的缩放比例 同比计算所需高度
                int needWidthHeight = (int) ((float) mSrcRect.width() * dstScale);
                Logger.i("yzh_", "图片比例 高度偏高， 需要裁掉部分高度,图片原宽度： " + mSrcRect.width() + ", 屏幕宽度： " + mDstRect.width() + " 图片原高度： " + mSrcRect.height() + ", 所需高度： " + needWidthHeight);
                if (needWidthHeight < mSrcRect.height()) {
                    int heightMore = mSrcRect.height() - needWidthHeight;
                    int offset = heightMore / 2;
                    mSrcRect.set(0, offset, mSrcRect.width(), mSrcRect.height() - offset);
                }

            } else if (srcScale < dstScale) {
                //图片比例中 宽度偏大， 需要裁掉部分宽度
                int needWidth = (int) ((float) mSrcRect.height() / dstScale);
                Logger.i("yzh_", "图片比例中 宽度偏大， 需要裁掉部分宽度, 图片原宽度：" + mSrcRect.width() + ", 所需宽度： " + needWidth + ", 图片原高度： " + mSrcRect.height() + ", 屏幕原高度 ： " + mDstRect.height());
                if (needWidth < mSrcRect.width()) {
                    int widthMore = mSrcRect.width() - needWidth;
                    int offset = widthMore / 2;
                    mSrcRect.set(offset, 0, mSrcRect.width() - offset, mSrcRect.height());
                }
            } else {
                Logger.i("yzh_", "状态 未知");
            }
        }
    }

    private boolean isGuideStart() {

        return mWallpaperUrl == null || mWallpaperUrl.equals(Uri.EMPTY);
    }

}
