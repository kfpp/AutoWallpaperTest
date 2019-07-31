package com.ye.example.autowallpapper.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ye.example.autowallpapper.data.database.FileDataBase;
import com.ye.example.autowallpapper.data.entities.ImageDirectory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
                .subscribe(new Consumer<List<ImageDirectory>>() {
                    @Override
                    public void accept(List<ImageDirectory> directories) throws Exception {
                        mDirectoryLiveData.setValue((directories != null && directories.size() > 0) ? directories : null);
                    }
                });
    }

    public void loadDirectoryFileCount(final String path) {
        FileDataBase.getInstance().loadDirectoryImageCount(path)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        getImageCountLiveData(path).setValue(integer);
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
