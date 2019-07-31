package com.ye.example.autowallpapper.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author yezhihao
 * @date 2019-07-31 11:21
 */
@Entity
public class ImageDirectory {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "dir_path")
    private String mPath;
    @ColumnInfo(name = "dir_parent_path")
    private String parentPath;

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
