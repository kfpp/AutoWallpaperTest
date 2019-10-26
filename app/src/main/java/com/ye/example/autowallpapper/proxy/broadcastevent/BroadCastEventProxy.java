package com.ye.example.autowallpapper.proxy.broadcastevent;

import com.ye.example.autowallpapper.presenters.AutoChangePresenter;
import com.ye.example.autowallpapper.presenters.RandomNextPaperPresenter;
import com.ye.example.autowallpapper.presenters.WorkingDayModePresenter;
import com.ye.example.autowallpapper.presenters.base.IBaseShowPresenter;

public class BroadCastEventProxy {
    private boolean mIsDealing;

    private AutoChangePresenter mAutoChangePresenter;
    public BroadCastEventProxy() {
        mIsDealing = false;
//        RandomNextPaperPresenter randomPresenter = new RandomNextPaperPresenter();
        IBaseShowPresenter presenter = new WorkingDayModePresenter();
        mAutoChangePresenter = new AutoChangePresenter(presenter);
    }

    public void onClickedEvent() {
        if (!mIsDealing) {
            mIsDealing = true;
            mAutoChangePresenter.changeImmediately();
            mIsDealing = false;
        }
    }

    public void onUserPresentEvent() {
        if (!mIsDealing) {
            mIsDealing = true;
            mAutoChangePresenter.checkAndChange();
            mIsDealing = false;
        }
    }
}
