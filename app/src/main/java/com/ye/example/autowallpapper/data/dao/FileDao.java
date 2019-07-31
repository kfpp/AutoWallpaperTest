package com.ye.example.autowallpapper.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.data.entities.ImageFile;

import java.util.List;

import io.reactivex.Flowable;


/**
 * @author yezhihao
 * @date 2019-07-02 16:15
 */
@Dao
public interface FileDao {

    @Query("SELECT * FROM ImageDirectory")
    Flowable<List<ImageDirectory>> loadAllDirectories();

    @Query("SELECT * FROM ImageFile WHERE directory_path == :directoryPath")
    Flowable<List<ImageFile>> loadFiles(String directoryPath);

    @Query("SELECT * FROM ImageFile WHERE file_path == :filePath")
    ImageFile loadFile(String filePath);

    @Query("select count(*) from ImageFile where directory_path == :path")
    Flowable<Integer> getDirectoryFileCount(String path);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertDirectory(ImageDirectory... directory);

    @Delete
    int deleteDirectory(ImageDirectory... directory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertFile(ImageFile... files);

    @Delete
    int deleteFiles(ImageFile... files);
}
