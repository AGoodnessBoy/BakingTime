package com.moming.jml.bakingtime.sync;

import android.content.ContentValues;
import android.content.Context;

import com.moming.jml.bakingtime.tool.JsonTools;
import com.moming.jml.bakingtime.tool.NetworkTools;
import com.moming.jml.bakingtime.data.RecipeContract;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jml on 2018/1/2.
 */

public class RecipeSyncTask {
    synchronized public static void syncRecipe(Context context){
        try {
            URL recipeUrl = NetworkTools.buildUrl();

            String  recipeJson = NetworkTools.getResponseFromHttpUrl(recipeUrl);

            ContentValues[] recipes = JsonTools.getRecipeFromJson(context,recipeJson);

            if (recipes!=null && recipes.length!=0){
                context.getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI,null,null);
                context.getContentResolver().bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI,recipes);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
