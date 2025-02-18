package com.example.mealz.data;

import com.example.mealz.utils.Constants;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;

public class UserLocalDataSourceImpl implements UserLocalDataSource{
    private final RxSharedPreferences rxSharedPreferences;
    private static UserLocalDataSourceImpl instance;



    public static UserLocalDataSourceImpl getInstance(RxSharedPreferences rxSharedPreferences) {
        if (instance == null) {
            instance = new UserLocalDataSourceImpl(rxSharedPreferences);
        }
        return instance;
    }

    public UserLocalDataSourceImpl(RxSharedPreferences rxSharedPreferences) {
        this.rxSharedPreferences = rxSharedPreferences;
    }

    public Observable<String> getUserId() {
        return rxSharedPreferences.getString(Constants.KEY_USER_ID, "").asObservable();
    }

    public Observable<String> getUsername() {
        return rxSharedPreferences.getString(Constants.KEY_USERNAME).asObservable();
    }

    public void clearUserId(){
        rxSharedPreferences.getString(Constants.KEY_USER_ID, "").set("");
    }

    public void clearUsername() {
        rxSharedPreferences.getString(Constants.KEY_USERNAME, "").set("");
    }

    public void setUserId(String userId) {
        rxSharedPreferences.getString(Constants.KEY_USER_ID, "").set(userId);
    }

    public void setUsername(String username) {
        rxSharedPreferences.getString(Constants.KEY_USERNAME, "").set(username);
    }

    @Override
    public void setRememberMe(boolean value) {
        rxSharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false).set(value);
    }

    @Override
    public Observable<Boolean> getRememberMe() {
        return rxSharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false).asObservable();
    }
}
