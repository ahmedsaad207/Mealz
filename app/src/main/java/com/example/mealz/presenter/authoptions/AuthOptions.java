package com.example.mealz.presenter.authoptions;

public interface AuthOptions {
    void saveCredential(String userId, String username);

    void setRememberMe(boolean isLogged);
}
