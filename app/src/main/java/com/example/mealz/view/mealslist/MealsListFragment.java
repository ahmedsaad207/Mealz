package com.example.mealz.view.mealslist;

import static android.content.Context.MODE_PRIVATE;

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
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealsListBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealslist.MealsListPresenter;
import com.example.mealz.presenter.mealslist.MealsListPresenterImpl;
import com.example.mealz.presenter.mealslist.MealsListView;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.List;

public class MealsListFragment extends Fragment implements MealsListView, OnMealItemClickListener {
    FragmentMealsListBinding binding;
    MealAdapter<Meal> adapter;
    MealsListPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideBottomNavBar();
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        presenter = new MealsListPresenterImpl(MealsRepositoryImpl.getInstance(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE)))
        ), this);

        MealsListFragmentArgs args = MealsListFragmentArgs.fromBundle(getArguments());
        String name = args.getName();
        int type = args.getType();
        String title;

        if (type == Constants.ITEM_CATEGORY) {
            title = ("Tasty Dishes from " + name);
            presenter.getMealsByCategory(name);
        } else if (type == Constants.ITEM_AREA) {
            title = "Popular Meals in " + name;
            presenter.getMealsByArea(name);
        } else if (type == Constants.ITEM_INGREDIENT) {
            title = "Discover Meals with  " + name;
            presenter.searchByIngredient(name);
        } else {
            title = "";
        }

        setupToolBar(actionBar, title);
    }

    private static void setupToolBar(ActionBar actionBar, String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void hideBottomNavBar() {
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
    }

    @Override
    public void displayMeals(List<Meal> meals) {
        adapter = new MealAdapter<>(this);
        adapter.submitList(meals);
        binding.rvMeals.setAdapter(adapter);
        if (!meals.isEmpty() && binding.loadingMealsList != null){
            binding.loadingMealsList.setVisibility(View.GONE);
            binding.rvMeals.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Navigation.findNavController(binding.rvMeals).navigate(MealsListFragmentDirections.actionMealsListFragmentToMealDetailsFragment(meal));
    }

    @Override
    public void navigateToMealsList(String name, int type) {

    }

    @Override
    public void removeMealFromFavorites(Meal meal) {

    }

}