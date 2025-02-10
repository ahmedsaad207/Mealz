package com.example.mealz.view.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.api.MealzApiService;
import com.example.mealz.data.api.MealzRetrofit;
import com.example.mealz.databinding.FragmentHomeBinding;
import com.example.mealz.model.Area;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FragmentHomeBinding binding;

    CategoryAdapter categoryAdapter;
    DailyInspirationAdapter dailyInspirationAdapter;

    ArrayList<Meal> meals;
    AreaAdapter areaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding.rvCategories.setHasFixedSize(true);
        binding.rvDailyInspiration.setHasFixedSize(true);
        meals = new ArrayList<>();

        MealzApiService service = MealzRetrofit.getService();
        Callback<MealzResponse> categoriesCallback = new Callback<>() {
            @Override
            public void onResponse(Call<MealzResponse> call, Response<MealzResponse> response) {
                if (response.body() != null && !response.body().categories.isEmpty()) {
                    categoryAdapter = new CategoryAdapter();
                    binding.rvCategories.setAdapter(categoryAdapter);
                    categoryAdapter.submitList(response.body().categories);

                    // Tried to init adapter outside onResponse method but when submitList called here. no data get displayed
                    categoryAdapter.setOnItemClickListener(categoryName -> {
                        Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToMealsListFragment(categoryName, true));
                    });
                }
            }

            @Override
            public void onFailure(Call<MealzResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        Callback<MealzResponse> dailyInspirationCallback = new Callback<>() {
            @Override
            public void onResponse(Call<MealzResponse> call, Response<MealzResponse> response) {
                if (response.body() != null && !response.body().meals.isEmpty()) {
                    Meal meal = response.body().meals.get(0);
                    meals.add(meal);

                    if (dailyInspirationAdapter == null) {
                        dailyInspirationAdapter = new DailyInspirationAdapter(mealId -> {
                            Navigation.findNavController(view).navigate(
                                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealId));
                        });
                    }

                    binding.rvDailyInspiration.setAdapter(dailyInspirationAdapter);
                    dailyInspirationAdapter.submitList(meals);
                }

            }

            @Override
            public void onFailure(Call<MealzResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        service.getCategories().enqueue(categoriesCallback);
        service.getRandomMeal().enqueue(dailyInspirationCallback);
        service.getRandomMeal().enqueue(dailyInspirationCallback);
        service.getRandomMeal().enqueue(dailyInspirationCallback);
        service.getRandomMeal().enqueue(dailyInspirationCallback);
        service.getRandomMeal().enqueue(dailyInspirationCallback);

        service.getAreas().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MealzResponse> call, Response<MealzResponse> response) {
                if (response.body() != null && !response.body().meals.isEmpty()) {
                    List<Meal> meals = response.body().meals;
                    Log.i("TAG", "onResponse: meals size: " + meals.size());
                    List<Area> areas = createAreasList(meals);
                    areaAdapter = new AreaAdapter(areaName -> {
                        Navigation.findNavController(view).navigate(
                                HomeFragmentDirections.actionHomeFragmentToMealsListFragment(areaName, false)
                        );
                    });
                    binding.rvAreas.setAdapter(areaAdapter);
                    areaAdapter.submitList(areas);
                    
                    Log.i("TAG", "onResponse: areas size: " + areas.size());

                }

            }

            @Override
            public void onFailure(Call<MealzResponse> call, Throwable t) {

            }
        });


        /*
        binding.btnSignOut.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
            }
        });
        */
    }

    public List<Area> createAreasList(List<Meal> meals) {
        ArrayList<Area> areas = new ArrayList<>();

        for (Meal meal : meals) {
            int imageResource = getDrawableResourceForCountry(meal.getMealArea());
            if (imageResource != 0) {
                areas.add(new Area(meal.getMealArea(), imageResource));
            }
        }

        return areas;
    }

    private int getDrawableResourceForCountry(String countryName) {
        Resources resources = getResources();
        return resources.getIdentifier(
                countryName.toLowerCase(),
                "drawable",
                requireActivity().getPackageName());
    }
}