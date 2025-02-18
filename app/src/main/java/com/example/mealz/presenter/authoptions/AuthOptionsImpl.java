package com.example.mealz.presenter.authoptions;

import com.example.mealz.data.MealsRepository;

public class AuthOptionsImpl implements AuthOptions{

    MealsRepository repo;

    public AuthOptionsImpl(MealsRepository repo) {
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
