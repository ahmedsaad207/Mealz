package com.example.mealz.presenter.profile;

import com.example.mealz.data.MealsRepository;

public class ProfilePresenterImpl implements ProfilePresenter {
    MealsRepository repo;

    public ProfilePresenterImpl(MealsRepository repo) {
        this.repo = repo;
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
}
