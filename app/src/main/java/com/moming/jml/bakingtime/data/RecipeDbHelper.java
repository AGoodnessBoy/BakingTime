package com.moming.jml.bakingtime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.moming.jml.bakingtime.data.RecipeContract.RecipeEntry;

/**
 * Created by jml on 2018/1/1.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipe.db";

    private static final int DATABASE_VERSION = 3;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE =
                "CREATE TABLE " + RecipeEntry.TABLE_NAME +" ("+
                        RecipeEntry._ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        RecipeEntry.COLUMN_RECIPE_ID +
                        " INTEGER NOT NULL, "+
                        RecipeEntry.COLUMN_RECIPE_NAME +
                        " TEXT NOT NULL, "+
                        RecipeEntry.COLUMN_RECIPE_INGREDIENTS +
                        " TEXT NOT NULL, "+
                        RecipeEntry.COLUMN_RECIPE_STEPS +
                        " TEXT NOT NULL, "+
                        RecipeEntry.COLUMN_RECIPE_SERIVINGS +
                        " TEXT ,"+
                        RecipeEntry.COLUMN_RECIPE_IMAGE +
                        " TEXT ," +
                        " UNIQUE ("+RecipeEntry.COLUMN_RECIPE_ID+
                        ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + RecipeContract.RecipeEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
