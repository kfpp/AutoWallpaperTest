package com.ye.example.autowallpapper.presenters;

import android.content.Context;

import com.ye.example.autowallpapper.common.Constant;
import com.ye.example.autowallpapper.utils.Logger;
import com.ye.example.autowallpapper.utils.SpUtil;

/**
 * @author yezhihao 2019-08-05 15:00
 */
public class AutoChangePresenter {
    public static final int MINUTE = 60 * 1000;

    private RandomNextPaperPresenter mRandomPresenter;

    public AutoChangePresenter(RandomNextPaperPresenter randomPresenter) {
        mRandomPresenter = randomPresenter;
    }

    public void checkAndChange(Context context) {
        Logger.d("yyyy", "checkAndChange");
        if (check()) {
            showNext(context);
        }
    }

    private boolean check() {
        long lastShowTime = SpUtil.get(Constant.SpId.lastTimeShow, 0L);
        return lastShowTime == 0 || isMoreThanFiveMinute(lastShowTime);
    }

    private boolean isMoreThanFiveMinute(long lastShow) {
        long nowTime = System.currentTimeMillis();
        return (nowTime - lastShow) >= 5 * MINUTE;
    }

    private void showNext(Context context) {
        Logger.d("yyyy", "showNext");
        mRandomPresenter.dealShowNext(context);
    }
}
