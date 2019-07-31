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
public class ImageFile {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "file_path")
    private String mFilePath;

    @ColumnInfo(name = "directory_path")
    private String mDirectoryPath;

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getDirectoryPath() {
        return mDirectoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        mDirectoryPath = directoryPath;
    }

}
