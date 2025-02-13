package com.example.mealz.view.mealplan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mealz.R;
import com.example.mealz.data.local.MealDao;
import com.example.mealz.data.local.MealsDatabase;
import com.example.mealz.databinding.FragmentMealPlanBinding;
import com.example.mealz.model.Meal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanFragment extends Fragment {
    FragmentMealPlanBinding binding;
    List<List<Meal>> currentWeekList;
    List<Meal> prevMeals;
    DateFormat format;
    Calendar calendar;
    MealDao database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_plan, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        prevMeals = new ArrayList<>();
        calendar = Calendar.getInstance();
        format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentWeekList = new ArrayList<>();
        getCurrentWeekMeals();
    }

    private void getCurrentWeekMeals() {
        database = MealsDatabase.getInstance(requireActivity()).getMealDao();
        Disposable disposable = database.getPlannedMeals("ahmed")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::fillCurrentWeekList,
                        throwable -> {
                            Log.i("TAG", "on error");
                        },
                        () -> {
                            Log.i("TAG", "on complete");
                        });
    }

    private void fillCurrentWeekList(List<Meal> meals) {
        for (int i = 1; i <= 7; i++) {
            List<Meal> dayList = new ArrayList<>();
            Date date = calendar.getTime();
            String currentDay = format.format(date);
            for (Meal meal : meals) {
                if (format.format(meal.getDate()).equals(currentDay)) {
                    dayList.add(meal);
                }
            }
            currentWeekList.add(dayList);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private String[] getCurrentWeek() {
        DateFormat format = new SimpleDateFormat("EEEE", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[7];

        // formats
        // "dd-MM-yyyy" → "12-02-2025"
        // "EEEE" -> Wednesday
        // "yyyy-MM-dd HH:mm:ss"
        // Tue 11/2

        for (int i = 0; i < 7; i++) {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        for (String day : days) {
        }
        return days;
    }
}