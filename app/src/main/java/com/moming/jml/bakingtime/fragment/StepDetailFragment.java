package com.moming.jml.bakingtime.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.moming.jml.bakingtime.R;
import com.moming.jml.bakingtime.data.StepEntry;
import com.moming.jml.bakingtime.tool.JsonTools;

import org.json.JSONException;


public class StepDetailFragment extends Fragment implements Player.EventListener {


    public SimpleExoPlayerView mStepPlayerView;
    public Button mNextButton;
    public Button mPreButton;
    public TextView mDescTextView;
    private String mRecipeId;
    private int mStepNow;

    private Dialog mFullScreenDialog;
    private Boolean mExoPlayerFullscreen = false;
    private Boolean mFullscreen = false;
    private ImageView mFullScreenIcon;

    private int mResumeWindow;
    private long mResumePosition;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";


    private FrameLayout mMainVideoFrame;

    private  Uri mStepVideoUri;

    private StepEntry[] mStepEntries;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = StepDetailFragment.class.getSimpleName();




    public StepDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"creat");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        mDescTextView = view.findViewById(R.id.tv_step_desc);
        mNextButton = view.findViewById(R.id.bt_next);
        mPreButton = view.findViewById(R.id.bt_pre);
        mStepPlayerView = view.findViewById(R.id.player_step);
        mMainVideoFrame = view.findViewById(R.id.main_media_frame);

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mStepPlayerView.getParent()).removeView(mStepPlayerView);
            mFullScreenDialog.addContentView(mStepPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }


        mDescTextView.setVisibility(View.VISIBLE);
        mPreButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);


        mNextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickToNext();
                    }
                }
        );

        mPreButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickToPre();
                    }
                }
        );

        return view;
    }
    //下一步
    public void clickToNext(){
        int length = mStepEntries.length;
        releasePlayer();
        if (length>0){
            if (mStepNow<length-1){
                mStepNow = mStepNow + 1;
                mNextButton.setText(getActivity().getResources().getString(R.string.next));
            }else {
                Toast.makeText(getContext(),getActivity().getResources().getString(R.string.last),Toast.LENGTH_LONG)
                        .show();
                mStepNow = length-1;
            }
            try {
                mStepVideoUri = getStepVideoUrl(mRecipeId,mStepNow);
            }catch (JSONException e){
                e.printStackTrace();
            }
            mDescTextView.setText(mStepEntries[mStepNow].getSetp_Desc());
            getActivity().setTitle(mStepEntries[mStepNow].getSetp_shortDesc());
            if (mStepVideoUri!=null){
                initializePlayer(mStepVideoUri);
                mStepPlayerView.setVisibility(View.VISIBLE);
                mMainVideoFrame.setVisibility(View.VISIBLE);
            }else {
                mStepPlayerView.setVisibility(View.GONE);
                mMainVideoFrame.setVisibility(View.GONE);
            }
        }


    }
    //上一步
    public void clickToPre(){
        int length = mStepEntries.length;
        releasePlayer();
        if (length>0){
            if (mStepNow-1>0){
                mStepNow = mStepNow -1;
                mPreButton.setText(getActivity().getResources().getString(R.string.previous));
            }else {
                Toast.makeText(getContext(),getActivity().getResources().getString(R.string.first),Toast.LENGTH_LONG)
                        .show();
                mStepNow = 0;
            }


            try {
                mStepVideoUri = getStepVideoUrl(mRecipeId,mStepNow);
            }catch (JSONException e){
                e.printStackTrace();
            }
            mDescTextView.setText(mStepEntries[mStepNow].getSetp_Desc());
            getActivity().setTitle(mStepEntries[mStepNow].getSetp_shortDesc());
            if (mStepVideoUri!=null){
                initializePlayer(mStepVideoUri);
                mStepPlayerView.setVisibility(View.VISIBLE);
                mMainVideoFrame.setVisibility(View.VISIBLE);
            }else {
                mStepPlayerView.setVisibility(View.GONE);
                mMainVideoFrame.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       if (savedInstanceState!=null){
           mStepNow = savedInstanceState.getInt("step");
           mRecipeId = savedInstanceState.getString("id");
           mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
           mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
           mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
           mStepVideoUri = Uri.parse(savedInstanceState.getString("url")).buildUpon().build();

       }
        initializeMediaSession();
       initFullscreenDialog();
       initFullscreenButton();


        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE&&
                (!(getActivity().getResources().getBoolean(R.bool.isPad)))
                ){
            openFullscreenDialog();
        }




    }



    public void inputData(String id,int postion){
        if (id!=null&&postion>=0){
            mRecipeId = id;
            mStepNow = postion;
            Log.i(TAG,Integer.toString(mStepNow));
        }
        try {
            mStepVideoUri= getStepVideoUrl(mRecipeId,mStepNow);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        releasePlayer();

        if (mStepVideoUri==null){
            Log.i(TAG,"uri null");
            mStepPlayerView.setVisibility(View.GONE);
            mMainVideoFrame.setVisibility(View.GONE);
        }else {
            mStepPlayerView.setVisibility(View.VISIBLE);
            mMainVideoFrame.setVisibility(View.VISIBLE);

            Log.i(TAG,mStepVideoUri.toString());
            Log.i(TAG,"uri not null");
            initializeMediaSession();
            initializePlayer(mStepVideoUri);
        }
        mDescTextView.setText(mStepEntries[mStepNow].getSetp_Desc());
        getActivity().setTitle(mStepEntries[mStepNow].getSetp_shortDesc());

    }


    //初始化全屏
    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }
    //进入全屏
    private void openFullscreenDialog() {
        mFullscreen = true;
        //横屏
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ((ViewGroup) mStepPlayerView.getParent()).removeView(mStepPlayerView);
        mFullScreenDialog.addContentView(mStepPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();

    }
    //关闭全屏
    private void closeFullscreenDialog() {
        mFullscreen = true;
        //竖屏
        if (!(getActivity().getResources().getBoolean(R.bool.isPad))){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }



        ((ViewGroup) mStepPlayerView.getParent()).removeView(mStepPlayerView);
        ((FrameLayout) getActivity().findViewById(R.id.main_media_frame)).addView(mStepPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));

    }
    //初始化全屏按钮
    private void initFullscreenButton() {

        PlaybackControlView controlView = mStepPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipeId!=null){
            outState.putString("id",mRecipeId);
            outState.putInt("step",mStepNow);

        }
        if (mStepVideoUri!=null){
            outState.putString("url",mStepVideoUri.toString());
        }
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"resume");
        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition&&mStepVideoUri!=null){
            initializePlayer(mStepVideoUri);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"pause");

        if (mStepPlayerView != null && mStepPlayerView.getPlayer() != null) {
            mResumeWindow = mStepPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mStepPlayerView.getPlayer().getContentPosition());

            releasePlayer();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }


    public Uri getStepVideoUrl(String id, int step) throws JSONException {
        if (mStepEntries==null){
            mStepEntries = JsonTools.getStepById(getContext(),id);
        }

        Uri uri = null;

        if (mStepEntries!=null){
            String urlStr = mStepEntries[step].getSetp_video();
            if (urlStr.equals("")||urlStr.length()==0){
                uri =  null;
            }else {
                uri =  Uri.parse(urlStr).buildUpon().build();
            }
        }

        return uri;
    }
    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    //初始化MediaSession
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }
    //初始化播放器
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mStepPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),userAgent);
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
            factory.setExtractorsFactory(extractorsFactory);
            MediaSource mediaSource = factory.createMediaSource(mediaUri);

            if (mResumePosition!=C.TIME_UNSET)
                mExoPlayer.seekTo(mResumeWindow,mResumePosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }
    //重置播放器
    private void releasePlayer() {
        if (mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    //播放器监听
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY)&&playWhenReady){
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(),
                    1f
            );
        }else if ((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(),
                    1f
            );
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession,intent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
