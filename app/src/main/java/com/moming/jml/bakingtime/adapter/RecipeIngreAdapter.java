package com.moming.jml.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moming.jml.bakingtime.R;
import com.moming.jml.bakingtime.data.IngredientEntry;

/**
 * Created by admin on 2018/1/13.
 */

public class RecipeIngreAdapter extends RecyclerView.Adapter<RecipeIngreAdapter.RecipeIngreViewHolder>  {

    final private Context mContext;
    private IngredientEntry[] mIngerData;

    public RecipeIngreAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecipeIngreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.ingredient_item;
        boolean shouldAttachToPartentImmediately = false;
        View layout = LayoutInflater.from(mContext).inflate(layoutId,parent,shouldAttachToPartentImmediately);

        layout.setFocusable(true);

        return new RecipeIngreViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecipeIngreViewHolder holder, int position) {
        IngredientEntry item = mIngerData[position];
        holder.mIngreQuantityTextView.setText(item.getIngr_quantity()+" "+item.getIngr_measure());
        holder.mIngreNameTextView.setText(item.getIngr_name());
    }

    @Override
    public int getItemCount() {
        if (mIngerData==null||mIngerData.length==0)return 0;
        return mIngerData.length;
    }

    public class RecipeIngreViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIngreNameTextView;
        public final TextView mIngreQuantityTextView;

        public RecipeIngreViewHolder(View itemView) {
            super(itemView);
            mIngreNameTextView = itemView.findViewById(R.id.tv_ingredient_name);
            mIngreQuantityTextView = itemView.findViewById(R.id.tv_ingredient_quantity);
        }
    }

    public void swapData(IngredientEntry[] mData){
        if (mData!=null&&mData.length!=0){
            mIngerData=mData;
        }
        notifyDataSetChanged();
    }
}
