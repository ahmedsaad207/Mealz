package com.example.mealz.view.mealslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.file.MealFileDataSource;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealsListBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealslist.MealsListPresenter;
import com.example.mealz.presenter.mealslist.MealsListPresenterImpl;
import com.example.mealz.presenter.mealslist.MealsListView;
import com.example.mealz.view.MealAdapter;

import java.util.List;

public class MealsListFragment extends Fragment implements MealsListView {
    FragmentMealsListBinding binding;
    MealAdapter adapter;
    MealsListPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        presenter = new MealsListPresenterImpl(
                MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSource.getInstance(requireActivity())
                ), this);

        MealsListFragmentArgs args = MealsListFragmentArgs.fromBundle(getArguments());
        String name = args.getName();
        boolean isCategory = args.getIsCategory();
        String title;
        if (isCategory) {
            title = (name + " Meals");
            presenter.getMealsByCategory(name);
        } else {
            title = "Popular Meals in " + name;
            presenter.getMealsByArea(name);
        }

        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void displayMeals(List<Meal> meals) {
        adapter = new MealAdapter(id -> {
            Navigation.findNavController(binding.rvMeals).navigate(MealsListFragmentDirections.actionMealsListFragmentToMealDetailsFragment(id));
        });
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
    }
}