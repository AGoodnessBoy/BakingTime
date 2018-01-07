package com.moming.jml.bakingtime.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.moming.jml.bakingtime.data.RecipeContract;

/**
 * Created by jml on 2018/1/2.
 */

public class RecipeSyncUntils {

    private static boolean sInitialized;

    synchronized  public static void initialize(@NonNull final Context context){
        if (sInitialized)return;

        sInitialized = true;
        Thread checkForEmpty = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
                        String[] projColumns = {RecipeContract.RecipeEntry._ID};
                        Cursor cursor = context.getContentResolver()
                                .query(uri,projColumns,null,null,null);

                        if (null == cursor||cursor.getCount() == 0){
                            startSyncNow(context);
                        }
                        if (cursor!=null){
                            cursor.close();
                        }

                    }
                }
        );
        checkForEmpty.start();
    }


    public  static void startSyncNow(@NonNull final Context context){
        Intent intent = new Intent(context,RecipeSyncIntentService.class);
        context.startService(intent);
    }


}
