package com.ye.example.autowallpapper.presenters.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.activities.MainActivity;
import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageFile;
import com.ye.example.autowallpapper.utils.VibratorUtil;
import com.ye.example.autowallpapper.views.FileRecyclerView;

import java.util.List;
import java.util.function.Function;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        List<FileRecyclerView.DataAdapter> list = mTarget.getRecyclerView().getSelectedItem();
        StringBuilder sb = new StringBuilder();
        for (FileRecyclerView.DataAdapter item : list) {
            sb.append("\n");
            sb.append(item.getName());
            sb.append("：");
            sb.append(item.getPath());
            sb.append("\n");

            FileDataBase.getInstance().deleteDirectory(item.getPath())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(integer -> {
                        Log.i("yyyy", "delete item : " + integer);
                    });
        }
        Log.i("yyyy", "selected item : " + sb.toString());
        RecyclerView.Adapter adapter = mTarget.getRecyclerView().getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Log.e("yyyy", "adapter is  null");
        }
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
