package com.example.mealz.presenter.splash;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashPresenter {
    MealsRepository repo;
    SplashView view;

    public SplashPresenterImpl(MealsRepositoryImpl repo, SplashView view) {
        this.repo = repo;
        this.view = view;
    }



    @Override
    public void getRememberMe() {
        repo.getRememberMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        view.onUserId(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
