package com.moming.jml.bakingtime.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by jml on 2018/1/1.
 */

public class RecipeProvider extends ContentProvider {

    public static final int CODE_RECIPE = 200;

    public static final int CODE_RECIPE_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private RecipeDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority =RecipeContract.CONTENT_AUTHORITY;


        matcher.addURI(authority,RecipeContract.PATH_MOVIE,CODE_RECIPE);

        matcher.addURI(authority,RecipeContract.PATH_MOVIE+"/#",CODE_RECIPE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE_WITH_ID:{
                String recipeIdString = uri.getLastPathSegment();
                String[] selectionArgument = new String[]{recipeIdString};
                cursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        strings,
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID+" =? ",
                        selectionArgument,null,null,s1
                );
                break;
            }

            case CODE_RECIPE:{
                cursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        strings,s,strings1,null,null,s1
                );
                break;
            }
            default:throw new UnsupportedOperationException();

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return  cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int numRowsDeleted;

        if (null == s) s = "1";

        switch (sUriMatcher.match(uri)){

            case CODE_RECIPE:
                numRowsDeleted =mDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        s,
                        strings
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        if (numRowsDeleted !=0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updateReturn;
        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE_WITH_ID:
                updateReturn = mDbHelper.getWritableDatabase().update(
                        RecipeContract.RecipeEntry.TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updateReturn!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return updateReturn;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase database = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE:
                database.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values){
                        long _id =
                                database.insert(
                                        RecipeContract.RecipeEntry.TABLE_NAME,null,
                                        value
                                );
                        if (_id!=-1){
                            rowInserted++;
                        }
                    }
                    database.setTransactionSuccessful();
                }finally {
                    database.endTransaction();
                }
                if (rowInserted > 0){
                    getContext().getContentResolver()
                            .notifyChange(uri,null);
                }
                return rowInserted;
                default:
                    return super.bulkInsert(uri, values);
        }



    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Baking Time.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException(
                "We are not implementing insert in Baking Time. Use bulkInsert instead");
    }


}
