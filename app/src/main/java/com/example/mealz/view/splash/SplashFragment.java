package com.example.mealz.view.splash;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.presenter.splash.SplashPresenter;
import com.example.mealz.presenter.splash.SplashPresenterImpl;
import com.example.mealz.presenter.splash.SplashView;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.AuthActivity;
import com.example.mealz.view.authoptions.OnLoginSuccessListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.database.FirebaseDatabase;

public class SplashFragment extends Fragment implements SplashView {
    SplashPresenter presenter;

    OnLoginSuccessListener onLoginSuccessListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            onLoginSuccessListener = (OnLoginSuccessListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


        presenter = new SplashPresenterImpl(MealsRepositoryImpl.getInstance(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(
                        RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))),
                BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())
        ), this);

        presenter.getUserId();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onUserId(String userId) {
        if (userId.isEmpty()) {
            new Handler().postDelayed(() -> {
                if (getView() != null) {
                    Navigation.findNavController(requireView()).navigate(SplashFragmentDirections.actionSplashFragmentToAuthOptionsFragment());
                }
            }, 3000);
        } else {
            onLoginSuccessListener.onLoginSuccess();
        }
    }
}