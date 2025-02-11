package com.example.mealz.view.mealdetails;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.data.api.MealzApiService;
import com.example.mealz.data.api.MealzRetrofit;
import com.example.mealz.databinding.FragmentMealDetailsBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.IngredientModel;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailsFragment extends Fragment {
    FragmentMealDetailsBinding binding;

    IngredientAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        requireActivity().setTitle("");

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");

        }
        prepareVideoPlayer();

        long mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getId();

        MealzApiService service = MealzRetrofit.getService();
        service.getMealById(mealId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MealzResponse> call, @NonNull Response<MealzResponse> response) {
                if (response.body() != null && !response.body().meals.isEmpty()) {
                    Meal meal = response.body().meals.get(0);
                    binding.mealNameTextView.setText(meal.getMealName());
                    binding.mealCategoryTextView.setText(meal.getMealCategory());
                    binding.mealInstructionsTextView.setText(meal.getMealInstructions());
                    binding.mealAreaTextView.setText(meal.getMealArea());
                    displayYoutube(meal.getMealYoutube());
                    Glide.with(binding.mealImage.getContext()).load(meal.getMealImage()).into(binding.mealImage);
                    displayIngredients(meal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealzResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareVideoPlayer() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient());
    }

    private void displayYoutube(String youtubeUrl) {
        if (youtubeUrl != null && youtubeUrl.contains("v=")) {
            String videoId = youtubeUrl.substring(youtubeUrl.indexOf("v=") + 2);
            String path = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
            binding.webView.loadData(path, "text/html", "UTF-8");
        }
    }


    private void displayIngredients(Meal meal) {
        List< Ingredient> ingredients = new IngredientModel(meal).getIngredients();
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
            // TODO save Meal to Room

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}