package com.ye.example.autowallpapper.presenters.main;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.activities.MainActivity;
import com.ye.example.autowallpapper.utils.VibratorUtil;

public class EditModePresenter implements IModePresenter {

    private MainActivity mTarget;

    public EditModePresenter(MainActivity target) {
        mTarget = target;
    }

    @Override
    public void onMenuClicked() {
        mTarget.getRecyclerView().exitEditMode();
    }

    @Override
    public void onFloatBtnClicked() {
        // TODO: 2019/10/26 delete
    }

    @Override
    public void enterMode() {
        mTarget.getFab().setImageResource(R.drawable.ic_delete);
        mTarget.getSettingMenuItem().setIcon(R.drawable.ic_done);
        VibratorUtil.VibratorOnce(mTarget);
    }

    @Override
    public boolean onBackPress() {
        mTarget.getRecyclerView().exitEditMode();
        return true;
    }
}
