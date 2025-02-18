package com.example.mealz.view.home;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mealz.utils.MealMapper.mapMealsToAreas;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealz.R;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.UserLocalDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSourceImpl;
import com.example.mealz.data.remote.MealsRemoteDataSourceImpl;
import com.example.mealz.databinding.FragmentHomeBinding;
import com.example.mealz.model.Area;
import com.example.mealz.model.Category;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.SearchItem;
import com.example.mealz.presenter.home.HomePresenterImpl;
import com.example.mealz.presenter.home.HomeView;
import com.example.mealz.utils.Constants;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnMealItemClickListener {
    static List<SearchItem> searchList;
    FirebaseAuth mAuth;
    FragmentHomeBinding binding;
    CategoryAdapter categoryAdapter;
    DailyInspirationAdapter dailyInspirationAdapter;
    AreaAdapter areaAdapter;
    HomePresenterImpl presenter;
    MealAdapter<SearchItem> searchAdapter;


    String key;
    Drawable drawableSearch, drawableArrow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBottomNavBar();

        if (binding.loadingHome != null) {
            binding.loadingHome.setVisibility(View.VISIBLE);
        }
        if (binding.searchEditText != null) {
            binding.searchEditText.setVisibility(View.INVISIBLE);
        }
        init();
        setupRV();
        requestData();
        handleSearch();
        /*
        binding.btnSignOut.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
            }
        });
        */
    }

    private void setupRV() {
        if (binding.rvMealsSearch != null) {
            binding.rvMealsSearch.setAdapter(searchAdapter);
        }
        binding.rvCategories.setHasFixedSize(true);
        binding.rvDailyInspiration.setHasFixedSize(true);
        binding.rvMealsSearch.setHasFixedSize(true);
        binding.rvAreas.setHasFixedSize(true);

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int areaItemWidth = (int) ((screenWidth - 64) / 4.5);
        areaAdapter.setItemWidth(areaItemWidth);
    }

    private void requestData() {
        presenter.getRandomMeal();
        presenter.getCategories();
        presenter.getAreas();
        presenter.getIngredients();
        presenter.getUsername();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        presenter = new HomePresenterImpl(MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireActivity()), MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(
                        RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))
                )), this);
        searchList = new ArrayList<>();
        searchAdapter = new MealAdapter<>(HomeFragment.this);
        drawableSearch = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_search);
        drawableArrow = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_arrow_back);
        areaAdapter = new AreaAdapter(this);
    }

    private void showBottomNavBar() {
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
    }

    private void handleSearch() {
        if (binding.searchEditText != null) {
            hideClearIconForSearchEditText();
            presenter.setList(searchList);
            handleTextWatcherForSearch();
            clearButtonListener();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clearButtonListener() {
        assert binding.searchEditText != null;
        binding.searchEditText.setOnTouchListener((v, event) -> {
            int arrow = binding.searchEditText.getCompoundDrawables()[0] != null ? binding.searchEditText.getCompoundDrawables()[0].getBounds().width() : 0;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() <= arrow) {
                    binding.searchEditText.setText("");

                    binding.searchEditText.performClick();
                    Toast.makeText(requireActivity(), "drawable clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

            return false;
        });
    }

    private void handleTextWatcherForSearch() {
        assert binding.searchEditText != null;
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setVisibilityForHomeContent(s);
                setVisibilityForClearButton(s);
                setVisibilityForSearchResult(s);
                key = s.toString();
                presenter.search(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setVisibilityForSearchResult(CharSequence s) {
        if (binding.rvMealsSearch != null) {
            binding.rvMealsSearch.setVisibility(s.toString().trim().isEmpty() ? View.INVISIBLE : View.VISIBLE);
//            ViewGroup.LayoutParams params = binding.nestedScrollView.getLayoutParams();
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            binding.nestedScrollView.setLayoutParams(params);
        }
    }

    private void setVisibilityForClearButton(CharSequence s) {
        assert binding.searchEditText != null;
        binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                s.toString().trim().isEmpty() ? null : drawableArrow,
                null, drawableSearch, null);
    }

    private void setVisibilityForHomeContent(CharSequence s) {
        if (binding.homeData != null) {
            binding.homeData.setVisibility(s.toString().trim().isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void hideClearIconForSearchEditText() {
        assert binding.searchEditText != null;
        binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null, drawableSearch, null);
    }

    @Override
    public void displayCategories(ArrayList<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            // TODO Empty List
            return;
        }
        categories.forEach(category -> searchList.add(new SearchItem(category.categoryName.toLowerCase(), category.categoryImage, 1)));

        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.submitList(categories);
        categoryAdapter.setOnItemClickListener(categoryName -> Navigation.findNavController(binding.rvCategories).navigate(HomeFragmentDirections.actionHomeFragmentToMealsListFragment(categoryName, Constants.ITEM_CATEGORY)));
    }

    @Override
    public void displayAreas(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            // TODO Empty List
            return;
        }

        List<Area> areas = mapMealsToAreas(meals, requireActivity());
        areas.forEach(meal -> searchList.add(new SearchItem(meal.getAreaName().toLowerCase(), meal.getImageResourceId(), 2)));
        binding.rvAreas.setAdapter(areaAdapter);
        areaAdapter.submitList(areas);
    }

    @Override
    public void displayDailyInspiration(List<Meal> meals) {
        if (dailyInspirationAdapter == null) {
            dailyInspirationAdapter = new DailyInspirationAdapter(this);
        }
        binding.rvDailyInspiration.setAdapter(dailyInspirationAdapter);
        dailyInspirationAdapter.submitList(meals);
        if (!meals.isEmpty() && binding.loadingHome != null && binding.homeData != null && binding.searchEditText != null) {
            binding.loadingHome.setVisibility(View.GONE);
            binding.homeData.setVisibility(View.VISIBLE);
            binding.searchEditText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onIngredientsListReady(List<Ingredient> ingredients) {
        ingredients.forEach(ingredient -> searchList.add(new SearchItem(ingredient.getName().toLowerCase(), ingredient.getImageUrl(), 3)));


    }

    @Override
    public void displaySearchItems(List<SearchItem> items) {
        if (binding.rvMealsSearch != null) {
            searchAdapter.submitList(items);
            binding.rvMealsSearch.setAdapter(searchAdapter);
        }
    }

    @Override
    public void displayUserName(String username) {
        if (getContext() != null && binding.helloUser != null && !username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, username));
        } else if (getContext() != null && binding.helloUser != null && username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, "Guest"));
        }
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Navigation.findNavController(binding.getRoot()).navigate(HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(meal));
    }

    @Override
    public void navigateToMealsList(String name, int type) {
        Navigation.findNavController(binding.getRoot()).navigate(HomeFragmentDirections.actionHomeFragmentToMealsListFragment(name, type));
    }

    @Override
    public void removeMealFromFavorites(Meal meal) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}