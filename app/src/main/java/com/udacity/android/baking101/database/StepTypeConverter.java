package com.udacity.android.baking101.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.android.baking101.model.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class StepTypeConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<Step> stringToStepList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Step>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stepListToString(List<Step> stepList) {
        return gson.toJson(stepList);
    }
}
