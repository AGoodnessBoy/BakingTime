package com.moming.jml.bakingtime.tool;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.moming.jml.bakingtime.data.IngredientEntry;
import com.moming.jml.bakingtime.data.RecipeContract;
import com.moming.jml.bakingtime.data.StepEntry;

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

    @Nullable
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

    @Nullable
    public static StepEntry[] getStepById(Context context, String id)throws JSONException{
        ContentResolver cr = context.getContentResolver();
        Uri uri = RecipeContract.RecipeEntry.buildRecipeUriWithId(id);
        String[] projection = {RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS};
        Cursor item =cr.query(uri,projection,null,null,null);
        if (item!=null){
            item.moveToFirst();
            String stepStr = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS));

            JSONArray stepArr = new JSONArray(stepStr);
            int num = stepArr.length();
            StepEntry[] stepEntries;
            stepEntries = new StepEntry[num];

            for (int i = 0;i<num;i++){
                StepEntry se = new StepEntry();
                JSONObject stepItem = stepArr.getJSONObject(i);
                se.setSetp_id(stepItem.getString(RECIPE_STEPS_ID));
                se.setSetp_Desc(stepItem.getString(RECIPE_DESCRIPTION));
                se.setSetp_shortDesc(stepItem.getString(RECIPE_SHORTDESCRIPTION));
                se.setSetp_video(stepItem.getString(RECIPE_VIDEOURL));
                se.setSetp_thumbnai(stepItem.getString(RECIPE_THUMBNAIURL));
                stepEntries[i]=se;
            }
            item.close();
            return stepEntries;
        }else return null;
    }


    @Nullable
    public static IngredientEntry[] getIngreById(Context context, String id)throws JSONException{
        ContentResolver cr = context.getContentResolver();
        Uri uri = RecipeContract.RecipeEntry.buildRecipeUriWithId(id);
        String[] projection = {RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS};
        Cursor item =cr.query(uri,projection,null,null,null);
        if (null!=item){
            item.moveToFirst();

            String ingreStr = item.getString(item.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS));

            JSONArray ingreArr = new JSONArray(ingreStr);
            int num = ingreArr.length();
            IngredientEntry[] ingreEntries;
            ingreEntries = new IngredientEntry[num];

            for (int i = 0;i<num;i++){
                IngredientEntry ie = new IngredientEntry();
                JSONObject ingreItem = ingreArr.getJSONObject(i);
                ie.setIngr_name(ingreItem.getString(RECIPE_INGREDIENT));
                ie.setIngr_measure(ingreItem.getString(RECIPE_MEASURE));
                ie.setIngr_quantity(ingreItem.getString(RECIPE_QUANTITY));

                ingreEntries[i]=ie;
            }
            item.close();
            return ingreEntries;
        }else return null;
    }

}
