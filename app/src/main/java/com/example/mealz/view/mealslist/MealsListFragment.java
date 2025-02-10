package com.example.mealz.view.mealslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.api.MealzApiService;
import com.example.mealz.data.api.MealzRetrofit;
import com.example.mealz.databinding.FragmentMealsListBinding;
import com.example.mealz.model.MealzResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealsListFragment extends Fragment {
    FragmentMealsListBinding binding;
    MealAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MealzApiService service = MealzRetrofit.getService();

        MealsListFragmentArgs args = MealsListFragmentArgs.fromBundle(getArguments());
        String name = args.getMealName();
        boolean isCategory = args.getIsCategory();

        if (isCategory) {
            service.getMealsByCategory(name).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MealzResponse> call, @NonNull Response<MealzResponse> response) {
                    if (response.body() != null && !response.body().meals.isEmpty()) {
                        adapter = new MealAdapter(id -> {
                            Navigation.findNavController(view).navigate(MealsListFragmentDirections.actionMealsListFragmentToMealDetailsFragment(id));
                        });
                        adapter.submitList(response.body().meals);
                        binding.rvMeals.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealzResponse> call, @NonNull Throwable t) {
                    Toast.makeText(requireActivity(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            service.getMealsByArea(name).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MealzResponse> call, @NonNull Response<MealzResponse> response) {
                    if (response.body() != null && !response.body().meals.isEmpty()) {
                        adapter = new MealAdapter(id -> {
                            Navigation.findNavController(view).navigate(MealsListFragmentDirections.actionMealsListFragmentToMealDetailsFragment(id));
                        });
                        adapter.submitList(response.body().meals);
                        binding.rvMeals.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealzResponse> call, @NonNull Throwable t) {
                    Toast.makeText(requireActivity(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }




    }
}