package com.example.mealz.view.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.databinding.FragmentLoginBinding;
import com.example.mealz.view.authoptions.OnLoginSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    FragmentLoginBinding binding;

    OnLoginSuccessListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            listener = (OnLoginSuccessListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getEditText().getText().toString().trim();
            String password = binding.edtPassword.getEditText().getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireActivity(), "login success", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    navigateToHome();
                } else {
                    Toast.makeText(requireActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("Login");
        }

        binding.txtGuest.setOnClickListener(v -> navigateToHome());
        binding.txtSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()));
    }

    private void navigateToHome() {
        if (listener != null) {
            listener.onLoginSuccess();
        }
    }
}