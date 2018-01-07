package com.moming.jml.bakingtime.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jml on 2018/1/1.
 */

public class RecipeContract {

    public static final String CONTENT_AUTHORITY =
            "com.moming.jml.backingtime";

    public static final Uri BASE_CONTENT_URI
            =Uri.parse("content://"+CONTENT_AUTHORITY);



    public static final String PATH_MOVIE = "recipe";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_RECIPE_NAME = "name";

        public static final String COLUMN_RECIPE_IMAGE = "image";

        public static final String COLUMN_RECIPE_STEPS = "steps";

        public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";

        public static final String COLUMN_RECIPE_SERIVINGS = "servings";

        public static Uri buildRecipeUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

    }

}
