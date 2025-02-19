package com.example.mealz.presenter.profile;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.view.profile.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {
    MealsRepository repo;
    ProfileView view;

    public ProfilePresenterImpl(MealsRepository repo, ProfileView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void clearUserId() {
        repo.clearUserId();
    }

    @Override
    public void clearUsername() {
        repo.clearUsername();
    }

    @Override
    public void setRememberMe(boolean value) {
        repo.setRememberMe(value);
    }

    @Override
    public void getUsername() {
        repo.getUsername()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(String username) {
                        view.displayUserName(username);
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
