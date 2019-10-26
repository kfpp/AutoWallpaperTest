package com.ye.example.autowallpapper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

public class WallPaperService extends Service {
    private static final String CHANNEL_ID = "AutoWallPaperService";
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < 26) {
           initNotificationBelowApi26();
        } else {
            initNotificationAboveApi26();
        }

        NextWallPaperBroadcast receiver = new NextWallPaperBroadcast();
        IntentFilter filter = new IntentFilter(NextWallPaperBroadcast.ACTION_CLICKED);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);
    }

    private void initNotificationBelowApi26() {
        Notification notification = new Notification.Builder(this)
                .setContentTitle("主服务")//设置标题
                .setContentText("运行中...")//设置内容
                .setWhen(System.currentTimeMillis())//设置创建时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//设置通知栏图标
                .setContentIntent(generateClickIntent())
                .build();
        startForeground(1, notification);
    }

    @TargetApi(26)
    private void initNotificationAboveApi26() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel Channel = new NotificationChannel(CHANNEL_ID, "主服务", NotificationManager.IMPORTANCE_HIGH);
        Channel.enableLights(true);//设置提示灯
        Channel.setLightColor(Color.RED);//设置提示灯颜色
        Channel.setShowBadge(true);//显示logo
        Channel.setDescription("ytzn");//设置描述
        Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
        manager.createNotificationChannel(Channel);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("壁纸自动切换服务")//标题
                .setContentText("正在运行中...")//内容
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(generateClickIntent())
                .build();
        startForeground(1, notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了
    }

    private PendingIntent generateClickIntent() {
        Intent intent = new Intent(NextWallPaperBroadcast.ACTION_CLICKED);
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
