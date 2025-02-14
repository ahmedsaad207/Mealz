package com.example.mealz.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealz.R;
import com.example.mealz.databinding.ActivityAuthBinding;
import com.example.mealz.view.authoptions.OnLoginSuccessListener;


public class AuthActivity extends AppCompatActivity implements OnLoginSuccessListener {

    ActivityAuthBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setSupportActionBar(binding.toolbarAuth);
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentAuth)).getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

}