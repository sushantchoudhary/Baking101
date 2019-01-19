package com.udacity.android.baking101.ui.main;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.android.baking101.IngredientUpdateService;
import com.udacity.android.baking101.R;
import com.udacity.android.baking101.RecipeDetailActivity;
import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Ingredient;
import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.network.ApiService;
import com.udacity.android.baking101.network.RetroClient;
import com.udacity.android.baking101.util.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MainFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private MainViewModel mViewModel;
    private RecyclerView mRecipeRV;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private List<Recipe> mRecipeList;
    private RecipeAdapter recipeAdapter;
    private LinearLayoutManager layoutManager;
    private static AppDatabase mDB;


    public static final String TAG = MainFragment.class.getSimpleName();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

        mDB = AppDatabase.getsInstance(getContext());

        mRecipeRV = rootView.findViewById(R.id.recyclerview_recipe);

        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL
                , false);
        mRecipeRV.setLayoutManager(layoutManager);

        mRecipeRV.setHasFixedSize(true);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mViewModel.getRecipes().observe(this, recipes -> {
                recipeAdapter = new RecipeAdapter(MainFragment.this, recipes);
                mRecipeRV.setAdapter(recipeAdapter);
            });
        } else {
            loadRecipes();
        }
    }

    /***
     Retrofit call to fetch baking recipes and populate the UI
     */
    private void loadRecipes() {
        Call<List<Recipe>> callRecipes;
        showRecipeCardView();
        ApiService apiService = RetroClient.getApiService();
        callRecipes = apiService.getRecipes();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        callRecipes.enqueue(new RecipeListCallback());

    }

    private void showRecipeCardView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecipeRV.setVisibility(View.VISIBLE);
    }

    /**
     * This method will show an error message and hide the Recipe
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.network_error)
                .setMessage(R.string.network_error_msg)
                .setNegativeButton(R.string.error_dismiss_button, (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    @Override
    public void onClick(Recipe recipe) {

        StringBuilder sbl = new StringBuilder();
        for(Ingredient ingredient : recipe.getIngredients()) {
            sbl.append("✔︎ " + ingredient.getIngredient()+ "\n" );
        }

        IngredientUpdateService.startActionUpdateIngredient(getContext(), sbl.toString());

        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(getContext(), destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, recipe);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable recipeState = layoutManager.onSaveInstanceState();
        outState.putParcelable("RECIPE_STATE", recipeState);

    }


    private class RecipeListCallback implements Callback<List<Recipe>> {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            if (response.isSuccessful()) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (response.body() != null) {
                    mRecipeList = response.body();

                    recipeAdapter = new RecipeAdapter(MainFragment.this, mRecipeList);
                    mRecipeRV.setAdapter(recipeAdapter);

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        for (Recipe recipe : mRecipeList) {
                            mDB.recipeDao().insertRecipe(recipe);
                        }
                    });
                } else {
                    throw new HttpException(response);
                }
            }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecipeRV.setVisibility(View.INVISIBLE);
            showErrorMessage();
        }
    }
}
