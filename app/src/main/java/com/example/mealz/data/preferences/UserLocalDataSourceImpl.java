package com.example.mealz.data.preferences;

import com.example.mealz.utils.Constants;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;

public class UserLocalDataSourceImpl implements UserLocalDataSource {
    private static UserLocalDataSourceImpl instance;
    private final RxSharedPreferences rxSharedPreferences;


    public UserLocalDataSourceImpl(RxSharedPreferences rxSharedPreferences) {
        this.rxSharedPreferences = rxSharedPreferences;
    }

    public static UserLocalDataSourceImpl getInstance(RxSharedPreferences rxSharedPreferences) {
        if (instance == null) {
            instance = new UserLocalDataSourceImpl(rxSharedPreferences);
        }
        return instance;
    }

    public Observable<String> getUserId() {
        return rxSharedPreferences.getString(Constants.KEY_USER_ID, "").asObservable();
    }

    public void setUserId(String userId) {
        rxSharedPreferences.getString(Constants.KEY_USER_ID, "").set(userId);
    }

    public Observable<String> getUsername() {
        return rxSharedPreferences.getString(Constants.KEY_USERNAME).asObservable();
    }

    public void setUsername(String username) {
        rxSharedPreferences.getString(Constants.KEY_USERNAME, "").set(username);
    }

    public void clearUserId() {
        rxSharedPreferences.getString(Constants.KEY_USER_ID, "").set("");
    }

    public void clearUsername() {
        rxSharedPreferences.getString(Constants.KEY_USERNAME, "").set("");
    }

    @Override
    public Observable<Boolean> getRememberMe() {
        return rxSharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false).asObservable();
    }

    @Override
    public void setRememberMe(boolean value) {
        rxSharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false).set(value);
    }
}
