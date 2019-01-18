package com.udacity.android.baking101.network;

import com.udacity.android.baking101.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

        @GET("baking.json")
        Call<List<Recipe>> getRecipes();
}
