package com.ye.example.autowallpapper.models.settings.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ye.example.autowallpapper.R;
import com.ye.example.autowallpapper.models.settings.utils.SettingSPUtil;

public class DurationDialog extends DialogFragment {

    public static DurationDialog newInstance() {
        return new DurationDialog();
    }

    private String[] mItemNames;
    private int[] mItemValues;

    private int mSelectedValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemNames = getContext().getResources().getStringArray(R.array.duration_array_name);
        mItemValues = getContext().getResources().getIntArray(R.array.duration_array_value);

        mSelectedValue = getSPDuration();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.duration_selection_dialog_title);
        builder.setSingleChoiceItems(mItemNames, searchValueIndex(mSelectedValue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedValue = mItemValues[which];
            }
        });
        builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveDuration(mSelectedValue);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private void saveDuration(int duration) {
        SettingSPUtil.setShowDuration(duration);
    }

    private int getSPDuration() {
        return SettingSPUtil.getShowDuration();
    }

    private int searchValueIndex(int selectedValue) {
        if (selectedValue == -1) {
            return -1;
        }
        for (int i = 0; i < mItemValues.length; i++) {
            if (mItemValues[i] == selectedValue) {
                return i;
            }
        }
        return -1;
    }
}
