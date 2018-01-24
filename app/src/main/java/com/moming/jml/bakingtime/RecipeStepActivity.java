package com.moming.jml.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.moming.jml.bakingtime.fragment.RecipeStepFragment;
import com.moming.jml.bakingtime.fragment.StepDetailFragment;
import com.moming.jml.bakingtime.tool.ImageTools;

public class RecipeStepActivity extends AppCompatActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        if (getResources().getBoolean(R.bool.isPad)){
            mImageView = findViewById(R.id.image_recpice_bg);
            Intent intent = getIntent();
            String mRecipeId = intent.getStringExtra("recpice_id");
            String mRecipeName = intent.getStringExtra("recpice_name");
            Log.i("tag",mRecipeId);
            int mPostion = intent.getIntExtra("step_postion",0);
            mImageView.setImageResource(ImageTools.getImageSourceIdByName(mRecipeName));
            mImageView.setContentDescription(mRecipeName);
            //mNameTextView.setText(name);

            RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_step_fragment);
            recipeStepFragment.mStepPostion = mPostion;
            this.setTitle(mRecipeName);


            StepDetailFragment fragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(
                    R.id.step_detail_fragment);
            Log.i("tag",mRecipeId);
            Log.i("tag","p:"+Integer.toString(mPostion));


            fragment.inputData(mRecipeId,mPostion);
        }

    }
}
