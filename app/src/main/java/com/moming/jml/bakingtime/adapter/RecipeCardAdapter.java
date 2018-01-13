package com.moming.jml.bakingtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.bakingtime.R;
import com.moming.jml.bakingtime.data.RecipeContract;
import com.moming.jml.bakingtime.tool.ImageTools;

/**
 * Created by jml on 2018/1/1.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {

    final private Context mContext;

    private Cursor mCursor;

    final private RecipeCardOnClickHandler mClickHandler;

    private final static String Tag = RecipeCardAdapter.class.getSimpleName();

    public interface RecipeCardOnClickHandler{
        void onClick(String id,String name);
    }


    public RecipeCardAdapter(Context mContext, RecipeCardOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
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
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),imageId);
        Bitmap roundedCornerBitmap = ImageTools.getRoundedCornerBitmap(bitmap,16);

        //Picasso.with(mContext).load(imageId).into(holder.mImageView);
        holder.mImageView.setImageBitmap(roundedCornerBitmap);

        holder.mRecipeNameTextView.setText(
                mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)));
        holder.itemView.setTag(mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID)));
    }

    @Override
    public int getItemCount() {
        if (null  == mCursor) return 0;
        return mCursor.getCount();
    }

    public class RecipeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mRecipeNameTextView;
        public final ImageView mImageView;

        public RecipeCardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.bg_recipe_card_item);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int postion = getAdapterPosition();
            mCursor.moveToPosition(postion);
            String id = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
            String name = (String) mRecipeNameTextView.getText();
            mClickHandler.onClick(id,name);
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
