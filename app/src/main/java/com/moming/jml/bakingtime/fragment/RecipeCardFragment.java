package com.moming.jml.bakingtime.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.backingtime.R;
import com.moming.jml.bakingtime.adapter.RecipeCardAdapter;
import com.moming.jml.bakingtime.data.RecipeContract;
import com.moming.jml.bakingtime.sync.RecipeSyncUntils;


public class RecipeCardFragment extends Fragment{

    private static final String Tag = RecipeCardFragment.class.getSimpleName();


    public static final String[] MAIN_RECIPE_PROJECTION ={
            RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,
            RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,
    };

    public static final int INDEX_MOVIE_ID=0;
    public static final int INDEX_RECIPE_NAME=1;

    public static final int RECIPE_LOAD_ID = 20;


    RecyclerView mRecyclerView;
    ProgressBar mLoadingBar;
    TextView  mErrorText;

    private RecipeCardAdapter mRecipeCardAdapter;

    public android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> callbacks;

    public RecipeCardFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_card, container, false);

        mLoadingBar = view.findViewById(R.id.pb_loading);
        mRecyclerView = view.findViewById(R.id.rv_recipe_card);
        mErrorText = view.findViewById(R.id.tv_error);

        // Inflate the layout for this fragment

        // StaggeredGridLayoutManager layoutManager =
        //        new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeCardAdapter = new RecipeCardAdapter(getContext());
        mRecyclerView.setAdapter(mRecipeCardAdapter);


        callbacks = new android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {


                switch (i){
                    case RECIPE_LOAD_ID: {
                        Log.v(Tag,Integer.toString(i));
                        showLoading();
                        Context context = getContext();
                        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
                        return  new CursorLoader(context, uri,
                                MAIN_RECIPE_PROJECTION, null, null, null);
                    }
                    default:
                        throw new RuntimeException("Loader Not Implemented: " + i);
                }
            }



            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

                mRecipeCardAdapter.swapCursor(cursor);

                Log.v(Tag,"onLoadFinished");

                if (cursor.getCount()==0){
                    showError();
                }else {
                    showData();
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mRecipeCardAdapter.swapCursor(null);
            }
        };

        RecipeSyncUntils.initialize(getContext());


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(RECIPE_LOAD_ID,null,callbacks);
    }

    private void showLoading(){
        Log.v(Tag,"show loading");
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    private void showData(){
        Log.v(Tag,"show data");
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    private void showError(){
        Log.v(Tag,"show error");
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }



}
