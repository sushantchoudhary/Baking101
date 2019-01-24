package com.udacity.android.baking101.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import com.udacity.android.baking101.MainActivity;
import com.udacity.android.baking101.R;
import com.udacity.android.baking101.RecipeDetailActivity;
import com.udacity.android.baking101.model.Ingredient;
import com.udacity.android.baking101.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeUpdateWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe,
                                int appWidgetId) {
        if (recipe == null) return;
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        StringBuilder sbl = new StringBuilder();

        if (recipe != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                sbl.append("✔︎ " + ingredient.getIngredient() + "\n");
            }
        }

        if (width < 300 ) {
                rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                rv.setOnClickPendingIntent(R.id.widget_recipe_ingredient, pendingIntent);
                rv.setViewVisibility(R.id.widget_recipe_image, GONE);
                rv.setTextViewText(R.id.recipe_name, recipe.getName());
                rv.setTextViewText(R.id.widget_recipe_ingredient, sbl.toString());
            } else {
                    rv = getRecipeGridRemoteView(context, recipe);
            }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientUpdateService.startActionUpdateRecipe(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        IngredientUpdateService.startActionUpdateRecipe(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }


    private static RemoteViews getRecipeGridRemoteView(Context context, Recipe recipe) {
        //
        StringBuilder sbl = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            sbl.append("✔︎ " + ingredient.getIngredient() + "\n");
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra("ingredient", sbl.toString());
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.putExtra(Intent.EXTRA_TEXT, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }
}

