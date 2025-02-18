package com.example.mealz.view.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.view.AuthActivity;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean state = ((AuthActivity) requireActivity()).navigateToSignUp;

        if (state) {
            Navigation.findNavController(view).navigate(SplashFragmentDirections.actionSplashFragmentToAuthOptionsFragment());
            return;
        }

        new Handler().postDelayed(() -> {

            Navigation.findNavController(view).navigate(SplashFragmentDirections.actionSplashFragmentToAuthOptionsFragment());
        }, 3000);

        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
    }
}