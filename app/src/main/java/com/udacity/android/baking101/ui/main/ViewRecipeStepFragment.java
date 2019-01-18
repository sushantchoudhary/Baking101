package com.udacity.android.baking101.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.android.baking101.R;
import com.udacity.android.baking101.ViewRecipeStepActivity;
import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Step;
import com.udacity.android.baking101.util.AppExecutors;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class ViewRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private ViewRecipeStepViewModel mViewModel;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private static Step selectedStep;
    private static int stepCount;
    private TextView stepInstructionTV;
    private Button previousButton;
    private Button nextButton;

    private static final String TAG = ViewRecipeStepFragment.class.getSimpleName();
    private AppDatabase mDB;

    public static ViewRecipeStepFragment newInstance(Step step) {
        selectedStep = step;
        return new ViewRecipeStepFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_recipe_step_fragment, container, false);

        playerView = rootView.findViewById(R.id.ep_video_view);
        stepInstructionTV = rootView.findViewById(R.id.recipe_step_instruction);

        previousButton = rootView.findViewById(R.id.previous_step);
        nextButton = rootView.findViewById(R.id.next_step);

        mDB = AppDatabase.getsInstance(getContext());


        initializePlayer(selectedStep.getVideoURL());
        return rootView;


    }

    private void initUI() {

            stepInstructionTV.setText(selectedStep.getDescription());

            AppExecutors.getInstance().diskIO().execute(() -> stepCount = mDB.stepDao().getStepsCount());


            if (selectedStep.getId() == stepCount - 1) {
                nextButton.setEnabled(false);
                Drawable background = nextButton.getBackground();
                background.setTint(ContextCompat.getColor(getContext(), R.color.secondary_text));
                nextButton.setBackground(background);
            }

            if (selectedStep.getId() == 0) {
                previousButton.setEnabled(false);
                Drawable background = previousButton.getBackground();
                background.setTint(ContextCompat.getColor(getContext(), R.color.secondary_text));
                previousButton.setBackground(background);

            }

            nextButton.setOnClickListener(view -> {
                if (selectedStep.getId() < stepCount) {

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        LiveData<Step> observableStep = mDB.stepDao().loadStepByStepId(selectedStep.getId() + 1);
                        observableStep.observe(getActivity(), step -> {
                            selectedStep = step;
                            getActivity().runOnUiThread(() -> {
                                Intent intentToStartStepActivity = new Intent(getContext(), ViewRecipeStepActivity.class);
                                intentToStartStepActivity.putExtra(Intent.EXTRA_TEXT, selectedStep);
                                startActivity(intentToStartStepActivity);
                            });
                        });
                    });

                }
            });

            previousButton.setOnClickListener(view -> AppExecutors.getInstance().diskIO().execute(() -> {
                LiveData<Step> observableStep = mDB.stepDao().loadStepByStepId(selectedStep.getId() - 1);
                observableStep.observe(getActivity(), step -> {
                    selectedStep = step;
                    getActivity().runOnUiThread(() -> {
                        Intent intentToStartStepActivity = new Intent(getContext(), ViewRecipeStepActivity.class);
                        intentToStartStepActivity.putExtra(Intent.EXTRA_TEXT, selectedStep);
                        startActivity(intentToStartStepActivity);
                    });
                });
            }));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ViewRecipeStepViewModelFactory factory = new ViewRecipeStepViewModelFactory(mDB, selectedStep.getId());
//        mViewModel = ViewModelProviders.of(this, factory).get(ViewRecipeStepViewModel.class);
//        mViewModel.getStep().observe(this, stepfromViewModel -> {

//            Fragment frg = getFragmentManager().findFragmentByTag("ViewRecipeStepFragment");
//            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();

//        });

        if (savedInstanceState != null) {
            selectedStep = savedInstanceState.getParcelable("step");
        }
        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            initUI();
        }

    }

    private void initializePlayer(String mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            playerView.setPlayer(player);

            // Set the ExoPlayer.EventListener to this activity.
//            playerView.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));

            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent))
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse(mediaUri));
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(selectedStep.getVideoURL());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("step", selectedStep);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Saving step in bundle during orientation change");

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
