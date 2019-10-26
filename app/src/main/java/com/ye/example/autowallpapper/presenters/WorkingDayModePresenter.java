package com.ye.example.autowallpapper.presenters;

import android.os.Environment;

import com.ye.example.autowallpapper.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkingDayModePresenter extends RandomNextPaperPresenter {
    private static final String DEFAULT_WALLPAPER_DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Huawei/MagazineUnlock/";

    private static final int[] WORK_WEEK_DAYS = new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};

    private static final int[] WORK_DAY_TIME_RANGE = new int[]{9, 21};

    private List<String> mDataList;
    public WorkingDayModePresenter() {
        File directory = new File(DEFAULT_WALLPAPER_DIRECTORY_PATH);
        mDataList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            mDataList.addAll(FileUtil.imagePathList(DEFAULT_WALLPAPER_DIRECTORY_PATH));
        }
    }

    @Override
    protected String getNextShowImgPath() {
        return super.getNextShowImgPath();
    }

    @Override
    protected List<String> getDataList() {
        if (isInWorkTime()) {
            return getWorkTimeDataList();
        } else {
            return getNormalTimeDataList();
        }
    }

    private boolean isInWorkTime() {
        Calendar calendar = Calendar.getInstance();
        int weekDayNow = calendar.get(Calendar.DAY_OF_WEEK);
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        return isInWorkDateRange(weekDayNow) && isInWorkTimeRange(hourNow);
    }

    private List<String> getWorkTimeDataList() {
        return super.getDataList();
    }

    private List<String> getNormalTimeDataList() {
        return mDataList;
    }

    private boolean isInWorkDateRange(int weekDayNow) {
        for (int workDay : WORK_WEEK_DAYS) {
            if (workDay == weekDayNow) {
                return true;
            }
        }
        return false;
    }

    private boolean isInWorkTimeRange(int hourNow) {
        return hourNow >= WORK_DAY_TIME_RANGE[0] && hourNow < WORK_DAY_TIME_RANGE[1];
    }
}
