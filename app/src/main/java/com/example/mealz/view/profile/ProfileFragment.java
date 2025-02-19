package com.example.mealz.view.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mealz.databinding.FragmentProfileBinding;
import com.example.mealz.presenter.profile.ProfilePresenter;
import com.example.mealz.presenter.profile.ProfilePresenterImpl;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.NetworkManager;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements ProfileView {

    FragmentProfileBinding binding;

    ProfilePresenter presenter;

    OnLogoutListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLogoutListener) {
            listener = (OnLogoutListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ProfilePresenterImpl(new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(
                        RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))),
                BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())
        ), this);

        binding.btnLogout.setOnClickListener(v -> {
            if (isConnected()) {

                presenter.clearUserId();
                presenter.clearUsername();
                presenter.setRememberMe(false);
                listener.onLogout();
            } else {
                Snackbar.make(requireView(), "No internet connection!", Snackbar.LENGTH_SHORT).show();
            }
        });
        presenter.getUsername();
    }

    @Override
    public void displayUserName(String username) {
        if (username.isEmpty()) {
            new AlertDialog.Builder(requireActivity())
                    .setMessage("You need to sign up to add meals to your favorites, plan and more features.")
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigateUp();
                    })
                    .setPositiveButton("Sign up", (dialog, which) -> {
                        listener.onLogout();
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private boolean isConnected() {
        return NetworkManager.isConnected(requireContext());
    }
}