package com.example.mealz.data.backup;

import android.util.Log;

import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.model.Meal;
import com.example.mealz.utils.Constants;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BackUpRemoteDataSourceImpl implements BackUpRemoteDataSource {

    private final FirebaseDatabase database;
    private static BackUpRemoteDataSourceImpl instance;



    public static BackUpRemoteDataSourceImpl getInstance(FirebaseDatabase database) {
        if (instance == null) {
            instance = new BackUpRemoteDataSourceImpl(database);
        }
        return instance;
    }

    public BackUpRemoteDataSourceImpl(FirebaseDatabase database) {
        this.database = database;
    }

    @Override
    public void removeMealFromFavorites(Meal meal, OnMealRemovedListener onMealRemovedListener) {
        database.getReference(Constants.BACKUP_USERS)
                .child(meal.getUserId())
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? Constants.BACKUP_FAV : Constants.BACKUP_PLAN)
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
                .removeValue()
                .addOnSuccessListener(command -> onMealRemovedListener.onMealRemoved(meal))
                .addOnFailureListener(command -> Log.d("TAG", "meal failed to delete from firebase"));

    }

    @Override
    public void retrieveBackupMeals(String userId, OnDataReceivedListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(userId).child(Constants.BACKUP_FAV).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<Meal> meals = new ArrayList<>();
                for (DataSnapshot mealSnapShot : snapshot.getChildren()) {
                    meals.add(mealSnapShot.getValue(Meal.class));
                }
                listener.onDataReceived(meals);
                Log.d("TAG", "firebase fav meals: " + meals.size());
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

        myRef.child(userId).child(Constants.BACKUP_PLAN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<Meal> meals = new ArrayList<>();
                for (DataSnapshot mealSnapShot : snapshot.getChildren()) {
                    meals.add(mealSnapShot.getValue(Meal.class));
                }
                listener.onDataReceived(meals);
                Log.d("TAG", "firebase plans size: " + meals.size());
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void backUp(Meal meal) {
        database.getReference(Constants.BACKUP_USERS)
                .child(meal.getUserId())
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? Constants.BACKUP_FAV : Constants.BACKUP_PLAN)
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
                .setValue(meal).addOnSuccessListener(command -> {
                    Log.d("TAG", "added to firebase");
                });
    }

    public interface OnMealRemovedListener {
            void onMealRemoved(Meal meal);
    }

    public interface OnDataReceivedListener {
        void onDataReceived(List<Meal> meals);
    }
}
