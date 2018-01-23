package com.moming.jml.bakingtime;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.moming.jml.bakingtime.data.RecipeContract;
import com.moming.jml.bakingtime.data.StepEntry;
import com.moming.jml.bakingtime.tool.JsonTools;

import org.json.JSONException;


public class RecipeWidgetIntentService extends IntentService {

    public static final String ACTION_NEXT_RECIPE = "com.moming.jml.bakingtime.action.nextRecipe";
    public static final String ACTION_PRE_RECIPE = "com.moming.jml.bakingtime.action.preRecipe";
    private static final String ACTION_DETAIL_STEP = "com.moming.jml.bakingtime.action.detailStep";
    private static final String ACTION_STEP = "com.moming.jml.bakingtime.action.step";
    private static final String ACTION_UPDATE = "com.moming.jml.bakingtime.action.update";

    private static final String RECIPE_ID= "recipe_id";
    private static final String RECIPE_POSTION= "recipe_postion";

    private String mRecipeId;
    private String mRecipeName;



    public RecipeWidgetIntentService() {
        super("RecipeWidgetIntentService");
    }


    public static void startActionUpdate(Context context){
        Intent intent = new Intent(context,RecipeWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE);
        context.startService(intent);
    }


    public static void startActionNextRecipe(Context context) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_NEXT_RECIPE);
        context.startService(intent);
    }

    public static void startActionPreRecipe(Context context) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_PRE_RECIPE);
        context.startService(intent);
    }

    public static void startActionDetailStep(Context context,String id, int potion) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_DETAIL_STEP);
        intent.putExtra(RecipeWidget.WIDGET_RECIPE_ID,id);
        intent.putExtra(RECIPE_POSTION,potion);
        context.startService(intent);
    }

    public static void startActionStep(Context context,String id) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_STEP);
        intent.putExtra(RecipeWidget.WIDGET_RECIPE_ID,id);
        context.startService(intent);
    }





    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NEXT_RECIPE.equals(action)) {
                mRecipeId = intent.getStringExtra(RecipeWidget.WIDGET_RECIPE_ID);
                handleActionNextRecipe();
            } else if (ACTION_PRE_RECIPE.equals(action)) {
                mRecipeId = intent.getStringExtra(RecipeWidget.WIDGET_RECIPE_ID);
                handleActionPreRecipe();
            }else if (ACTION_DETAIL_STEP.equals(action)){
                final String id = intent.getStringExtra(RecipeWidget.WIDGET_RECIPE_ID);
                final int postion = intent.getIntExtra(RECIPE_POSTION,0);
                handleActionDetailStep(id,postion);
            }else if (ACTION_STEP.equals(action)){
                final String id = intent.getStringExtra(RecipeWidget.WIDGET_RECIPE_ID);
                handleActionStep(id);
            }else if (ACTION_UPDATE.equals(action)){
                handleActionUpdate();
            }
        }
    }

    private void handleActionUpdate() {
        Log.i("widget","handleActionUpdate");

        String recipeId="";
        String name="";


            ContentResolver cr = this.getContentResolver();
            Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
            Cursor item =cr.query(uri,null,null,null,null);
            if (item!=null) {
                item.moveToFirst();
                recipeId = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
                Log.i("widget","handleActionUpdate"+recipeId);
                name = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
                item.close();
            }



        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv_recipe_list);
        //Now update all widgets
        RecipeWidget.updateRecipeWidget(this,appWidgetManager,appWidgetIds,recipeId,name);
    }

    private void handleActionNextRecipe() {

        int idNow = 0;
        try {
            idNow = Integer.valueOf(mRecipeId).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int cursorIdNow = idNow-1;
        int cursorIdNext = cursorIdNow+1;
        String recipeId="";
        String name="";
        ContentResolver cr = this.getContentResolver();
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        Cursor item =cr.query(uri,null,null,null,null);
        if (item!=null) {
           if (cursorIdNext>=item.getCount()){
                item.moveToFirst();
           }else {
               item.moveToPosition(cursorIdNext);
           }

            recipeId = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
            name = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
            item.close();
        }


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv_recipe_list);
        //Now update all widgets
        RecipeWidget.updateRecipeWidget(this,appWidgetManager,appWidgetIds,recipeId,name);
    }

    private void handleActionPreRecipe() {
        Log.i("widget","handleActionPreRecipe"+mRecipeId);
        int idNow = 0;
        try {
           idNow = Integer.valueOf(mRecipeId).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int cursorIdNow = idNow-1;
        int cursorIdPre = cursorIdNow-1;
        String recipeId="";
        String name="";
        ContentResolver cr = this.getContentResolver();
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        Cursor item =cr.query(uri,null,null,null,null);
        if (item!=null) {
            if (cursorIdPre<0){
                item.moveToLast();
            }else {
                item.moveToPosition(cursorIdPre);
            }

            recipeId = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
            name = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
            item.close();
        }


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lv_recipe_list);
        //Now update all widgets
        RecipeWidget.updateRecipeWidget(this,appWidgetManager,appWidgetIds,recipeId,name);
    }

    private void handleActionDetailStep(String id,int postion) {

        startActionDetailStep(this,id,postion);
    }

    private void handleActionStep(String id) {

        startActionStep(this,id);
    }
}
