package com.moming.jml.bakingtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.backingtime.R;
import com.moming.jml.bakingtime.data.RecipeContract;
import com.squareup.picasso.Picasso;

/**
 * Created by jml on 2018/1/1.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {

    final private Context mContext;

    private Cursor mCursor;

    private final static String Tag = RecipeCardAdapter.class.getSimpleName();


    public RecipeCardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecipeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForCardItem = R.layout.recipe_card_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToPartentImmediately = false;
        View view  = inflater.inflate(layoutIdForCardItem,parent,shouldAttachToPartentImmediately);
        view.setFocusable(true);
        return new RecipeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeCardViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        //Context context = holder.itemView.getContext();
        String name = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
        Log.v(Tag, name);

        int imageId = getImageSourceIdByName(name);
        Picasso.with(mContext).load(imageId).into(holder.mImageView);

        holder.mRecipeNameTextView.setText(
                mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)));

    }

    @Override
    public int getItemCount() {
        if (null  == mCursor) return 0;
        return mCursor.getCount();
    }

    public class RecipeCardViewHolder extends RecyclerView.ViewHolder{

        public final TextView mRecipeNameTextView;
        public final ImageView mImageView;

        public RecipeCardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.bg_recipe_card_item);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);

        }
    }

    public void swapCursor(Cursor newOne){
        mCursor = newOne;
        notifyDataSetChanged();
    }

    public int getImageSourceIdByName(String name){

        switch (name){
            case "Cheesecake":
                return R.drawable.cheesecake;
            case "Yellow Cake":
                return R.drawable.yellow_cake;
            case "Nutella Pie":
                return R.drawable.nutella_pie;
            case "Brownies":
                return R.drawable.brownies;
            default:return 0;
        }

    }



























}
