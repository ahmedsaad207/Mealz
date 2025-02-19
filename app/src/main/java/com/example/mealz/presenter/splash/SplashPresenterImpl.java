package com.example.mealz.presenter.splash;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;

public class SplashPresenterImpl implements SplashPresenter {
    MealsRepository repo;
    SplashView view;

    public SplashPresenterImpl(MealsRepositoryImpl repo, SplashView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getUserId() {
        repo.getUserId()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(String username) {
                        view.onUserId(username);
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
