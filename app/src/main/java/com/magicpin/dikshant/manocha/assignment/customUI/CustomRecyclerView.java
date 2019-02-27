package com.magicpin.dikshant.manocha.assignment.customUI;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.magicpin.dikshant.manocha.assignment.MainActivity;
import com.magicpin.dikshant.manocha.assignment.R;
import com.magicpin.dikshant.manocha.assignment.adapter.CustomRecyclerviewAdapter;
import com.magicpin.dikshant.manocha.assignment.beans.VideoBean;
import com.magicpin.dikshant.manocha.assignment.utility.MyUtil;

import java.util.ArrayList;

public class CustomRecyclerView extends RecyclerView {

    private ArrayList<VideoBean> videos;
    private SimpleExoPlayer[] simpleExoPlayer;
    private MediaSource videoSources;
    private PlayerView playerView;
    private Context application;
    private boolean isVideoThere = false;
    private int videoNumber = -1;
    private View parentView;
    private int videoHeight, screenHeight;
    private ProgressBar progressBar;
    private ImageView sample;
    private long[] positions;
    private FrameLayout frameLayout;
    private Context context;
    private Dialog fullScreenDialog;
    public boolean fullScreen=false;
    int previousPosition = -1;
    int i;

    public CustomRecyclerView(Context context,
                              AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
        this.context=context;
        init(context);
    }

    public CustomRecyclerView(Context context,
                              AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        this.videos = MainActivity.videos;
        positions = new long[videos.size()];
        application = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoHeight = point.x;
        screenHeight = point.y;
        playerView = new PlayerView(application);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator(true, 16),
                MyUtil.MIN_BUFFER_DURATION,
                MyUtil.MAX_BUFFER_DURATION,
                MyUtil.MIN_PLAYBACK_START_BUFFER,
                MyUtil.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

        simpleExoPlayer=new SimpleExoPlayer[videos.size()];
        for (int k = 0; k < videos.size(); k++) {
            simpleExoPlayer[k] = ExoPlayerFactory.newSimpleInstance(application, trackSelector, loadControl);
        }

        playerView.setPlayer(simpleExoPlayer[0]);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(application,
                Util.getUserAgent(application, "Assignment"), defaultBandwidthMeter);
        for (i = 0; i < videos.size(); i++) {
            simpleExoPlayer[i].addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {

                        case Player.STATE_BUFFERING:
                            playerView.setAlpha(0.5f);
                            playerView.setKeepScreenOn(true);
                            if (progressBar != null) {
                                progressBar.setVisibility(VISIBLE);
                            }

                            break;
                        case Player.STATE_ENDED:
                            simpleExoPlayer[previousPosition].seekTo(0);
                            playerView.setKeepScreenOn(false);
                            break;
                        case Player.STATE_IDLE:
                            playerView.setKeepScreenOn(false);
                            break;
                        case Player.STATE_READY:
                            if (progressBar != null) {
                                progressBar.setVisibility(GONE);
                            }
                            playerView.setVisibility(VISIBLE);
                            playerView.setKeepScreenOn(true);
                            playerView.setAlpha(1);
                            if(sample!=null)
                                sample.setVisibility(GONE);
                            break;
                        default:
                            break;
                    }
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
            });


            String uriString = videos.get(i).getLink();
            if (uriString != null) {
                Uri uri = Uri.parse(uriString);
                Handler mainHandler = new Handler();
                videoSources = new HlsMediaSource(uri,
                        dataSourceFactory, mainHandler, null);

                simpleExoPlayer[i].prepare(videoSources,false,false);
                simpleExoPlayer[i].setPlayWhenReady(false);
            }
        }
            simpleExoPlayer[0].setPlayWhenReady(true);
            addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        playVideo();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {

                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {
                    if (isVideoThere && parentView != null && parentView.equals(view)) {
                        removeVideoView(playerView);
                        videoNumber = -1;
                        playerView.setVisibility(INVISIBLE);
                    }

                }
            });


        }

        public void playVideo () {
            int targetPosition, startPosition, endPosition;
            startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);
                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }

            if (targetPosition < 0 ) {
                return;
            }
            videoNumber = targetPosition;
            if (playerView == null) {
                return;
            }

            playerView.setVisibility(INVISIBLE);
            removeVideoView(playerView);

            int at = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

            View child = getChildAt(at);
            if (child == null) {
                return;
            }
            CustomRecyclerviewAdapter.MyViewHolder holder = (CustomRecyclerviewAdapter.MyViewHolder) child.getTag();
            if (holder == null) {
                videoNumber = -1;
                return;
            }

            progressBar = holder.progressBar;
            sample = holder.sample;
            frameLayout = holder.itemView.findViewById(R.id.video_layout);
            frameLayout.addView(playerView);
            playerView.setPlayer(simpleExoPlayer[targetPosition]);
            frameLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterFullScreen();
                }
            });
            isVideoThere = true;
            parentView = holder.itemView;
            playerView.requestFocus();
            playerView.setUseController(false);
            if(previousPosition!=-1)
            simpleExoPlayer[previousPosition].setPlayWhenReady(false);
            playerView.setPlayer(simpleExoPlayer[targetPosition]);
            previousPosition = targetPosition;
            simpleExoPlayer[targetPosition].seekTo(positions[targetPosition]);
            simpleExoPlayer[targetPosition].setPlayWhenReady(true);

        }

        public void enterFullScreen()
        {
            if(fullScreen == false) {
                fullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen){
                    @Override
                    public void onBackPressed() {
                        if(fullScreen) {
                            exitFullScreen();
                        }
                        super.onBackPressed();
                    }

                };
                ((ViewGroup) playerView.getParent()).removeView(playerView);
                playerView.setUseController(true);
                fullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                fullScreen = true;
                fullScreenDialog.show();
                playerView.showController();
            }

        }

        public void exitFullScreen()
        {
            fullScreen=false;
            fullScreenDialog.dismiss();
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            frameLayout.addView(playerView);
            playerView.setUseController(false);
        }
        public int getVisibleVideoSurfaceHeight ( int pos){
            int at = pos - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

            View child = getChildAt(at);
            if (child == null) {
                return 0;
            }

            int[] location = new int[2];
            child.getLocationInWindow(location);

            if (location[1] < 0) {
                return location[1] + videoHeight;
            } else {
                return screenHeight - location[1];
            }
        }
        public void removeVideoView (PlayerView playerView)
        {
            ViewGroup parent = (ViewGroup) playerView.getParent();
            if (parent == null) {
                return;
            }
            positions[previousPosition] = simpleExoPlayer[previousPosition].getCurrentPosition();
            int index = parent.indexOfChild(playerView);
            if (index >= 0) {
                parent.removeViewAt(index);
                isVideoThere = false;
            }
        }

        public void onRelease () {

            if (simpleExoPlayer != null) {
                for (i = 0; i < videos.size() && simpleExoPlayer[i]!=null; i++) {
                    simpleExoPlayer[i].release();
                    simpleExoPlayer[i]=null;
                }
                simpleExoPlayer = null;
            }
            parentView = null;
        }

        public void pausePlayer() {
        if(previousPosition!=-1)
            simpleExoPlayer[previousPosition].setPlayWhenReady(false);
        }
        public void resumePlayer()
        {
            playVideo();
        }

}

