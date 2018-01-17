package com.moming.jml.bakingtime.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.moming.jml.bakingtime.R;


public class StepDetailFragment extends Fragment {


    private SimpleExoPlayerView mStepPlayerView;
    private Button mNextButton;
    private Button mPreButton;
    private TextView mDescTextView;



    public StepDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        mDescTextView = view.findViewById(R.id.tv_step_desc);
        mNextButton = view.findViewById(R.id.bt_next);
        mPreButton = view.findViewById(R.id.bt_pre);
        mStepPlayerView = view.findViewById(R.id.player_step);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        int postion = intent.getIntExtra("step_postion",0);
    }
}
