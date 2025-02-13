package com.example.mealz.view.favorite;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealz.R;
import com.example.mealz.data.local.MealsDatabase;
import com.example.mealz.databinding.FragmentFavoriteBinding;
import com.example.mealz.model.Ingredient;
import com.example.mealz.view.MealAdapter;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteFragment extends Fragment {

    FragmentFavoriteBinding binding;
    MealAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Disposable disposable = MealsDatabase.getInstance(requireActivity()).getMealDao().getFavoriteMealsByUserId("ahmed")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {

                });


//        adapter = new MealAdapter(product -> {
//
//        });
//        adapter.submitList();
//        binding.rvFavMeals.setAdapter(adapter);
    }

    private void displayImage(Ingredient ingredient) {

        File dir = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ingredients");
        if (!dir.exists()) {
            Log.d("TAG", "folder not created");
            return;
        }

        String fileName = ingredient.getImageUrl().substring(ingredient.getImageUrl().lastIndexOf("/") + 1);
        File file = new File(dir, fileName);

        Glide.with(binding.imgTest.getContext())
                .load(file)
                .into(binding.imgTest);
    }
}