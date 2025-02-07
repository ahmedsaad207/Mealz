package com.example.mealz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mealz.R;
import com.example.mealz.databinding.FragmentMealPlanBinding;

public class MealPlanFragment extends Fragment {
    FragmentMealPlanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_plan, container, false);
        return binding.getRoot();
    }
}