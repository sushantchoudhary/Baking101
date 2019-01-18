package com.udacity.android.baking101.ui.main;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.android.baking101.R;
import com.udacity.android.baking101.model.Recipe;

import java.util.HashMap;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private static final String BASE_IMAGE_URL = "https://image.bake.org/t/p/w500";


    private final RecipeAdapterOnClickHandler clickHandler;
    private List<Recipe> mRecipeList;

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler, List<Recipe> recipes) {
        this.clickHandler = clickHandler;
        this.mRecipeList = recipes;
    }


    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForRecipe = R.layout.main_recipe_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForRecipe, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        final Recipe recipe = mRecipeList.get(position);



        String recipeUrl = BASE_IMAGE_URL + recipe.getImage();

        if (URLUtil.isValidUrl(recipeUrl)) {

            HashMap<String, Integer> bakeMap = new HashMap<>();
            bakeMap.put("Nutella Pie", R.drawable.nutella_pie);
            bakeMap.put("Brownies", R.drawable.brownie);
            bakeMap.put("Yellow Cake", R.drawable.yellowcake);
            bakeMap.put("Cheesecake", R.drawable.cheesecake);

            bakeMap.entrySet().stream()
                    .filter(e -> recipe.getName().equals(e.getKey()))
                    .forEach(e -> recipeAdapterViewHolder.recipeBackground.setImageResource(e.getValue()));
            recipeAdapterViewHolder.cakeName.setText(recipe.getName());


//            Context context = recipeAdapterViewHolder.recipeBackground.getContext();
//            Picasso.with(context)
//                    .load(recipeUrl)
//                    .placeholder(R.drawable.nutella_pie)
//                    .error(R.drawable.nutella_pie)
//                    .into(recipeAdapterViewHolder.recipeBackground);
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipeList == null) return 0;
        return mRecipeList.size();
    }

    public void setRecipeData(List<Recipe> recipes) {
        mRecipeList = recipes;
        notifyDataSetChanged();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView recipeBackground;
        public final TextView cakeName;

        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeName = itemView.findViewById(R.id.cake_name);
            recipeBackground = itemView.findViewById(R.id.recipe_background);
            recipeBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe recipe = mRecipeList.get(position);
            clickHandler.onClick(recipe);

        }
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }
}
