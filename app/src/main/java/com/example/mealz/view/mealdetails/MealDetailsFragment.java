package com.example.mealz.view.mealdetails;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.preferences.UserLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentMealDetailsBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenter;
import com.example.mealz.presenter.mealdetails.MealDetailsPresenterImpl;
import com.example.mealz.presenter.mealdetails.MealDetailsView;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.NetworkManager;
import com.example.mealz.utils.Utils;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

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

        binding.contentDetails.setVisibility(View.GONE);
        binding.loadingDetails.setVisibility(View.VISIBLE);

        presenter = new MealDetailsPresenterImpl(
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireActivity()),
                        MealFileDataSourceImpl.getInstance(requireActivity()),
                        UserLocalDataSourceImpl.getInstance(
                                RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))
                        ), BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())
                ),
                this
        );

        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

        prepareVideoPlayer();

        Meal meal = MealDetailsFragmentArgs.fromBundle(getArguments()).getMeal();

        if (meal.getDate() == Constants.TYPE_DEFAULT) { // network
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


            if (isConnected()) {
                if (currentMeal.getDate() == Constants.TYPE_FAVORITE) {
                    presenter.removeMealFromFavorites(currentMeal);
                } else {
                    presenter.insertFavMeal(currentMeal);
                }
                binding.btnFav.setEnabled(false);
            } else {
                Snackbar.make(requireView(), "No internet connection!", Snackbar.LENGTH_SHORT).show();
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
            if (isConnected()) {
                calendar.set(year1, month1, dayOfMonth1);
                currentMeal.setDate(calendar.getTimeInMillis());
                presenter.insertMeal(currentMeal);
            } else {
                Snackbar.make(requireView(), "No internet connection!", Snackbar.LENGTH_SHORT).show();
            }

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
        if (meal != null) {
            currentMeal = meal;
            if (meal.getDate() == Constants.TYPE_FAVORITE) {
                currentMeal.setUserId(meal.getUserId());
                binding.groupFab.setVisibility(View.VISIBLE);
            } else {
                presenter.getUserId();
            }
            binding.loadingDetails.setVisibility(View.GONE);
            binding.contentDetails.setVisibility(View.VISIBLE);

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
    }

    @Override
    public void changeImageResourceForFav() {
        currentMeal.setDate(Constants.TYPE_FAVORITE);
        binding.btnFav.setImageResource(R.drawable.ic_fav_added);
    }

    @Override
    public void onUserId(String userId) {
        if (userId.isEmpty()) {
            binding.groupFab.setVisibility(View.GONE); //Guest Mode
        } else {
            currentMeal.setUserId(userId);
            binding.groupFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteComplete() {
        currentMeal.setDate(Constants.TYPE_DEFAULT);
        binding.btnFav.setEnabled(true);
        binding.btnFav.setImageResource(R.drawable.ic_fav_add);
        showMessage("Meal removed from favorites");
    }

    @Override
    public void onDeleteError(String error) {
        binding.btnFav.setEnabled(true);
        showMessage("Failed to remove meal from favorites: " + error);
    }

    @Override
    public void onInsertFavCompleted() {
        currentMeal.setDate(Constants.TYPE_FAVORITE);
        binding.btnFav.setEnabled(true);
        binding.btnFav.setImageResource(R.drawable.ic_fav_added);
        showMessage("Meal added to your favorites");
    }

    @Override
    public void onInsertError(String error) {
        binding.btnFav.setEnabled(true);
        showMessage("Failed to add meal: " + error);
    }

    @Override
    public void onFetchMealFailed(String error) {
        showMessage(error);
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void onInsertPlanCompleted() {
        Log.d("TAG", "detail plan type: when plan is pressed: " + currentMeal.getDate());
//        currentMeal.setDate(Constants.TYPE_FAVORITE);
        binding.btnFav.setEnabled(true);
//        binding.btnFav.setImageResource(R.drawable.ic_fav_added);
        showMessage("Meal added to your plan");
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        return NetworkManager.isConnected(requireContext());
    }
}