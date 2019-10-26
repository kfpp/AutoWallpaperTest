package com.ye.example.autowallpapper.presenters;

import android.os.Environment;

import com.ye.example.autowallpapper.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkingDayModePresenter extends RandomNextPaperPresenter {
    private static final String DEFAULT_WALLPAPER_DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Huawei/MagazineUnlock/";

    private List<String> mDataList;
    public WorkingDayModePresenter() {
        File directory = new File(DEFAULT_WALLPAPER_DIRECTORY_PATH);
        mDataList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            mDataList.addAll(FileUtil.imagePathList(DEFAULT_WALLPAPER_DIRECTORY_PATH));
        }
    }

    @Override
    protected List<String> getDataList() {
        return mDataList;
    }
}
