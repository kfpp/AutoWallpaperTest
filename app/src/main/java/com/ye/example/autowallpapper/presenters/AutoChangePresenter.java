package com.ye.example.autowallpapper.presenters;

import com.ye.example.autowallpapper.base.YEApp;
import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.module.settings.utils.SettingSPUtil;
import com.ye.example.autowallpapper.presenters.base.IBaseShowPresenter;
import com.ye.example.autowallpapper.utils.Logger;
import com.ye.example.autowallpapper.utils.ScreenUtil;
import com.ye.example.autowallpapper.utils.SpUtil;

/**
 * @author yezhihao 2019-08-05 15:00
 */
public class AutoChangePresenter {
    private static final int MINUTE = 60 * 1000;

    private IBaseShowPresenter mshowPresenter;

    public AutoChangePresenter(IBaseShowPresenter randomPresenter) {
        mshowPresenter = randomPresenter;
    }

    public void checkAndChange() {
        Logger.d("yyyy", "checkAndChange");
        if (canShowNext()) {
            showNext();
        } else {
            Logger.d("yyyy", "can not show next");
        }
    }

    public void changeImmediately() {
        showNext();
    }

    private boolean canShowNext() {
        long lastShowTime = SpUtil.get(Constant.SpId.lastTimeShow, 0L);
        boolean isLandScape = ScreenUtil.isLandscape(YEApp.getInstance().getApplicationContext());
        return !isLandScape && (lastShowTime == 0 || isLongThanMinDuration(lastShowTime));
    }

    private boolean isLongThanMinDuration(long lastShow) {
        long nowTime = System.currentTimeMillis();
        return (nowTime - lastShow) >= SettingSPUtil.getShowDuration() * MINUTE;
    }

    private void showNext() {
        Logger.d("yyyy", "showNext");
        mshowPresenter.showNextWallPaper();
    }
}
