package com.ye.example.autowallpapper.presenters;

import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.models.settings.utils.SettingSPUtil;
import com.ye.example.autowallpapper.presenters.base.IBaseShowPresenter;
import com.ye.example.autowallpapper.utils.Logger;
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
        if (check()) {
            showNext();
        }
    }

    public void changeImmediately() {
        showNext();
    }

    private boolean check() {
        long lastShowTime = SpUtil.get(Constant.SpId.lastTimeShow, 0L);
        return lastShowTime == 0 || isLongThanMinDuration(lastShowTime);
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
