package com.example.mealz.view.mealdetails;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mealz.R;
import com.example.mealz.data.api.MealzApiService;
import com.example.mealz.data.api.MealzRetrofit;
import com.example.mealz.data.local.MealsDatabase;
import com.example.mealz.databinding.FragmentMealDetailsBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.IngredientModel;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;
import com.example.mealz.model.NetworkMeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailsFragment extends Fragment {
    FragmentMealDetailsBinding binding;

    IngredientAdapter adapter;

    NetworkMeal networkMeal;

    List<Ingredient> ingredients;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

        prepareVideoPlayer();

        long mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getId();

        MealzApiService service = MealzRetrofit.getService();
        service.getMealById(mealId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MealzResponse> call, @NonNull Response<MealzResponse> response) {
                if (response.body() != null && !response.body().meals.isEmpty()) {
                    networkMeal = response.body().meals.get(0);
                    binding.mealNameTextView.setText(networkMeal.getMealName());
                    binding.mealCategoryTextView.setText(networkMeal.getMealCategory());
                    binding.mealInstructionsTextView.setText(networkMeal.getMealInstructions());
                    binding.mealAreaTextView.setText(networkMeal.getMealArea());
                    displayYoutube(networkMeal.getMealYoutube());
                    Glide.with(binding.mealImage.getContext()).load(networkMeal.getMealImage()).into(binding.mealImage);
                    displayIngredients(networkMeal);
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


    private void displayIngredients(NetworkMeal networkMeal) {
        ingredients = new IngredientModel(networkMeal).getIngredients();
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
            insertFavMeal();
            return true;
        } else if (item.getItemId() == R.id.action_plan) {
            insertPlanMeal();
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPlanMeal() {
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
        insertMeal(calendar.getTime().getTime());
    }

    private void insertFavMeal() {
        Disposable disposable = MealsDatabase.getInstance(requireActivity()).getMealDao().isFavMealExist("ahmed", networkMeal.getMealId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(meal -> {
                            Toast.makeText(requireActivity(), "Already Saved", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            insertMeal(0);
                        });

    }

    private void insertMeal(long date) {
        MealsDatabase.getInstance(requireActivity())
                .getMealDao()
                .insertMeal(convertToMealModel(networkMeal, date))
                .subscribeOn(Schedulers.io())
                .subscribe();

        downloadIngredientImages();
        downloadImage();
    }

    private Meal convertToMealModel(NetworkMeal networkMeal, long date) {
        Meal meal = new Meal();
        meal.setNetworkId(networkMeal.getMealId());
        meal.setArea(networkMeal.getMealArea());
        meal.setName(networkMeal.getMealName());
        meal.setCategory(networkMeal.getMealCategory());
        meal.setInstructions(networkMeal.getMealInstructions());
        meal.setUrlImage(networkMeal.getMealImage());
        meal.setUserId("ahmed");
        meal.setDate(date);
        meal.setIngredients(ingredients);

        return meal;
    }

    private void downloadImage() {
        File dir = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "meals");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = networkMeal.getMealImage().substring(networkMeal.getMealImage().lastIndexOf("/") + 1);
        File file = new File(dir, fileName);

        if (file.exists()) {
            Toast.makeText(requireActivity(), "already saved", Toast.LENGTH_SHORT).show();
            return;
        }


        Glide.
                with(requireActivity())
                .asBitmap()
                .load(networkMeal.getMealImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Toast.makeText(requireActivity(), "saved", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    private void downloadIngredientImages() {
        File dir = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ingredients");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (Ingredient ingredient : ingredients) {

            String fileName = ingredient.getImageUrl().substring(ingredient.getImageUrl().lastIndexOf("/") + 1);
            File file = new File(dir, fileName);

            if (file.exists()) {
                continue;
            }

            Glide.
                    with(requireActivity())
                    .asBitmap()
                    .load(ingredient.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                String path = file.getAbsolutePath();

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }


    }
}