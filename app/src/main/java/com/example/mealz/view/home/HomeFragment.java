package com.example.mealz.view.home;

import static com.example.mealz.utils.MealMapper.mapMealsToAreas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.mealz.data.file.MealFileDataSource;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentHomeBinding;
import com.example.mealz.model.Area;
import com.example.mealz.model.Category;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.home.HomePresenterImpl;
import com.example.mealz.presenter.home.HomeView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView {
    FirebaseAuth mAuth;
    FragmentHomeBinding binding;

    CategoryAdapter categoryAdapter;
    DailyInspirationAdapter dailyInspirationAdapter;
    AreaAdapter areaAdapter;
    HomePresenterImpl presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        binding.rvCategories.setHasFixedSize(true);
        binding.rvDailyInspiration.setHasFixedSize(true);
        presenter = new HomePresenterImpl(
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSource.getInstance(requireActivity())),
                this
        );

        presenter.getRandomMeal();
        presenter.getCategories();
        presenter.getAreas();

        binding.searchEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    binding.homeData.setVisibility(View.VISIBLE);
                } else {
                    binding.homeData.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void displayCategories(ArrayList<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            // TODO Empty List
            return;
        }
        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.submitList(categories);
        categoryAdapter.setOnItemClickListener(categoryName -> Navigation.findNavController(binding.rvCategories).navigate(HomeFragmentDirections.actionHomeFragmentToMealsListFragment(categoryName, true)));
    }

    @Override
    public void displayAreas(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            // TODO Empty List
            return;
        }
        List<Area> areas = mapMealsToAreas(meals, requireActivity());
        areaAdapter = new AreaAdapter(areaName -> Navigation.findNavController(binding.rvAreas).navigate(
                HomeFragmentDirections.actionHomeFragmentToMealsListFragment(areaName, false)
        ));
        binding.rvAreas.setAdapter(areaAdapter);
        areaAdapter.submitList(areas);
    }

    @Override
    public void displayDailyInspiration(List<Meal> meals) {
//        networkMeals.add(meal);

        if (dailyInspirationAdapter == null) {
            dailyInspirationAdapter = new DailyInspirationAdapter(mealId -> Navigation.findNavController(binding.rvDailyInspiration).navigate(
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealId)));
        }

        binding.rvDailyInspiration.setAdapter(dailyInspirationAdapter);
        dailyInspirationAdapter.submitList(meals);
    }
}