package com.example.mealz.view.mealdetails;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealDetailsBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenter;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenterImpl;
import com.example.mealz.presenter.mealdetails.MealDetailsView;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.Utils;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView {
    FragmentMealDetailsBinding binding;

    IngredientAdapter adapter;

    Meal currentMeal;

    MealDetailsPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        setListeners();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        presenter = new MealDetailsPresenterImpl(
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSourceImpl.getInstance(requireActivity()),
                        UserLocalDataSourceImpl.getInstance(
                                RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))
                        )),
                this
        );

        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

        prepareVideoPlayer();

        Meal meal = MealDetailsFragmentArgs.fromBundle(getArguments()).getMeal();

        if (meal.getDate() == Constants.TYPE_DEFAULT) {
            presenter.getMealById(meal.getNetworkId());
        } else if (meal.getDate() == Constants.TYPE_FAVORITE) {
            displayMeal(meal);
        } else {
            displayMeal(meal);
        }
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });

        binding.btnPlan.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.btnFav.setOnClickListener(v ->
        {
            if (currentMeal.getDate() == Constants.TYPE_FAVORITE) {
                currentMeal.setDate(Constants.TYPE_DEFAULT);
                presenter.deleteMeal(currentMeal);
            } else {
                currentMeal.setDate(Constants.TYPE_FAVORITE);
                presenter.insertFavMeal(currentMeal);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void prepareVideoPlayer() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.brownish_gray));
    }

    private void displayYoutube(String youtubeUrl) {
        if (Utils.getVideoIframe(youtubeUrl) != null) {
            binding.webView.loadData(Utils.getVideoIframe(youtubeUrl), "text/html", "UTF-8");
        } else {
            binding.webView.setVisibility(View.GONE);
            binding.videoLabel.setVisibility(View.GONE);
        }
    }

    private void displayIngredients(List<Ingredient> ingredients) {
        adapter = new IngredientAdapter();
        adapter.submitList(ingredients);
        binding.rvIngredients.setAdapter(adapter);
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), R.style.DialogTheme, (view, year1, month1, dayOfMonth1) -> {
            calendar.set(year1, month1, dayOfMonth1);
            currentMeal.setDate(calendar.getTimeInMillis());
            presenter.insertPlanMeal(currentMeal);
        }, year, month, dayOfMonth);

        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        long startOfWeek = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth + 6);
        long endOfWeek = calendar.getTimeInMillis();
        dialog.getDatePicker().setMinDate(startOfWeek);
        dialog.getDatePicker().setMaxDate(endOfWeek);
        dialog.show();
    }

    @Override
    public void displayMeal(Meal meal) {
        binding.groupFab.setVisibility(View.VISIBLE);
        currentMeal = meal;
        binding.mealNameTextView.setText(meal.getName());
        binding.mealCategoryTextView.setText(getString(R.string.beef_popular_in, meal.getCategory()));
        binding.videoLabel.setText(getString(R.string.title_video, meal.getName()));
        binding.mealInstructionsTextView.setText(meal.getInstructions());
        binding.areaTextView.setText(meal.getArea());
        displayYoutube(meal.getYoutubeUrl());
        binding.areaImageView.setImageResource(Utils.getDrawableResourceForCountry(meal.getArea(), requireActivity()));
        Glide.with(binding.mealImage.getContext()).load(meal.getUrlImage()).into(binding.mealImage);
        displayIngredients(meal.getIngredients());

        if (meal.getDate() == Constants.TYPE_FAVORITE) {
            binding.btnFav.setImageResource(R.drawable.ic_fav_added);
        } else {
            presenter.isFavMealExist(meal.getNetworkId());
        }
    }

    @Override
    public void onSuccess() {
        if (currentMeal.getDate() == Constants.TYPE_FAVORITE) {
            showMessage("Added Successfully to Your Favorites!");
            binding.btnFav.setImageResource(R.drawable.ic_fav_added);
        } else if (currentMeal.getDate() == Constants.TYPE_DEFAULT) {
            showMessage("Deleted from Your Favorites!");
            binding.btnFav.setImageResource(R.drawable.ic_fav_add);
        } else {
            showMessage("Added Successfully to Your Plan!");
        }
    }

    @Override
    public void insertMeal() {
        presenter.insertMeal(currentMeal);
    }

    @Override
    public void changeImageResourceForFav() {
        currentMeal.setDate(Constants.TYPE_FAVORITE);
        binding.btnFav.setImageResource(R.drawable.ic_fav_added);
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}