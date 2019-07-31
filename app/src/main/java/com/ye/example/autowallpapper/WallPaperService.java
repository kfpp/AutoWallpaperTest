package com.ye.example.autowallpapper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.ye.example.autowallpapper.base.YEApp;
import com.ye.example.autowallpapper.utils.FileUtil;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WallPaperService extends Service {
    private static final String CHANNEL_ID = "NFCService";
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < 26) {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("主服务")//设置标题
                    .setContentText("运行中...")//设置内容
                    .setWhen(System.currentTimeMillis())//设置创建时间
                    .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//设置通知栏图标
                    .setContentIntent(generateClickIntent())
                    .build();
            startForeground(1, notification);
        } else {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel Channel = new NotificationChannel(CHANNEL_ID, "主服务", NotificationManager.IMPORTANCE_HIGH);
            Channel.enableLights(true);//设置提示灯
            Channel.setLightColor(Color.RED);//设置提示灯颜色
            Channel.setShowBadge(true);//显示logo
            Channel.setDescription("ytzn");//设置描述
            Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            manager.createNotificationChannel(Channel);

            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("主服务")//标题
                    .setContentText("运行中...")//内容
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(generateClickIntent())
                    .build();
            startForeground(1, notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了
        }

        Single<List<String>> observable = Single.create(new SingleOnSubscribe<List<String>>() {
            @Override
            public void subscribe(SingleEmitter<List<String>> emitter) {
                List<String> pathList = FileUtil.imagePathList(FileUtil.DEFAULT_IMAGE_DIRECTORY_PATH);
                emitter.onSuccess(pathList);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        YEApp.getInstance().setImageList(strings);
                    }
                });

        NextWallPaperBroadcast receiver = new NextWallPaperBroadcast();
        IntentFilter filter = new IntentFilter(NextWallPaperBroadcast.ACTION_NAME);
        registerReceiver(receiver, filter);

        if (Build.VERSION.SDK_INT >= 27) {
            WallpaperManager wpm = (WallpaperManager) getSystemService(
                    Context.WALLPAPER_SERVICE);

        }
    }

    private PendingIntent generateClickIntent() {
        Intent intent = new Intent(NextWallPaperBroadcast.ACTION_NAME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
