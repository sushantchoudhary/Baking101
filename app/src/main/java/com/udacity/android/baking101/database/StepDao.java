package com.udacity.android.baking101.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.android.baking101.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM step")
    LiveData<List<Step>> loadAllStep();

    @Query("DELETE FROM step")
    void deleteAllStep();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStep(Step step);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertStepList(List<Step> stepRecords);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(Step step);

    @Delete
    void deleteStep(Step step);

    @Query("SELECT * FROM step WHERE recipeId = :recipeId")
    LiveData<List<Step>> loadStepByRecipeId(int recipeId);

    @Query("SELECT * FROM step WHERE id = :id")
    LiveData<Step> loadStepByStepId(int id);

    @Query("SELECT COUNT(*) FROM step")
    int getStepsCount();
}
