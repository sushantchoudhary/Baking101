package com.udacity.android.baking101;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class IngredientUpdateService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGET = "com.udacity.android.baking101.action.update_recipe";
    public static final String ACTION_UPDATE_INGREDIENT = "com.udacity.android.baking101.action.update_ingredient";

    private static final String EXTRA_RECIPE = "com.udacity.android.baking101.extra.RECIPE_INGREDIENT" ;
    private static String ingredientFromSource;

    public IngredientUpdateService( ) {
        super("IngredientUpdateService");
    }

    public static void startActionUpdateRecipe(Context context) {
        Intent intent = new Intent(context, IngredientUpdateService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }

    public static void startActionUpdateIngredient(Context context, String ingredient) {
        Intent intent = new Intent(context, IngredientUpdateService.class);
        intent.putExtra(EXTRA_RECIPE, ingredient);
        intent.setAction(ACTION_UPDATE_INGREDIENT);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_INGREDIENT.equals(action) ) {
                ingredientFromSource = intent.getStringExtra(EXTRA_RECIPE);
                handleActionUpdateIngredient(ingredientFromSource);
            } else   if(ACTION_UPDATE_RECIPE_WIDGET.equals(action) ) {
                handleActionUpdateRecipe();
            }
        }
    }

    private void handleActionUpdateRecipe() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeUpdateWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_layout);

        RecipeUpdateWidgetProvider.updateRecipeWidgets(this, appWidgetManager, ingredientFromSource, appWidgetIds );
    }

    private void handleActionUpdateIngredient(String ingredient) {
        ingredientFromSource = ingredient;
        startActionUpdateRecipe(this);
    }


}
