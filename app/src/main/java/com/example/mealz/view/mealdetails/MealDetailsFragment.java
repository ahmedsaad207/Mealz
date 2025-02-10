package com.example.mealz.view.mealdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    ArrayList<String> ingredients;

    ArrayList<String> measures;


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
//
//        ingredients = new ArrayList<>();
//        measures = new ArrayList<>();
//        if (meal.getMealIngredient1() != null && !meal.getMealIngredient1().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient1() + "-Small.png");
//            measures.add(meal.getMealMeasure1() == null ? "" : meal.getMealMeasure1());
//        }
//
//        if (meal.getMealIngredient2() != null && !meal.getMealIngredient2().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient2() + "-Small.png");
//            measures.add(meal.getMealMeasure2() == null ? "" : meal.getMealMeasure2());
//        }
//
//        if (meal.getMealIngredient3() != null && !meal.getMealIngredient3().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient3() + "-Small.png");
//            measures.add(meal.getMealMeasure3() == null ? "" : meal.getMealMeasure3());
//        }
//
//        if (meal.getMealIngredient4() != null && !meal.getMealIngredient4().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient4() + "-Small.png");
//            measures.add(meal.getMealMeasure4() == null ? "" : meal.getMealMeasure4());
//        }
//
//        if (meal.getMealIngredient5() != null && !meal.getMealIngredient5().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient5() + "-Small.png");
//            measures.add(meal.getMealMeasure5() == null ? "" : meal.getMealMeasure5());
//        }
//
//        if (meal.getMealIngredient6() != null && !meal.getMealIngredient6().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient6() + "-Small.png");
//            measures.add(meal.getMealMeasure6() == null ? "" : meal.getMealMeasure6());
//        }
//
//        if (meal.getMealIngredient7() != null && !meal.getMealIngredient7().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient7() + "-Small.png");
//            measures.add(meal.getMealMeasure7() == null ? "" : meal.getMealMeasure7());
//        }
//
//        if (meal.getMealIngredient8() != null && !meal.getMealIngredient8().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient8() + "-Small.png");
//            measures.add(meal.getMealMeasure8() == null ? "" : meal.getMealMeasure8());
//        }
//
//        if (meal.getMealIngredient9() != null && !meal.getMealIngredient9().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient9() + "-Small.png");
//            measures.add(meal.getMealMeasure9() == null ? "" : meal.getMealMeasure9());
//        }
//
//        if (meal.getMealIngredient10() != null && !meal.getMealIngredient10().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient10() + "-Small.png");
//            measures.add(meal.getMealMeasure10() == null ? "" : meal.getMealMeasure10());
//        }
//        if (meal.getMealIngredient11() != null && !meal.getMealIngredient11().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient11() + "-Small.png");
//            measures.add(meal.getMealMeasure11() == null ? "" : meal.getMealMeasure11());
//        }
//
//        if (meal.getMealIngredient12() != null && !meal.getMealIngredient12().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient12() + "-Small.png");
//            measures.add(meal.getMealMeasure12() == null ? "" : meal.getMealMeasure12());
//        }
//
//        if (meal.getMealIngredient13() != null && !meal.getMealIngredient13().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient13() + "-Small.png");
//            measures.add(meal.getMealMeasure13() == null ? "" : meal.getMealMeasure13());
//        }
//
//        if (meal.getMealIngredient14() != null && !meal.getMealIngredient14().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient14() + "-Small.png");
//            measures.add(meal.getMealMeasure14() == null ? "" : meal.getMealMeasure14());
//        }
//
//        if (meal.getMealIngredient15() != null && !meal.getMealIngredient15().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient15() + "-Small.png");
//            measures.add(meal.getMealMeasure15() == null ? "" : meal.getMealMeasure15());
//        }
//
//        if (meal.getMealIngredient16() != null && !meal.getMealIngredient16().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient16() + "-Small.png");
//            measures.add(meal.getMealMeasure16() == null ? "" : meal.getMealMeasure16());
//        }
//
//        if (meal.getMealIngredient17() != null && !meal.getMealIngredient17().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient17() + "-Small.png");
//            measures.add(meal.getMealMeasure17() == null ? "" : meal.getMealMeasure17());
//        }
//
//        if (meal.getMealIngredient18() != null && !meal.getMealIngredient18().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient18() + "-Small.png");
//            measures.add(meal.getMealMeasure18() == null ? "" : meal.getMealMeasure18());
//        }
//
//        if (meal.getMealIngredient19() != null && !meal.getMealIngredient19().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient19() + "-Small.png");
//            measures.add(meal.getMealMeasure19() == null ? "" : meal.getMealMeasure19());
//        }
//
//        if (meal.getMealIngredient20() != null && !meal.getMealIngredient20().trim().isEmpty()) {
//            ingredients.add(url + meal.getMealIngredient20() + "-Small.png");
//            measures.add(meal.getMealMeasure20() == null ? "" : meal.getMealMeasure20());
//        }

        List< Ingredient> ingredients = new IngredientModel(meal).getIngredients();
        adapter = new IngredientAdapter();
        adapter.submitList(ingredients);
        binding.rvIngredients.setAdapter(adapter);
    }

}