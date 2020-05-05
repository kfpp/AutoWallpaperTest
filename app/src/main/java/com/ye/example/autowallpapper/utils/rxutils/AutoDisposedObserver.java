package com.ye.example.autowallpapper.utils.rxutils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AutoDisposedObserver<T> implements Observer<T> {
    private Disposable mDisposable;
    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
