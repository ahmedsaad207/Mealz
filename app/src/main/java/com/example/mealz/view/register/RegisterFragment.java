package com.example.mealz.view.register;

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
import com.example.mealz.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setupToolbar();
        setListeners();
    }

    private void setListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.edtEmail.getEditText() != null ? binding.edtEmail.getEditText().getText().toString().trim() : "";
            String username = binding.edtUsername.getEditText() != null ? binding.edtUsername.getEditText().getText().toString().trim() : "";
            String password = binding.edtPassword.getEditText() != null ? binding.edtPassword.getEditText().getText().toString().trim() : "";
            String confirmPassword = binding.edtConfirmPassword.getEditText() != null ? binding.edtConfirmPassword.getEditText().getText().toString().trim() : "";
            if (validateUserInput(email, username, password, confirmPassword)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUsername(username);
                        mAuth.signOut();
                        navigateToLogin(v);
                    } else {
                        handleError(task);
                    }
                });
            }
        });
        binding.txtLogin.setOnClickListener(RegisterFragment::navigateToLogin);
    }

    private void handleError(Task<AuthResult> task) {
        Exception e = task.getException();
        if (e != null) {
            if (e instanceof FirebaseAuthUserCollisionException) {
                binding.edtEmail.setError("Email already in use");
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Snackbar.make(binding.getRoot(), "Invalid email format", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.getRoot(), "Registration failed: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private static void navigateToLogin(View v) {
        Navigation.findNavController(v).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
    }

    private void updateUsername(String username) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());
        }
    }

    private boolean validateUserInput(String email, String username, String password, String confirmPassword) {

        binding.edtEmail.setErrorEnabled(false);
        binding.edtUsername.setErrorEnabled(false);
        binding.edtConfirmPassword.setErrorEnabled(false);
        binding.edtPassword.setErrorEnabled(false);

        if (email.isEmpty()) {
            binding.edtEmail.setError("Email is required");
            return false;
        }

//        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
//                "\\@" +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
//                "(" +
//                "\\." +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
//                ")+"

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Invalid email format");
            return false;
        }

        if (username.isEmpty()) {
            binding.edtUsername.setError("Username is required");
            return false;
        }

        if (username.length() < 3) {
            binding.edtUsername.setError("Username should be at least 3 characters");
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

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmPassword.setError("Please confirm your password");
            return false;
        }
        if (!confirmPassword.equals(password)) {
            binding.edtConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void setupToolbar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("Register");
        }
    }


}