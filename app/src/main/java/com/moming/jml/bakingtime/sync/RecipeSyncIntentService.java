package com.moming.jml.bakingtime.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Moming-Desgin on 2018/1/7.
 */

public class RecipeSyncIntentService extends IntentService {


    public RecipeSyncIntentService() {
        super("RecipeSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        RecipeSyncTask.syncRecipe(this);
    }
}
