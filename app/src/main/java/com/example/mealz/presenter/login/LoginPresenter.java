package com.example.mealz.presenter.login;

public interface LoginPresenter {

    void saveCredential(String userId, String username);

    void setRememberMe(boolean value);
}
