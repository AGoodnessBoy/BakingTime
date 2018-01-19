package com.moming.jml.bakingtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.moming.jml.bakingtime.fragment.StepDetailFragment;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Intent intent = getIntent();
        int postion = intent.getIntExtra("step_postion",0);
        String id  = intent.getStringExtra("recipe_id");
        StepDetailFragment fragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.step_detail_fragment);
        fragment.inputData(id,postion);
    }
}
