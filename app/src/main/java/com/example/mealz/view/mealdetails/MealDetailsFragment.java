package com.example.mealz.view.mealdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.data.file.MealFileDataSource;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealDetailsBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenter;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenterImpl;
import com.example.mealz.presenter.mealdetails.MealDetailsView;
import com.example.mealz.utils.Utils;

import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView {
    FragmentMealDetailsBinding binding;

    IngredientAdapter adapter;

    Meal currentMeal;

    List<Ingredient> ingredients;
    MealDetailsPresenter presenter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");

        }

        presenter = new MealDetailsPresenterImpl(
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSource.getInstance(requireActivity())),
                this
        );

        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

        prepareVideoPlayer();

        long mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getId();
        presenter.getMealById(mealId);
    }

    private void prepareVideoPlayer() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setBackgroundColor(getResources().getColor(R.color.brownish_gray));
    }

    private void displayYoutube(String youtubeUrl) {
        if (Utils.getVideoIframe(youtubeUrl) != null) {
            binding.webView.loadData(Utils.getVideoIframe(youtubeUrl), "text/html", "UTF-8");
        }
    }

    private void displayIngredients(List<Ingredient> ingredients) {
        adapter = new IngredientAdapter();
        adapter.submitList(ingredients);
        binding.rvIngredients.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            presenter.insertFavMeal(currentMeal);
            return true;
        } else if (item.getItemId() == R.id.action_plan) {
            presenter.insertPlanMeal(currentMeal);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayMeal(Meal meal) {
        currentMeal = meal;
        binding.mealNameTextView.setText(meal.getName());
        binding.mealCategoryTextView.setText(meal.getCategory());
        binding.mealInstructionsTextView.setText(meal.getInstructions());
        binding.mealAreaTextView.setText(meal.getArea());
        displayYoutube(meal.getYoutubeUrl());
        Glide.with(binding.mealImage.getContext()).load(meal.getUrlImage()).into(binding.mealImage);
        displayIngredients(meal.getIngredients());
    }
}