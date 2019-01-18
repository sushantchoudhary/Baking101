package com.udacity.android.baking101;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.android.baking101.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
