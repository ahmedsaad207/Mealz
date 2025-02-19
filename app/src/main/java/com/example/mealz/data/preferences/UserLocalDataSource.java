package com.example.mealz.data.preferences;

import io.reactivex.Observable;

public interface UserLocalDataSource {

     Observable<String> getUserId();

     void setUserId(String userId);

     Observable<String> getUsername();

     void setUsername(String username);
     void setRememberMe(boolean isLogged);

     void clearUserId();

     void clearUsername();

     Observable<Boolean> getRememberMe();
}
