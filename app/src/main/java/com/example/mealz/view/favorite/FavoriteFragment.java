package com.example.mealz.view.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mealz.R;
import com.example.mealz.databinding.FragmentFavoriteBinding;
import com.example.mealz.view.MealAdapter;

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

//        adapter = new MealAdapter(product -> {
//
//        });
//        adapter.submitList();
//        binding.rvFavMeals.setAdapter(adapter);
    }
}