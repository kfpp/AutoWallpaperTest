package com.ye.example.autowallpapper.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.text.TextUtils;

import com.ye.example.autowallpapper.data.dao.FileDao;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.data.entities.ImageFile;

import java.io.File;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yezhihao
 * @date 2019-07-02 16:22
 */
@Database(entities = {ImageFile.class, ImageDirectory.class}, version = 1, exportSchema = false)
public abstract class FileDataBase extends RoomDatabase {

    public static final String DB_NAME = "CompanyDatabase.db";
    private static volatile FileDataBase instance;

    public static synchronized FileDataBase getInstance() {
        return instance;
    }

    public static void initDataBase(Context context) {
        if (instance == null) {
            instance = create(context);
        }
    }

    private static FileDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                FileDataBase.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract FileDao getFileDao();

    public Flowable<List<ImageDirectory>> loadAllDirectory() {
        return getFileDao().loadAllDirectories();
    }

    public Flowable<List<ImageFile>> loadFiles(String directoryPath) {
        return getFileDao().loadFiles(directoryPath);
    }

    public ImageFile loadFile(String filePath) {
        return getFileDao().loadFile(filePath);
    }

    public void insertDirectory(String dirPath, String parentPath) {
        ImageDirectory directory = new ImageDirectory();
        directory.setPath(dirPath);
        directory.setParentPath(parentPath);
        getFileDao().insertDirectory(directory);
    }

    public void insertFile(String filePath) {
        FileDao fileDao = getFileDao();
        if (fileDao.loadFile(filePath) != null) {
            return;
        }
        File file = new File(filePath);
        String parentPath = file.getParentFile().getAbsolutePath();
        ImageFile fileEntiry = new ImageFile();
        fileEntiry.setFilePath(filePath);
        fileEntiry.setDirectoryPath(parentPath);
        fileDao.insertFile(fileEntiry);
    }

    public void insertFiles(List<String> filePaths) {
        String parentPath = "";
        ImageFile[] arr = new ImageFile[filePaths.size()];
        for (int i = 0; i < filePaths.size(); i++) {
            String path = filePaths.get(i);
            File file = new File(path);
            parentPath = file.getParentFile().getAbsolutePath();
            ImageFile fileEntiry = new ImageFile();
            fileEntiry.setFilePath(path);
            fileEntiry.setDirectoryPath(parentPath);
            arr[i] = fileEntiry;
        }
        getFileDao().insertFile(arr);
    }

    public Flowable<Integer> deleteDirectory(final String dirpPath) {
        return Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                getFileDao().loadFiles(dirpPath)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<ImageFile>>() {
                            @Override
                            public void accept(List<ImageFile> fileEntiries) throws Exception {
                                deleteFiles(fileEntiries);
                                ImageDirectory directory = new ImageDirectory();
                                directory.setPath(dirpPath);
                                int count = getFileDao().deleteDirectory(directory);
                                emitter.onNext(count);
                                emitter.onComplete();
                            }
                        });
            }
        }, BackpressureStrategy.BUFFER);


    }

    private void deleteFiles(List<ImageFile> list) {
        ImageFile[] arr = list.toArray(new ImageFile[list.size()]);
        getFileDao().deleteFiles(arr);
    }

    public void insertDirectoryAndFiles(final String parentDirectoryPath, List<String> filePaths) {
        if (filePaths == null || filePaths.size() == 0) {
            return;
        }

        insertFiles(filePaths);
        if (!TextUtils.isEmpty(parentDirectoryPath)) {
            loadAllDirectory().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<ImageDirectory>>() {
                        @Override
                        public void accept(List<ImageDirectory> imageDirectories) throws Exception {
                            boolean exist = false;
                            for (ImageDirectory directory : imageDirectories) {
                                if (directory.getPath().equalsIgnoreCase(parentDirectoryPath)) {
                                    exist = true;
                                }
                            }

                            if (!exist) {
                                insertDirectory(parentDirectoryPath, "");
                            }
                        }
                    });
        }
    }

    public Flowable<Integer> loadDirectoryImageCount(String path) {
        return getFileDao().getDirectoryFileCount(path);
    }

    //
}
