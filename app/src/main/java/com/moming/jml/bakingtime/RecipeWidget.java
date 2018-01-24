package com.moming.jml.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    public static final String WIDGET_RECIPE_ID = "widget_recipe_id";
    public static final String RECIPE_DETAIL_ACTION = "recipe_detail_action";

    public static String mRecipeId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,String id,String name) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Intent intent = new Intent(context,WidgetListAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.widget_lv_recipe_list,intent);
        views.setEmptyView(R.id.widget_lv_recipe_list,R.id.empty_view);
        views.setTextViewText(R.id.widget_tv_recipe_name,name);
        mRecipeId = id;

        Log.i("widget","mRecipeId:"+id);

        setPreBtnClickIntent(context,views,id);

       setNextBtnClickIntent(context,views,id);

        Intent GotoRecipeintent = new Intent(context,RecipeStepActivity.class);
        GotoRecipeintent.putExtra("recpice_id",id);
        GotoRecipeintent.putExtra("recpice_name",name);
        PendingIntent goToRecipeIntent = PendingIntent.getActivity(context,0,GotoRecipeintent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_tv_recipe_name,goToRecipeIntent);
        Intent GoToDetailintent;

        if (context.getResources().getBoolean(R.bool.isPad)){
            Log.i("widget","pad");
            GoToDetailintent= new Intent(context,RecipeStepActivity.class);
            GoToDetailintent.putExtra("recpice_name",name);
            GoToDetailintent.putExtra("recpice_id",id);
            PendingIntent goToDetailIntent = PendingIntent.getActivity(context,0,
                    GoToDetailintent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_lv_recipe_list,goToDetailIntent);
        }else {
            Log.i("widget","not pad");
            GoToDetailintent = new Intent(context,StepDetailActivity.class);
            PendingIntent goToDetailIntent = PendingIntent.getActivity(context,0,
                    GoToDetailintent,PendingIntent.FLAG_UPDATE_CURRENT);
            Log.i("widget","1");
            views.setPendingIntentTemplate(R.id.widget_lv_recipe_list,goToDetailIntent);
            Log.i("widget","2");
        }




        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void setPreBtnClickIntent(Context context,RemoteViews views, String id){
        Intent preIntent= new Intent(context,RecipeWidgetIntentService.class);
        preIntent.putExtra(WIDGET_RECIPE_ID,id);
        preIntent.setAction(RecipeWidgetIntentService.ACTION_PRE_RECIPE);
        PendingIntent prePendingIntent = PendingIntent.getService(context,0,preIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_bt_left,prePendingIntent);


    }

    public static void setNextBtnClickIntent(Context context,RemoteViews views, String id){
        Intent nextIntent= new Intent(context,RecipeWidgetIntentService.class);
        nextIntent.putExtra(WIDGET_RECIPE_ID,id);
        nextIntent.setAction(RecipeWidgetIntentService.ACTION_NEXT_RECIPE);
        PendingIntent prePendingIntent = PendingIntent.getService(context,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_bt_right,prePendingIntent);


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
      RecipeWidgetIntentService.startActionUpdate(context);




    }

    public  static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, @Nullable String id,@Nullable String name) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,id,name);
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
}

