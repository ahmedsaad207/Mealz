package com.example.mealz.view.favorite;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
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
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.preferences.UserLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentFavoriteBinding;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.favorite.FavoritePresenter;
import com.example.mealz.presenter.favorite.FavoritePresenterImpl;
import com.example.mealz.presenter.favorite.FavoriteView;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.NetworkManager;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;
import com.example.mealz.view.OnSignUpClickListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView, OnMealItemClickListener {

    FragmentFavoriteBinding binding;
    MealAdapter<Meal> adapter;
    FavoritePresenter presenter;

    OnSignUpClickListener onSignUpClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpClickListener) {
            onSignUpClickListener = (OnSignUpClickListener) context;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBottomNavBar();
        setupToolBar();
        init();
        presenter.getFavoriteMeals();
    }

    private void setupToolBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("My Favorite Meals");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

    }

    private void init() {
        presenter = new FavoritePresenterImpl(MealsRepositoryImpl.getInstance(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))),
                BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())),
                this);
        adapter = new MealAdapter<>(this);

    }

    private void showBottomNavBar() {
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
    }

    @Override
    public void displayFavoriteMeals(List<Meal> meals) {
        adapter.submitList(meals);
        binding.rvFavMeals.setAdapter(adapter);

        if (meals.isEmpty()) {
            binding.loadingFavorites.setVisibility(View.VISIBLE);
            binding.emtyFavoritesTextView.setVisibility(View.VISIBLE);
        } else {
            binding.loadingFavorites.setVisibility(View.GONE);
            binding.emtyFavoritesTextView.setVisibility(View.GONE);
            binding.rvFavMeals.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showError(String msg) {
        if (getView() != null) {
            Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserId(String userId) {
        if (userId.isEmpty() && getActivity() != null) {
            new AlertDialog.Builder(requireActivity())
                    .setMessage("You need to sign up to add meals to your favorites, plan and more features.")
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    })
                    .setPositiveButton("Sign up", (dialog, which) -> {
                        onSignUpClickListener.onSignUp();
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Navigation.findNavController(binding.getRoot()).navigate(FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(meal));
    }

    @Override
    public void navigateToMealsList(String name, int type) {

    }

    @Override
    public void removeMealFromFavorites(Meal meal) {
        if (isConnected()) {
            presenter.deleteFromFirebase(meal);
        } else {
            Snackbar.make(requireView(), "No internet connection!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        return NetworkManager.isConnected(requireContext());
    }

}