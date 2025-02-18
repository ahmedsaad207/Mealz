package com.example.mealz.view;

import static com.example.mealz.utils.Constants.KEY_SIGN_UP_STATE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mealz.R;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.home.OnGuestModeListener;
import com.example.mealz.view.profile.OnLogoutListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements OnLogoutListener,OnSignUpClickListener {
    Toolbar toolbar;
    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentHome);

        if (navHostFragment == null) {
            return;
        }

        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) {
                NavOptions options = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build();
                navController.navigate(R.id.homeFragment, null, options);
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController);
        });
        navController.addOnDestinationChangedListener((nc, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.mealsListFragment || navDestination.getId() == R.id.favoriteFragment) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
        intent.putExtra(Constants.KEY_SIGN_UP_STATE, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignUp() {
        Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
        intent.putExtra(Constants.KEY_SIGN_UP_STATE, true);
        startActivity(intent);
        finish();
    }
}