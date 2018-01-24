package com.moming.jml.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.moming.jml.bakingtime.data.StepEntry;
import com.moming.jml.bakingtime.tool.JsonTools;

import org.json.JSONException;

public class WidgetListAdapterService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}

class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private int mAppWidgetId;
    private StepEntry[] mStepEntries;


    public WidgetListRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        
    }

    @Override
    public void onDataSetChanged() {

        mStepEntries = null;
        try {
            mStepEntries = JsonTools.getStepById(mContext,RecipeWidget.mRecipeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        mStepEntries = null;

    }

    @Override
    public int getCount() {
        if (mStepEntries!=null){
            return mStepEntries.length;
        }else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(
                mContext.getPackageName(),R.layout.widget_list_item);

        remoteViews.setTextViewText(R.id.widget_tv_step_name,
                mStepEntries[position].getSetp_id()+" . "+mStepEntries[position].getSetp_shortDesc()
                );

        Bundle bundle = new Bundle();
        bundle.putInt("step_postion",position);
        bundle.putString("recipe_id",RecipeWidget.mRecipeId);
        Log.i("widget_bundle",RecipeWidget.mRecipeId+"  " +Integer.toString(position));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.widget_tv_step_name,fillInIntent);


        return remoteViews;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
