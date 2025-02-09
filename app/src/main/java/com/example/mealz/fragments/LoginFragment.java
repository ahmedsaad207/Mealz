package com.example.mealz.fragments;

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
import com.example.mealz.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;
    FragmentLoginBinding binding;



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
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "login success", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment());
                    } else {
                        Toast.makeText(requireActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.txtSignUp.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()));

    }
}