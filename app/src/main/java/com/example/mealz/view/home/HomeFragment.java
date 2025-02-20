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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.mealz.databinding.FragmentHomeBinding;
import com.example.mealz.model.Area;
import com.example.mealz.model.Category;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.SearchItem;
import com.example.mealz.presenter.home.HomePresenter;
import com.example.mealz.presenter.home.HomePresenterImpl;
import com.example.mealz.presenter.home.HomeView;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.NetworkManager;
import com.example.mealz.view.MealAdapter;
import com.example.mealz.view.OnMealItemClickListener;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnMealItemClickListener, NetworkManager.NetworkListener {
    static List<SearchItem> searchList;
    FirebaseAuth mAuth;
    FragmentHomeBinding binding;
    CategoryAdapter categoryAdapter;
    DailyInspirationAdapter dailyInspirationAdapter;
    AreaAdapter areaAdapter;
    HomePresenter presenter;
    MealAdapter<SearchItem> searchAdapter;
    NetworkManager networkManager;


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
        loading();
        init();
        setupRV();
        handleSearch();
    }

    @Override
    public void onStart() {
        super.onStart();
        handleConnection();
    }

    private void setupRV() {
        binding.rvMealsSearch.setAdapter(searchAdapter);
        binding.rvCategories.setHasFixedSize(true);
        binding.rvDailyInspiration.setHasFixedSize(true);
        binding.rvMealsSearch.setHasFixedSize(true);
        binding.rvAreas.setHasFixedSize(true);

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int areaItemWidth = (int) ((screenWidth - 64) / 4.5);
        areaAdapter.setItemWidth(areaItemWidth);
    }

    private void requestData() {
        loading();
        presenter.getUserId();
        presenter.getRandomMeal();
        presenter.getCategories();
        presenter.getAreas();
        presenter.getIngredients();
        presenter.getUsername();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        presenter = new HomePresenterImpl(MealsRepositoryImpl.getInstance(
                MealsRemoteDataSourceImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireActivity()),
                MealFileDataSourceImpl.getInstance(requireActivity()),
                UserLocalDataSourceImpl.getInstance(RxSharedPreferences.create(requireActivity().getSharedPreferences(Constants.SP_CREDENTIAL, MODE_PRIVATE))),
                BackUpRemoteDataSourceImpl.getInstance(FirebaseDatabase.getInstance())), this);
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
        hideClearIconForSearchEditText();
        presenter.setList(searchList);
        handleTextWatcherForSearch();
        clearButtonListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clearButtonListener() {
        binding.searchEditText.setOnTouchListener((v, event) -> {
            int arrow = binding.searchEditText.getCompoundDrawables()[0] != null ? binding.searchEditText.getCompoundDrawables()[0].getBounds().width() : 0;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() <= arrow) {
                    binding.searchEditText.setText("");

                    binding.searchEditText.performClick();
                    return true;
                }
            }

            return false;
        });
    }

    private void handleTextWatcherForSearch() {
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
        binding.rvMealsSearch.setVisibility(s.toString().trim().isEmpty() ? View.INVISIBLE : View.VISIBLE);
    }

    private void setVisibilityForClearButton(CharSequence s) {
        binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                s.toString().trim().isEmpty() ? null : drawableArrow,
                null, drawableSearch, null);
    }

    private void setVisibilityForHomeContent(CharSequence s) {
        binding.contentHome.setVisibility(s.toString().trim().isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void hideClearIconForSearchEditText() {
        binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null, drawableSearch, null);
    }

    @Override
    public void displayCategories(ArrayList<Category> categories) {
        if (categories == null || categories.isEmpty()) {
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
        if (!meals.isEmpty()) {
            binding.loadingHome.setVisibility(View.GONE);
            binding.contentHome.setVisibility(View.VISIBLE);
            binding.searchEditText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onIngredientsListReady(List<Ingredient> ingredients) {
        ingredients.forEach(ingredient -> searchList.add(new SearchItem(ingredient.getName().toLowerCase(), ingredient.getImageUrl(), 3)));


    }

    @Override
    public void displaySearchItems(List<SearchItem> items) {
        searchAdapter.submitList(items);
        binding.rvMealsSearch.setAdapter(searchAdapter);
    }

    @Override
    public void displayUserName(String username) {
        if (getContext() != null && !username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, username));
        } else if (getContext() != null && username.isEmpty()) {
            binding.helloUser.setText(getContext().getString(R.string.hello_name, "Guest"));
        }
    }

    @Override
    public void onHideLoading(String error) {
        binding.loadingHome.setVisibility(View.GONE);
        binding.contentHome.setVisibility(View.GONE);
        binding.searchEditText.setVisibility(View.GONE);
        binding.errorTextView.setText(error);
        binding.errorTextView.setVisibility(View.VISIBLE);
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

    private void handleConnection() {
        networkManager = new NetworkManager(requireActivity(), this);
    }

    @SuppressLint("SetTextI18n")
    private void offlineMode() {
        binding.contentHome.setVisibility(View.GONE);
        binding.searchEditText.setVisibility(View.GONE);
        binding.loadingNoConn.setVisibility(View.VISIBLE);
        binding.loadingHome.setVisibility(View.GONE);
        binding.errorTextView.setVisibility(View.VISIBLE);
        binding.errorTextView.setText("No internet connection!");
    }

    private void onlineMode() {
        binding.errorTextView.setVisibility(View.GONE);
        binding.loadingNoConn.setVisibility(View.GONE);
        requestData();
    }

    private void loading() {
        binding.loadingHome.setVisibility(View.VISIBLE);
        binding.searchEditText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        networkManager.registerNetworkCallback();
    }

    @Override
    public void onPause() {
        super.onPause();
        networkManager.unregisterNetworkCallback();
    }

    @Override
    public void onNetworkAvailable() {
        requireActivity().runOnUiThread(this::onlineMode);
    }

    @Override
    public void onNetworkLost() {
        requireActivity().runOnUiThread(this::offlineMode);
    }
}