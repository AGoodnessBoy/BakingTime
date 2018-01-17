package com.moming.jml.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moming.jml.bakingtime.R;
import com.moming.jml.bakingtime.data.StepEntry;

/**
 * Created by admin on 2018/1/13.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private Context mContext;
    private StepEntry[] mStepData;
    private RecipeStepClickHandler mClickHandler;


    public RecipeStepAdapter(Context context,RecipeStepClickHandler handler) {
        this.mContext = context;
        this.mClickHandler = handler;
    }


    public interface RecipeStepClickHandler{
        void onClick(int postion);


    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForStepItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToPartentImmediately = false;
        View view  = inflater.inflate(layoutIdForStepItem,parent,shouldAttachToPartentImmediately);
        view.setFocusable(true);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        StepEntry stepEntry = mStepData[position];
        holder.mRecipeStepTextView.setText(stepEntry.getSetp_id()+". "+stepEntry.getSetp_shortDesc());
        holder.itemView.setTag(stepEntry);

    }

    @Override
    public int getItemCount() {
        if (mStepData!=null&&mStepData.length!=0){
            return mStepData.length;
        }else {
            return 0;
        }

    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mRecipeStepTextView;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            mRecipeStepTextView = itemView.findViewById(R.id.tv_step_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
        }
    }

    public void swapData(StepEntry[] newData){
        if (newData!=null&&newData.length!=0){
            mStepData = newData;
        }
        notifyDataSetChanged();
    }
}
