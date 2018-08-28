package videoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import tech.niocoders.com.bakingapp.R;

public class videoPlayer extends Fragment implements ExoPlayer.EventListener {
    private Context context;
    private PlayerView simplePlayerView;
    private SimpleExoPlayer player;
    private String url;
    private boolean playWhenReady = true;
    //must be initialized to 0
    private long playbackPosition=0;
    private int currentWindow = 0;
    private MediaSource  mediaSource;
    public String VIDEO_PLAYER_STR= "VIDEO_PLAYER_STR";
    public String VIDEO_PLAYER_POSITION = "VIDEO_PLAYER_POSITION";
    public String VIDEO_PLAYER_CURRENT_WINDOW = "VIDEO_PLAYER_CURRENT_WINDOW";
    private String TAG = videoPlayer.class.getSimpleName();


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
        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        simplePlayerView.setPlayer(player);

        // Set the ExoPlayer.EventListener to this activity.
        player.addListener(this);

        // Prepare the MediaSource.
        Uri uri =  Uri.parse(url).buildUpon().build();
        mediaSource =  buildMediaSource(uri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.seekTo(currentWindow, playbackPosition);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            url =  savedInstanceState.getString(VIDEO_PLAYER_STR);
            currentWindow =  savedInstanceState.getInt(VIDEO_PLAYER_CURRENT_WINDOW);
            playbackPosition =  savedInstanceState.getLong(VIDEO_PLAYER_POSITION);
            Toast.makeText(getActivity().getBaseContext(),"createView The current Step index "+playbackPosition,Toast.LENGTH_LONG).show();

        }

        View view =  inflater.inflate(R.layout.video_xml,container,false);
        simplePlayerView = view.findViewById(R.id.exoplayer);

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
        if ((Util.SDK_INT <= 23 && !TextUtils.isEmpty(url))) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause Fragment");
        super.onPause();
        playbackPosition = player.getCurrentPosition();
        currentWindow =  player.getCurrentWindowIndex();
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
        Toast.makeText(getActivity().getBaseContext(),"savedInstace The current Step index "+player.getCurrentPosition(),Toast.LENGTH_LONG).show();

        outState.putString(VIDEO_PLAYER_STR,url);
        if(player!=null)
        {
            outState.putLong(VIDEO_PLAYER_POSITION, player.getCurrentPosition());
            outState.putInt(VIDEO_PLAYER_CURRENT_WINDOW,player.getCurrentWindowIndex());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null)
        {
                currentWindow =  savedInstanceState.getInt(VIDEO_PLAYER_CURRENT_WINDOW);
                playbackPosition =  savedInstanceState.getLong(VIDEO_PLAYER_POSITION);
                Toast.makeText(getActivity().getBaseContext(),"restored The current Step index "+playbackPosition,Toast.LENGTH_LONG).show();

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
