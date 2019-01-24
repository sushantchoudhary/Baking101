package com.udacity.android.baking101.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.android.baking101.R;
import com.udacity.android.baking101.RecipeDetailActivity;
import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Ingredient;
import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.util.AppExecutors;

import java.util.List;


public class GridWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewRemoteViewFactory(this, intent);
    }

}

class GridViewRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public  static final String TAG = GridViewRemoteViewFactory.class.getSimpleName();

    private Context mContext;
    private String ingredients;
    private static AppDatabase mDB;
    private List<Ingredient> ingredientList;



    public GridViewRemoteViewFactory(Context context, Intent intent) {
        this.mContext = context;
        if (intent != null) {
            if (intent.hasExtra("ingredient")) {
                ingredients = intent.getStringExtra("ingredient");
            }
        }
        mDB = AppDatabase.getsInstance(context);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredientList =  mDB.ingredientDao().loadAllIngredient();

        StringBuilder sbl = new StringBuilder();
        for (Ingredient ingredient : ingredientList) {
            sbl.append("✔︎ " + ingredient.getIngredient() + "\n");
        }

        ingredients = sbl.toString();

        Log.d(TAG, "widget data has changed");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
        views.setViewVisibility(R.id.widget_recipe_image, View.GONE);
        views.setTextViewText(R.id.widget_recipe_ingredient, ingredients);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
