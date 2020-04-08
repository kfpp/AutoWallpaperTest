package com.ye.example.autowallpapper.presenters.base;

import android.text.TextUtils;

import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.proxy.showproxy.LiveWallpaperShowPorxy;
import com.ye.example.autowallpapper.proxy.showproxy.WallpaperShowPorxy;
import com.ye.example.autowallpapper.proxy.showproxy.base.IWallpaperShowProxy;
import com.ye.example.autowallpapper.utils.SpUtil;

import java.util.List;

public abstract class BaseShowPresenter implements IBaseShowPresenter {

    public static final int RESULT_FAILED = 0;

    private IWallpaperShowProxy mWallpaperShowProxy;

    public BaseShowPresenter() {
//        mWallpaperShowProxy = new WallpaperShowPorxy();
        mWallpaperShowProxy = new LiveWallpaperShowPorxy();
    }

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
        mWallpaperShowProxy.showImage(path);
    }

}
