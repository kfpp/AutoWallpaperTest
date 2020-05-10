package com.ye.example.autowallpapper.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ye.example.autowallpapper.base.YEApp;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author yezhihao
 * @date 2019-07-09 17:29
 */
public class AppUtil {
    public static final String TAG = AppUtil.class.getSimpleName();
    private static volatile AppUtil sInstance;
    public static AppUtil getInstance() {
        synchronized (AppUtil.class) {
            if (sInstance == null) {
                synchronized (AppUtil.class) {
                    sInstance = new AppUtil();
                }
            }
        }

        return sInstance;
    }

    public static String getTopActivityPackageName(@NonNull Context context) {
        final UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        if(usageStatsManager == null) {
            return null;
        }

        String topActivityPackageName = null;
        long time = System.currentTimeMillis();
        // 查询最后十秒钟使用应用统计数据
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
        // 以最后使用时间为标准进行排序
        if(usageStatsList != null) {
            SortedMap<Long,UsageStats> sortedMap = new TreeMap<Long,UsageStats>();
            for (UsageStats usageStats : usageStatsList) {
                sortedMap.put(usageStats.getLastTimeUsed(),usageStats);
            }
            if(sortedMap.size() != 0) {
                topActivityPackageName =  sortedMap.get(sortedMap.lastKey()).getPackageName();
                Logger.d(TAG,"Top activity package name = " + topActivityPackageName);
            }
        }

        return topActivityPackageName;
    }

}
