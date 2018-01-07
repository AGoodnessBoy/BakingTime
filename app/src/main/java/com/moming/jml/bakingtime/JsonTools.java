package com.moming.jml.bakingtime;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.moming.jml.bakingtime.data.RecipeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jml on 2018/1/1.
 */

public class JsonTools {
    final static String Tag = JsonTools.class.getSimpleName();

    final static String RECIPE_ID = "id";
    final static String RECIPE_NAME = "name";
    final static String RECIPE_INGREDIENTS = "ingredients";
    final static String RECIPE_QUANTITY = "quantity";
    final static String RECIPE_MEASURE = "measure";
    final static String RECIPE_INGREDIENT = "ingredient";
    final static String RECIPE_STEPS = "steps";
    final static String RECIPE_STEPS_ID = "id";
    final static String RECIPE_SHORTDESCRIPTION = "shortDescription";
    final static String RECIPE_DESCRIPTION = "description";
    final static String RECIPE_VIDEOURL = "videoURL";
    final static String RECIPE_THUMBNAIURL = "thumbnailURL";
    final static String RECIPE_SERIVINGS = "servings";
    final static String RECIPE_IMAGE = "image";

    public static ContentValues[] getRecipeFromJson
            (Context context,String recipeJson)throws JSONException{


        JSONArray recipeArray = new JSONArray(recipeJson);
        if (recipeArray!=null){
            int count = recipeArray.length();
            ContentValues[] contentValues = new ContentValues[count];
            for (int i=0;i<count ;i++){
                JSONObject recipeTemp = recipeArray.getJSONObject(i);
                ContentValues cv = new ContentValues();
                Log.v(Tag,recipeTemp.getString(RECIPE_ID));
                Log.v(Tag,recipeTemp.getString(RECIPE_NAME));
                Log.v(Tag,recipeTemp.getString(RECIPE_STEPS));

                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,recipeTemp.getString(RECIPE_ID));
                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipeTemp.getString(RECIPE_NAME));
                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS,recipeTemp.getString(RECIPE_INGREDIENTS));
                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS,recipeTemp.getString(RECIPE_STEPS));
                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERIVINGS,recipeTemp.getString(RECIPE_SERIVINGS));
                cv.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE,recipeTemp.getString(RECIPE_IMAGE));
                contentValues[i] = cv;
            }
            return contentValues;
        }else {
            return null;
        }
    }

}
