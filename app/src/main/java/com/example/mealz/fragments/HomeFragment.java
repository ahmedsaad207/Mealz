package com.example.mealz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealz.MainActivity;
import com.example.mealz.R;
import com.example.mealz.databinding.FragmentHomeBinding;
import com.example.mealz.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FragmentHomeBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding.btnSignOut.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
            }
        });
    }
}