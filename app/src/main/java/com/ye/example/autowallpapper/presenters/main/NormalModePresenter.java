package com.ye.example.autowallpapper.presenters.main;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.base.MainActivity;
import com.ye.example.autowallpapper.module.settings.SettingActivity;

public class NormalModePresenter implements IModePresenter {

    private MainActivity mTarget;

    public NormalModePresenter(MainActivity target) {
        mTarget = target;
    }

    @Override
    public void onMenuClicked() {
        SettingActivity.startActivity(mTarget);
    }

    @Override
    public void onFloatBtnClicked() {
        mTarget.getFileBrowserUtils().showFileBorwser();
    }

    @Override
    public void enterMode() {
        mTarget.getFab().setImageResource(R.drawable.ic_add_to_photos);
        mTarget.getSettingMenuItem().setIcon(R.drawable.ic_settings);
    }
}
