package com.example.mealz.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealz.R;
import com.example.mealz.databinding.ActivityAuthBinding;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.authoptions.OnLoginSuccessListener;


public class AuthActivity extends AppCompatActivity implements OnLoginSuccessListener {

    ActivityAuthBinding binding;
    NavController navController;

    public boolean navigateToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setSupportActionBar(binding.toolbarAuth);
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentAuth)).getNavController();
        Intent intent = getIntent();
        if (intent != null) {
            navigateToSignUp = intent.getBooleanExtra(Constants.KEY_SIGN_UP_STATE, false);
        }

//        SharedPreferences preferences = getSharedPreferences("credential",MODE_PRIVATE);
//        if (!preferences.getString("userId","").isEmpty()) {
//            onLoginSuccess();
//        }
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