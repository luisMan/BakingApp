package videoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import tech.niocoders.com.bakingapp.R;

public class videoPlayer extends Fragment implements ExoPlayer.EventListener {
    private Context context;
    private PlayerView simplePlayerView;
    private SimpleExoPlayer player;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private DefaultTrackSelector trackSelector;
    private DataSource.Factory mediaDataSourceFactory;
    private String url;
    private boolean playWhenReady;
    //must be initialized to 0
    private long playbackPosition;
    private int currentWindow;
    private MediaSource  mediaSource;
    public String VIDEO_PLAYER_STR= "VIDEO_PLAYER_STR";
    public String VIDEO_PLAYER_POSITION = "VIDEO_PLAYER_POSITION";
    public String VIDEO_PLAYER_CURRENT_WINDOW = "VIDEO_PLAYER_CURRENT_WINDOW";
    public String VIDEO_PLAY_WHEN_READY="VIDEO_PLAY_WHEN_READY";
    private String TAG = videoPlayer.class.getSimpleName();
    public Bundle globalState;


    //leave this constructor empty so that our FragmentManager instantiate the work
    public  videoPlayer(){}

    public void setContext(Context context) {
        this.context = context;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    //GET THE MEDIA SOURCE URI BASE ON LINK REFERENCE
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void initializePlayer() {
        if(player==null) {

            if(globalState!=null) {
                currentWindow = globalState.getInt(VIDEO_PLAYER_CURRENT_WINDOW);
                playbackPosition = globalState.getLong(VIDEO_PLAYER_POSITION);
                playWhenReady = globalState.getBoolean(VIDEO_PLAY_WHEN_READY);
            }

            bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);


            player = ExoPlayerFactory.newSimpleInstance(getActivity().getBaseContext(), trackSelector);

            simplePlayerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);

            // Set the ExoPlayer.EventListener to this activity.
            player.addListener(this);

            // Prepare the MediaSource.
            Uri uri = Uri.parse(url).buildUpon().build();
            mediaSource = buildMediaSource(uri);

            boolean containStartingPosition = currentWindow != C.INDEX_UNSET;
            if (containStartingPosition) {
                player.seekTo(currentWindow, playbackPosition);
            }

            player.prepare(mediaSource, !containStartingPosition, false);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {


        if(savedInstanceState!=null)
        {
            url =  savedInstanceState.getString(VIDEO_PLAYER_STR);
            currentWindow =  savedInstanceState.getInt(VIDEO_PLAYER_CURRENT_WINDOW);
            playbackPosition =  savedInstanceState.getLong(VIDEO_PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(VIDEO_PLAY_WHEN_READY);
            globalState = savedInstanceState;

         }else{

                playWhenReady = true;
                currentWindow = 0;
                playbackPosition = 0;

        }

        View view =  inflater.inflate(R.layout.video_xml,container,false);
        simplePlayerView = view.findViewById(R.id.exoplayer);

        shouldAutoPlay = true;
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Fragment");
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Fragment");
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause Fragment");
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop Fragment");
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (player != null) {
            currentWindow =  player.getCurrentWindowIndex();
            playbackPosition = player.getCurrentPosition();
            playWhenReady =  player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
            outState.putString(VIDEO_PLAYER_STR,url);
            currentWindow =  player.getCurrentWindowIndex();
            playbackPosition = player.getCurrentPosition();
            playWhenReady =  player.getPlayWhenReady();


            outState.putBoolean(VIDEO_PLAY_WHEN_READY,playWhenReady);
            outState.putLong(VIDEO_PLAYER_POSITION,playbackPosition);
            outState.putInt(VIDEO_PLAYER_CURRENT_WINDOW,currentWindow);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null)
        {
                currentWindow =  savedInstanceState.getInt(VIDEO_PLAYER_CURRENT_WINDOW);
                playbackPosition =  savedInstanceState.getLong(VIDEO_PLAYER_POSITION);
                playWhenReady = savedInstanceState.getBoolean(VIDEO_PLAY_WHEN_READY);
               // Toast.makeText(getActivity().getBaseContext(),"restored The current Step index "+playbackPosition,Toast.LENGTH_LONG).show();
                globalState =  savedInstanceState;
        }
    }

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
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            this.playWhenReady =  playWhenReady;
        } else if((playbackState == ExoPlayer.STATE_READY)){
           this.playWhenReady =  playWhenReady;
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
}
