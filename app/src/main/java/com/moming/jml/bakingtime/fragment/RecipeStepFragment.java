package com.moming.jml.bakingtime.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.bakingtime.R;
import com.moming.jml.bakingtime.adapter.RecipeIngreAdapter;
import com.moming.jml.bakingtime.adapter.RecipeStepAdapter;
import com.moming.jml.bakingtime.data.IngredientEntry;
import com.moming.jml.bakingtime.data.StepEntry;
import com.moming.jml.bakingtime.tool.ImageTools;
import com.moming.jml.bakingtime.tool.JsonTools;

import org.json.JSONException;


public class RecipeStepFragment extends Fragment implements RecipeStepAdapter.RecipeStepClickHandler{

    private ImageView mImageView;
   //private TextView mNameTextView;
    private TextView mStepErr;
    private TextView mIngreErr;
    private RecyclerView mStepRV;
    private RecyclerView mIngreRV;

    private RecipeStepAdapter mStepAdaper;
    private RecipeIngreAdapter mIngreAdaper;

    private LoaderManager.LoaderCallbacks<StepEntry[]> stepCallback;
    private LoaderManager.LoaderCallbacks<IngredientEntry[]> ingreCallback;

    private Cursor mCursor;


    private final static int LOAD_STEP_ID = 64;
    private final static int LOAD_INGRE_ID = 62;





    public RecipeStepFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("recpice_id");
        String name = intent.getStringExtra("recpice_name");
        Bundle bundle = new Bundle();
        bundle.putString("id",id);

        mImageView.setImageResource(ImageTools.getImageSourceIdByName(name));
        mImageView.setContentDescription(name);
        //mNameTextView.setText(name);
        getActivity().setTitle(name);
        getActivity().getSupportLoaderManager().initLoader(LOAD_STEP_ID,bundle,stepCallback);
        getActivity().getSupportLoaderManager().initLoader(LOAD_INGRE_ID,bundle,ingreCallback);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        mImageView = view.findViewById(R.id.image_recpice_bg);
        //mNameTextView = view.findViewById(R.id.tv_recipe_name_detail);
        mStepRV = view.findViewById(R.id.rv_steps);
        mIngreRV = view.findViewById(R.id.rv_ingredients);

        mIngreErr = view.findViewById(R.id.tv_ingre_error);
        mStepErr = view.findViewById(R.id.tv_step_error);

        mIngreAdaper = new RecipeIngreAdapter(getContext());
        mStepAdaper = new RecipeStepAdapter(getContext(),this);

        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        mStepRV.setLayoutManager(layoutManager_1);
        mStepRV.setHasFixedSize(true);
        mStepRV.setAdapter(mStepAdaper);

        mIngreRV.setLayoutManager(layoutManager_2);
        mIngreRV.setHasFixedSize(true);
        mIngreRV.setAdapter(mIngreAdaper);



       //


        stepCallback = new LoaderManager.LoaderCallbacks<StepEntry[]>() {
            @Override
            public StepLoader onCreateLoader(int id, Bundle args) {
                switch (id){
                    case LOAD_STEP_ID:
                        return new StepLoader(getContext(),args);
                    default:
                        throw new RuntimeException("Loader Not Implemented: " + id);
                }
            }

            @Override
            public void onLoadFinished(Loader<StepEntry[]> loader, StepEntry[] data) {
                if (data!=null&&data.length!=0){
                    mStepErr.setVisibility(View.INVISIBLE);
                    mStepRV.setVisibility(View.VISIBLE);

                    mStepAdaper.swapData(data);
                }else {
                    mStepErr.setVisibility(View.VISIBLE);
                    mStepRV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoaderReset(Loader<StepEntry[]> loader) {
                mStepAdaper.swapData(null);

            }
        };


        ingreCallback = new LoaderManager.LoaderCallbacks<IngredientEntry[]>() {
            @Override
            public IngreLoader onCreateLoader(int id, Bundle args) {
                switch (id){
                    case LOAD_INGRE_ID:
                        return new IngreLoader(getContext(),args);
                    default:
                        throw new RuntimeException("Loader Not Implemented: " + id);
                }
            }

            @Override
            public void onLoadFinished(Loader<IngredientEntry[]> loader, IngredientEntry[] data) {

                if (data!=null&&data.length!=0){
                    mIngreErr.setVisibility(View.INVISIBLE);
                    mIngreRV.setVisibility(View.VISIBLE);

                    mIngreAdaper.swapData(data);
                }else {
                    mIngreErr.setVisibility(View.VISIBLE);
                    mIngreRV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoaderReset(Loader<IngredientEntry[]> loader) {
                mIngreAdaper.swapData(null);

            }
        };

        return view;
    }

    @Override
    public void onClick() {

    }


    public static class StepLoader extends AsyncTaskLoader<StepEntry[]>{

        private Bundle args;

        public StepLoader(Context context,Bundle bundle) {
            super(context);
            args =bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.i("LOAD","onStartLoading");
            forceLoad();
        }

        @Override
        public StepEntry[] loadInBackground() {
            Log.i("LOAD","loadInBackground");
           String recipe_id = args.getString("id");
            try {
                StepEntry[] stepData = JsonTools.getStepById(getContext(),recipe_id);
                return stepData;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(StepEntry[] data) {
            super.deliverResult(data);
        }
    }



    public static class IngreLoader extends AsyncTaskLoader<IngredientEntry[]>{

        private Bundle args;

        public IngreLoader(Context context,Bundle bundle) {
            super(context);
            args =bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.i("LOAD","onStartLoading");
            forceLoad();
        }

        @Override
        public IngredientEntry[] loadInBackground() {
            Log.i("LOAD","loadInBackground");
            String recipe_id = args.getString("id");
            try {
                IngredientEntry[] ingreData = JsonTools.getIngreById(getContext(),recipe_id);
                return ingreData;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(IngredientEntry[] data) {
            super.deliverResult(data);
        }
    }

}
