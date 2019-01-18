package com.udacity.android.baking101.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.android.baking101.R;
import com.udacity.android.baking101.model.Step;

import java.util.List;

public class RecipeDetailAdpater extends  RecyclerView.Adapter<RecipeDetailAdpater.RecipeDetailAdpaterViewHolder> {

    private final RecipeDetailsAdapterOnClickHandler clickHandler;
    private List<Step> mRecipeSteps;

    public RecipeDetailAdpater(RecipeDetailsAdapterOnClickHandler clickHandler, List<Step> mRecipeSteps) {
        this.clickHandler = clickHandler;
        this.mRecipeSteps = mRecipeSteps;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailAdpaterViewHolder recipeDetailAdpaterViewHolder, int position) {
        final Step step = mRecipeSteps.get(position);
        recipeDetailAdpaterViewHolder.recipeStep.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
    }

    public class RecipeDetailAdpaterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final Button recipeStep;

        public RecipeDetailAdpaterViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeStep = itemView.findViewById(R.id.recipe_step_description);
            recipeStep.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Step step = mRecipeSteps.get(position);
            clickHandler.onClick(step);
        }
    }

    @NonNull
    @Override
    public RecipeDetailAdpaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        int layoutIdForRecipe = R.layout.recipe_step_description;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForRecipe, viewGroup, false);
        return new RecipeDetailAdpaterViewHolder(view);
    }


    public interface RecipeDetailsAdapterOnClickHandler {
        void onClick(Step step);
    }

    public void setRecipeSteps(List<Step> recipeStep) {
        mRecipeSteps = recipeStep;
        notifyDataSetChanged();
    }
}
