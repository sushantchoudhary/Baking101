package com.udacity.android.baking101.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.baking101.R;
import com.udacity.android.baking101.ViewRecipeStepActivity;
import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Ingredient;
import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.model.Step;

public class RecipeDetailFragment extends Fragment implements RecipeDetailAdpater.RecipeDetailsAdapterOnClickHandler {

    private RecipeDetailViewModel mViewModel;
    private RecyclerView mRecipeDetailRV;
    private TextView mIngredientsTV;
    private static Recipe recipe;
    private static int stepCount;

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private AppDatabase mDB;

    OnRecipeClickListener mCallback;
    // Interface to communicate with Activity
    public interface  OnRecipeClickListener {
        void onStepSelected(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeClickListener) context;

        } catch (ClassCastException e ) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener" );
        }
    }

    public static RecipeDetailFragment newInstance(Recipe recipeFromIntent) {
        recipe = recipeFromIntent;
        return new RecipeDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);


        mIngredientsTV = rootView.findViewById(R.id.ingredients_view);

        mRecipeDetailRV = rootView.findViewById(R.id.recyclerview_recipe_steps);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL
                ,false);
        mRecipeDetailRV.setLayoutManager(layoutManager);

        mRecipeDetailRV.setHasFixedSize(true);

        mDB = AppDatabase.getsInstance(getContext());


        return rootView;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadRecipeDetails() {

        StringBuilder sbl = new StringBuilder();
        for(Ingredient ingredient : recipe.getIngredients()) {
            sbl.append("✔︎ " + ingredient.getIngredient()+ "\n" + "\n");
        }
        mIngredientsTV.setText(sbl.toString());

        RecipeDetailAdpater recipeDetailAdpater = new RecipeDetailAdpater(this, recipe.getSteps());
        mRecipeDetailRV.setAdapter(recipeDetailAdpater);

        stepCount = recipeDetailAdpater.getItemCount();


        getActivity().setTitle(recipe.getName());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecipeDetailViewModelFactory factory = new RecipeDetailViewModelFactory(mDB, recipe.getId());

        mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
        mViewModel.getRecipe().observe( this, recipeFromModel -> {
            mDB.stepDao().insertStepList(recipeFromModel.getSteps());
        });
        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("recipe");
        }
        loadRecipeDetails();
    }

    @Override
    public void onClick(Step step) {
        if(getActivity().findViewById(R.id.recipe_detail_tablet_layout) != null) {
            mCallback.onStepSelected(step);
        } else {
            Class destinationClass = ViewRecipeStepActivity.class;
            Intent intentToStartStepActivity = new Intent(getContext(), destinationClass);
            intentToStartStepActivity.putExtra(Intent.EXTRA_TEXT, step);
            startActivity(intentToStartStepActivity );
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("recipe", recipe);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Saving recipe in bundle during orientation change");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


}
