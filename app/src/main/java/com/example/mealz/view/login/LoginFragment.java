package com.example.mealz.view.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.mealz.data.preferences.UserLocalDataSourceImpl;
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentLoginBinding;
import com.example.mealz.presenter.login.LoginPresenter;
import com.example.mealz.presenter.login.LoginPresenterImpl;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.authoptions.OnLoginSuccessListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    FragmentLoginBinding binding;

    OnLoginSuccessListener listener;

    LoginPresenter presenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            listener = (OnLoginSuccessListener) context;
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        init();
        setListeners();
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getEditText() != null ? binding.edtEmail.getEditText().getText().toString().trim() : "";
            String password = binding.edtPassword.getEditText() != null ? binding.edtPassword.getEditText().getText().toString().trim() : "";

            if (validateUserInput(email, password)) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveLogin();
                        mAuth.signOut();
                        navigateToHome();
                    } else {
                        handleError(task);
                    }
                });
            }
        });
        binding.txtGuest.setOnClickListener(v -> navigateToHome());
        binding.txtSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()));
    }

    private void handleError(Task<AuthResult> task) {
        Exception e = task.getException();
        if (e != null) {
            if (e instanceof FirebaseAuthInvalidUserException) {
                Snackbar.make(binding.getRoot(), "User does not exist", Snackbar.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Snackbar.make(binding.getRoot(), "Login Failed. Incorrect email or password", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.getRoot(), "Login Failed: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

        }
    }

    private void saveLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            presenter.saveCredential(user.getUid(), user.getDisplayName());
        }
        presenter.setRememberMe(binding.checkboxRememberMe.isChecked());
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        presenter = new LoginPresenterImpl(new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))),
                BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())
        ));
    }

    private void setupToolbar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("Login");
        }
    }

    private boolean validateUserInput(String email, String password) {

        binding.edtEmail.setErrorEnabled(false);
        binding.edtPassword.setErrorEnabled(false);

        if (email.isEmpty()) {
            binding.edtEmail.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            binding.edtPassword.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Password should be at least 6 characters");
            return false;
        }

        if (!password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) {
            binding.edtPassword.setError("Password should contain at least one letter and one number");
            return false;
        }
        return true;
    }

    private void navigateToHome() {
        if (listener != null) {
            listener.onLoginSuccess();
        }
    }
}