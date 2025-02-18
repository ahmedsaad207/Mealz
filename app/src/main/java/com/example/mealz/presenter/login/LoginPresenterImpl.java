package com.example.mealz.presenter.login;

import com.example.mealz.data.MealsRepository;

public class LoginPresenterImpl implements LoginPresenter{

    MealsRepository repo;

    public LoginPresenterImpl(MealsRepository repo) {
        this.repo = repo;
    }

    @Override
    public void saveCredential(String userId, String username) {
        repo.setUserId(userId);
        repo.setUsername(username);
    }

    @Override
    public void setRememberMe(boolean value) {
        repo.setRememberMe(value);
    }
}
