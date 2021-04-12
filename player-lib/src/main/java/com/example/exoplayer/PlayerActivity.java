/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exoplayer;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {

  private PlaybackStateListener playbackStateListener;
  private static final String TAG = com.example.exoplayer.PlayerActivity.class.getName();

  private PlayerView playerView;
  public static SimpleExoPlayer player;
  private boolean playWhenReady = true;
  private int currentWindow = 0;
  private long playbackPosition = 0;
  private boolean onFullScreen;
  private Uri extras;


  public static ImageButton button_play;
  public static ImageButton button_pause;
  public static ImageButton button_fullscreen;
  public static ImageButton button_setting;
  public static ImageButton setting_save;
  public static ImageButton button_forward;
  public static View button_rewind;
  public static View button_fastforward;
  public static View button_subtitles;
  public static View button_progress_placeholder;
  public static View view_back;
  public static boolean edit = false;

  public long startTime1 = System.currentTimeMillis();
  public long elapsedTime1 = 0;
  private View selected_item = null;
  private View root;
  private int offset_x = 0;
  private int offset_y = 0;
  Boolean touchFlag = false;
  ViewGroup.LayoutParams imageParams;
  int eX, eY;



  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    playerView = findViewById(R.id.video_view);
    playbackStateListener = new PlaybackStateListener();
  }

  @Override
  public void onStart() {
    super.onStart();
    extras = getIntent().getData();
    if(extras != null) {
      Toast toast = Toast.makeText(getApplicationContext(),
              extras.toString(), Toast.LENGTH_SHORT);
      toast.show();
    }
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    hideSystemUi();
    if ((Util.SDK_INT <= 23 || player == null)) {
      initializePlayer();
    }
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  private void initializePlayer() {
    if (player == null) {
      DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
      trackSelector.setParameters(
              trackSelector.buildUponParameters().setMaxVideoSizeSd());
      player = new SimpleExoPlayer.Builder(this)
              .setTrackSelector(trackSelector)
              .build();
    }
    playerView.setPlayer(player);
    if (extras != null){
      MediaItem mediaItem = MediaItem.fromUri(extras);
      /*MediaItem mediaItem = new MediaItem.Builder()
            .setUri(getString(R.string.media_url_dash))
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build();*/
      player.setMediaItem(mediaItem);
      playerView.setControllerShowTimeoutMs(3000);
      player.setPlayWhenReady(playWhenReady);
      player.seekTo(currentWindow, playbackPosition);
      player.addListener(playbackStateListener);
      player.prepare();
    }
    initializeButton();
  }

  private void initializeButton(){
    button_play = (ImageButton) findViewById(R.id.exo_play);
    button_pause = (ImageButton) findViewById(R.id.exo_pause);
    button_fullscreen = (ImageButton) findViewById(R.id.exo_fullscreen);
    button_fullscreen.setOnClickListener(playbackStateListener);
    button_setting = (ImageButton) findViewById(R.id.exo_settings);
    button_setting.setOnClickListener(playbackStateListener);
    setting_save = (ImageButton) findViewById(R.id.settings_save);
    setting_save.setOnClickListener(playbackStateListener);
    button_rewind = (View) findViewById(R.id.exo_rew);
    //button_rewind.setOnTouchListener(this);
    button_fastforward = (View) findViewById(R.id.exo_ffwd);
    //button_fastforward.setOnTouchListener(this);
    button_subtitles = (View) findViewById(R.id.exo_subtitle);
    //button_subtitles.setOnTouchListener(this);
    button_forward = (ImageButton) findViewById(R.id.exo_forward);
    //button_forward.setOnTouchListener(this);
    view_back = (View) findViewById(R.id.view_back);
    //view_back.setOnClickListener(playbackStateListener);


    //button_progress_placeholder = (View) findViewById(R.id.exo_progress_placeholder);
    onFullScreen = false;
  }
  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      playWhenReady = player.getPlayWhenReady();
      player.removeListener(playbackStateListener);
      player.release();
      player = null;
    }
  }

  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      Log.d("ORIENTATION", "LANDSCAPE");
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      Log.d("ORIENTATION", "PORTRAIT");
    }
    super.onConfigurationChanged(newConfig);
  }

  private class PlaybackStateListener implements Player.EventListener, View.OnTouchListener, View.OnClickListener {

    @Override
    public void onPlaybackStateChanged(int playbackState) {
      String stateString;
      switch (playbackState) {
        case ExoPlayer.STATE_IDLE:
          stateString = "STATE_IDLE";
          break;
        case ExoPlayer.STATE_BUFFERING:
          stateString = "STATE_BUFFERING";
          break;
        case ExoPlayer.STATE_READY:
          stateString = "STATE_READY";
          break;
        case ExoPlayer.STATE_ENDED:
          stateString = "STATE_ENDED";
          break;
        default:
          stateString = "UNKNOWN_STATE";
          break;
      }
      Log.d(TAG, "changed state to " + stateString);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      return false;
    }

    private void clickAction(int button) {
      if (button == R.id.exo_fullscreen) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      }
    }

    @Override
    public void onClick(View v) {
      final int id = v.getId();
      if((id == R.id.view_back) && (edit)){
        playerView.showController(); return;
      }
      if(id == R.id.exo_fullscreen){
        if(onFullScreen){
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
          button_fullscreen.setImageResource(R.drawable.exo_styled_controls_fullscreen_enter);
        }
        else {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
          button_fullscreen.setImageResource(R.drawable.exo_styled_controls_fullscreen_exit);
        }
        onFullScreen = !onFullScreen;
      }
      if(id == R.id.exo_settings){

        BottomSheetDialogFragment dialogFragment = CustomBottomSheetDialogFragment.newInstance(isControls());
        dialogFragment.show(getSupportFragmentManager(), CustomBottomSheetDialogFragment.TAG);

      }
      if(id == R.id.settings_save){
        View rootView = (View) v.getRootView();
        int back = R.drawable.circle_back;
        rootView.findViewById(R.id.view_back).setBackgroundResource(R.color.colorNull);
        rootView.findViewById(R.id.exo_settings).setVisibility(View.VISIBLE);

        rootView.findViewById(R.id.linearLayout).setBackground(null);
        rootView.findViewById(R.id.exo_play).setBackgroundResource(back);
        rootView.findViewById(R.id.exo_play).setClickable(true);
        player.setPlayWhenReady(true);
        PlayerView playerView = rootView.findViewById(R.id.video_view);
        playerView.setControllerShowTimeoutMs(3000);

        if(button_subtitles.getVisibility() == View.VISIBLE) button_subtitles.setBackgroundResource(back);
        if(button_forward.getVisibility() == View.VISIBLE) button_forward.setBackgroundResource(back);
        if(button_fastforward.getVisibility() == View.VISIBLE) button_fastforward.setBackgroundResource(back);
        if(button_rewind.getVisibility() == View.VISIBLE) button_rewind.setBackgroundResource(back);

        edit = false;
        v.setVisibility(View.INVISIBLE);
      }
    }
  }


  private View[] isControls() {
    View[] isControls = new View[5];
    isControls[0] = button_progress_placeholder;
    isControls[1] = button_rewind;
    isControls[2] = button_fastforward;
    isControls[3] = button_subtitles;
    isControls[4] = button_forward;
    return isControls;
  }

  public static void Edit(View view){
    edit = true;
    View frame = view.findViewById(R.id.view_back);
    int back_r = R.drawable.frame_edit;
    int back_b = R.drawable.frame_edit_b;
    frame.setBackgroundResource(back_r);
    view.findViewById(R.id.exo_settings).setVisibility(View.INVISIBLE);
    view.findViewById(R.id.settings_save).setVisibility(View.VISIBLE);
    view.findViewById(R.id.linearLayout).setBackgroundResource(back_r);
    view.findViewById(R.id.exo_play).setBackgroundResource(back_r);
    view.findViewById(R.id.exo_play).setClickable(false);
    player.setPlayWhenReady(false);
    PlayerView playerView = view.findViewById(R.id.video_view);
    playerView.setControllerShowTimeoutMs(0);

    if(button_subtitles.getVisibility() == View.VISIBLE) button_subtitles.setBackgroundResource(back_b);
    if(button_forward.getVisibility() == View.VISIBLE) button_forward.setBackgroundResource(back_b);
    if(button_fastforward.getVisibility() == View.VISIBLE) button_fastforward.setBackgroundResource(back_b);
    if(button_rewind.getVisibility() == View.VISIBLE) button_rewind.setBackgroundResource(back_b);
  }


}
