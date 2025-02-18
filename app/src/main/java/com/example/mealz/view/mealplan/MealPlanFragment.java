package com.example.mealz.view.mealplan;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealPlanBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealplan.MealPlanPresenter;
import com.example.mealz.presenter.mealplan.MealPlanPresenterImpl;
import com.example.mealz.presenter.mealplan.MealPlanView;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;
import com.example.mealz.view.OnSignUpClickListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MealPlanFragment extends Fragment implements OnDayItemClickListener, MealPlanView, OnMealItemClickListener {
    FragmentMealPlanBinding binding;
    List<List<Meal>> currentWeekList;
    DateFormat format;
    Calendar calendar;

    DayAdapter dayAdapter;
    MealPlanPresenter presenter;
    MealAdapter<Meal> mealAdapter;
    List<Integer> days;

    OnSignUpClickListener onSignUpClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpClickListener) {
            onSignUpClickListener = (OnSignUpClickListener) context;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_plan, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBottomNavBar();
        init();
        fillCurrentWeek();

        dayAdapter = new DayAdapter(this, days);
        binding.rvDays.setAdapter(dayAdapter);
        presenter.getPlannedMeals();
        presenter.getUsername();
    }

    private void init() {
        presenter = new MealPlanPresenterImpl(
                MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSourceImpl.getInstance(requireActivity()),
                        UserLocalDataSourceImpl.getInstance(RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE)))
                ), this);
        calendar = Calendar.getInstance();
        days = new ArrayList<>();
        currentWeekList = new ArrayList<>();
        format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    }

    private void showBottomNavBar() {
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
    }

    private void fillCurrentWeekList(List<Meal> meals) {
        calendar = Calendar.getInstance();
        for (int i = 1; i <= 7; i++) {
            Date date = calendar.getTime();
            List<Meal> dayList = new ArrayList<>();
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

    private void fillCurrentWeek() {
        // formats
        // "dd-MM-yyyy" → "12-02-2025"
        // "EEEE" -> Wednesday
        // "yyyy-MM-dd HH:mm:ss"
        // Tue 11/2

        for (int i = 0; i < 7; i++) {
            days.add(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    public void displayMeals(Integer selectedDay) {
        Optional<Integer> currentDayInteger = days.stream().filter(integer -> integer.equals(selectedDay)).findFirst();

        if (currentDayInteger.isPresent()) {
            int index = days.indexOf(currentDayInteger.get());
            List<Meal> meals = currentWeekList.get(index);
            mealAdapter.submitList(meals);

            if (meals.isEmpty()) {
                binding.loadingPlan.setVisibility(View.VISIBLE);
                binding.emptyPlanTextView.setVisibility(View.VISIBLE);
            } else {
                binding.loadingPlan.setVisibility(View.GONE);
                binding.emptyPlanTextView.setVisibility(View.GONE);
                binding.rvPlannedMeals.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void displayFirstDayInCurrentWeek(List<Meal> meals) {
        fillCurrentWeekList(meals);
        mealAdapter = new MealAdapter<>(this);
        mealAdapter.submitList(currentWeekList.get(0));
        binding.rvPlannedMeals.setAdapter(mealAdapter);

        binding.loadingPlan.setVisibility(View.GONE);
        binding.rvPlannedMeals.setVisibility(View.VISIBLE);

        if (meals.isEmpty()) {
            binding.loadingPlan.setVisibility(View.VISIBLE);
//            binding.emtyFavoritesTextView.setVisibility(View.VISIBLE);
        } else {
            binding.loadingPlan.setVisibility(View.GONE);
//            binding.emtyFavoritesTextView.setVisibility(View.GONE);
            binding.rvPlannedMeals.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayUserName(String username) {
        if (getContext() != null && !username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, username));
        } else if (getContext() != null && username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, "Guest"));
            new AlertDialog.Builder(requireActivity())
                    .setMessage("You need to sign up to add meals to your favorites, plan and more features.")
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    })
                    .setPositiveButton("Sign up", (dialog, which) -> {
                        onSignUpClickListener.onSignUp();
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Navigation.findNavController(binding.rvPlannedMeals).navigate(
                MealPlanFragmentDirections.actionMealPlanFragmentToMealDetailsFragment(meal));
    }

    @Override
    public void navigateToMealsList(String name, int type) {

    }

    @Override
    public void removeMealFromFavorites(Meal meal) {

    }

}