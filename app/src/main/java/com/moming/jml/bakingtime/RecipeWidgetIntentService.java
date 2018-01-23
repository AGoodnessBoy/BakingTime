package com.moming.jml.bakingtime;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class RecipeWidgetIntentService extends IntentService {

    private static final String ACTION_NEXT_RECIPE = "com.moming.jml.bakingtime.action.nextRecipe";
    private static final String ACTION_PRE_RECIPE = "com.moming.jml.bakingtime.action.preRecipe";
    private static final String ACTION_DETAIL_STEP = "com.moming.jml.bakingtime.action.detailStep";
    private static final String ACTION_STEP = "com.moming.jml.bakingtime.action.step";

    private static final String RECIPE_ID= "recipe_id";
    private static final String RECIPE_POSTION= "recipe_postion";



    public RecipeWidgetIntentService() {
        super("RecipeWidgetIntentService");
    }


    public static void startActionNextRecipe(Context context,String id, int potion) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_NEXT_RECIPE);
        intent.putExtra(RECIPE_ID,id);
        intent.putExtra(RECIPE_POSTION,potion);
        context.startService(intent);
    }

    public static void startActionPreRecipe(Context context,String id, int potion) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_PRE_RECIPE);
        intent.putExtra(RECIPE_ID,id);
        intent.putExtra(RECIPE_POSTION,potion);

        context.startService(intent);
    }

    public static void startActionDetailStep(Context context,String id, int potion) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_DETAIL_STEP);
        intent.putExtra(RECIPE_ID,id);
        intent.putExtra(RECIPE_POSTION,potion);
        context.startService(intent);
    }

    public static void startActionStep(Context context,String id) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_STEP);
        intent.putExtra(RECIPE_ID,id);
        context.startService(intent);
    }





    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NEXT_RECIPE.equals(action)) {
                final String id = intent.getStringExtra(RECIPE_ID);
                final int postion = intent.getIntExtra(RECIPE_POSTION,0);
                handleActionNextRecipe(id,postion);
            } else if (ACTION_PRE_RECIPE.equals(action)) {
                final String id = intent.getStringExtra(RECIPE_ID);
                final int postion = intent.getIntExtra(RECIPE_POSTION,0);
                handleActionPreRecipe(id,postion);
            }else if (ACTION_DETAIL_STEP.equals(action)){
                final String id = intent.getStringExtra(RECIPE_ID);
                final int postion = intent.getIntExtra(RECIPE_POSTION,0);
                handleActionDetailStep(id,postion);
            }else if (ACTION_STEP.equals(action)){
                final String id = intent.getStringExtra(RECIPE_ID);
                handleActionStep(id);
            }
        }
    }

    private void handleActionNextRecipe(String id, int postion) {

        startActionNextRecipe(this,id,postion);
    }

    private void handleActionPreRecipe(String id, int postion) {

        startActionNextRecipe(this,id,postion);
    }

    private void handleActionDetailStep(String id,int postion) {

        startActionDetailStep(this,id,postion);
    }

    private void handleActionStep(String id) {

        startActionStep(this,id);
    }
}
