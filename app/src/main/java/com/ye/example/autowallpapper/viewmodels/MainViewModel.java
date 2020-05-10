package com.ye.example.autowallpapper.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;
import com.ye.example.autowallpapper.data.entities.ImageFile;
import com.ye.example.autowallpapper.utils.Logger;
import com.ye.example.autowallpapper.utils.rxutils.AutoDisposedObserver;

import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yezhihao
 * @date 2019-07-31 14:41
 */
public class MainViewModel extends ViewModel {

    private MutableLiveData<List<ImageDirectory>> mDirectoryLiveData = new MutableLiveData<>();
    private Map<String, MutableLiveData<Integer>> mImageCountMap = new HashMap<>();

    public void loadDirectories() {
        FileDataBase.getInstance().loadAllDirectory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(directories -> mDirectoryLiveData.setValue((directories != null && directories.size() > 0) ? directories : null));
    }

    public void loadDirectoryFileCount(final String path) {
        FileDataBase.getInstance().loadDirectoryImageCount(path)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(integer -> getImageCountLiveData(path).setValue(integer));
    }

    public void showImages() {
        FileDataBase.getInstance().loadAllDirectory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ImageDirectory>>() {
            @Override
            public void accept(List<ImageDirectory> imageDirectories) throws Exception {
                StringBuilder sb = new StringBuilder();
                for (ImageDirectory file : imageDirectories) {
                    sb.append("\n");
                    sb.append(file.getParentPath());
                    sb.append('-');
                    sb.append(file.getPath());
                }
                Logger.i("yyyy", "directories in db : " + sb.toString());
            }
        });
        FileDataBase.getInstance().loadAllFiles().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(imageFiles -> {
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (ImageFile file : imageFiles) {
                sb.append("\n");
                sb.append(file.getDirectoryPath());
                sb.append('-');
                sb.append(file.getFilePath());
                if (i > 10) {
                    break;
                } else {
                    i++;
                }
            }
            Logger.i("yyyy", "file in db : " + sb.toString());
        });
    }

    public void preloadTestData() {
        insertDemoData();
    }

    private void insertDemoData() {
        String[] preloadDirectories = new String[]{
                "/storage/6A2A-A729/Pictures/AISS",
//                "/storage/6A2A-A729/Pictures/AISS/纳丝摄影",
//                "/storage/6A2A-A729/Pictures/AISS/周于希",
//                "/storage/6A2A-A729/Pictures/AISS/森萝财团",
//                "/storage/6A2A-A729/Pictures/AISS/轻兰映画",
        };
        Observable.fromArray(preloadDirectories)
                .map(this::searchDirectories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AutoDisposedObserver<List<String>>() {
                    private List<String> mSubDirectories = new ArrayList<>();
                    @Override
                    public void onNext(List<String> strings) {
                        mSubDirectories.addAll(strings);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Logger.i("yyyy", "" +
                                "" + mSubDirectories.size() + "个子目录");
                        preloadImages(mSubDirectories);

                    }
                });
    }

    private List<String> searchDirectories(String dir) {
        List<String> pathList = new ArrayList<>();
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory() || !dirFile.canRead()) {
            return pathList;
        }
        File[] subDirectories = dirFile.listFiles(pathname -> pathname.isDirectory() && pathname.canRead());
        Logger.i("yyyy", "从目录 ： " + dir + "搜索到" + (subDirectories == null ? 0 : subDirectories.length) + "个子目录");
        if (subDirectories != null) {
            for (File file : subDirectories) {
                pathList.add(file.getAbsolutePath());
                pathList.addAll(searchDirectories(file.getAbsolutePath()));
            }
        }

        return pathList;
    }

    private void preloadImages(List<String> dirList) {
        Observable.fromIterable(dirList)
                .map(File::new)
                .flatMap((Function<File, ObservableSource<File[]>>) file -> {
                    File[] imageFiles = file.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                    return Observable.just(imageFiles);
                })
                .map(Arrays::asList)
                .map(files -> {
                    List<String> pathList = new ArrayList<>();
                    for (File file : files) {
                        pathList.add(file.getAbsolutePath());
                    }
                    return pathList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AutoDisposedObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        if (strings != null && strings.size() > 0) {
                            String parent = new File(strings.get(0)).getParentFile().getAbsolutePath();
//                            Logger.i("yyyy", "插入目录 ： " + parent + ", " + strings.size() + " 张图片");
                            Logger.i("yyyy", "插入图片 ： " + parent + ", " + strings.size() + " 张图片");
                            FileDataBase.getInstance().insertDirectoryAndFiles(parent, strings);
                        }
                    }
                });

    }

    public MutableLiveData<List<ImageDirectory>> getDirectoryLiveData() {
        return mDirectoryLiveData;
    }

    public MutableLiveData<Integer> getImageCountLiveData(String dirPath) {
        if (!mImageCountMap.containsKey(dirPath)) {
            mImageCountMap.put(dirPath, new MutableLiveData<Integer>());
        }

        return mImageCountMap.get(dirPath);
    }


    public static final class DirectoryData {
        private ImageDirectory mImageDirectory;
        private int mImageCount;
    }

}
