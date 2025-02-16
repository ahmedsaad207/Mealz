package com.example.mealz.view.favorite;

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
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentFavoriteBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.favorite.FavoritePresenter;
import com.example.mealz.presenter.favorite.FavoritePresenterImpl;
import com.example.mealz.presenter.favorite.FavoriteView;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView, OnMealItemClickListener {

    FragmentFavoriteBinding binding;
    MealAdapter<Meal> adapter;
    FavoritePresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new FavoritePresenterImpl(MealsRepositoryImpl.getInstance(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity())
        ), this);

        presenter.getFavoriteMeals("ahmed");

    }

    @Override
    public void displayFavoriteMeals(List<Meal> meals) {
        adapter = new MealAdapter<>(this);
        adapter.submitList(meals);
        binding.rvFavMeals.setAdapter(adapter);
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Navigation.findNavController(binding.getRoot()).navigate(FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(meal));
    }

    @Override
    public void navigateToMealsList(String name, int type) {

    }
}