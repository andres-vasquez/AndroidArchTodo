package io.github.andres_vasquez.flatfoottodo;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.andres_vasquez.flatfoottodo.utils.Constants;
import io.github.andres_vasquez.flatfoottodo.viewModel.TimerViewModel;
import io.github.andres_vasquez.flatfoottodo.viewModel.TodoViewModel;

/**
 * Created by andresvasquez on 5/24/17.
 */

public class TimerActivity extends AppCompatActivity implements LifecycleRegistryOwner, View.OnClickListener{
    private static final String LOG = TimerActivity.class.getSimpleName();

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    //ViewModel
    private TimerViewModel mTimerViewModel;
    public TodoViewModel todoViewModel;

    //Timer controls
    public ImageView timerActionImageView;
    public TextView timerTextView;

    private boolean isTimerActive = false;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        timerActionImageView.setOnClickListener(this);
    }

    /**
     * Init view Models in Main Activity
     */
    private void initViewModel() {
        todoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        mTimerViewModel= ViewModelProviders.of(this).get(TimerViewModel.class);
    }

    /**
     * Observe elapseTime variable changes
     */
    final Observer<String> elapsedTimeObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable final String timerResult) {
            if(timerResult!=null){
                if(timerResult.compareTo("00:00")==0){
                    ringtone();
                }
                timerTextView.setText(timerResult);
            }
        }
    };

    /**
     * Subscribe observer to elapseTime variable changes
     */
    private void subscribeEventChanges() {
        mTimerViewModel.getElapsedTime().observe(this, elapsedTimeObserver);
    }

    /**
     * Unsubscribe observer to elapseTime variable changes
     */
    private void unsubscribeEventChanges() {
        mTimerViewModel.getElapsedTime().removeObserver(elapsedTimeObserver);
    }

    @Override
    public void onClick(View v) {
        if(isTimerActive){
            timerActionImageView.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            unsubscribeEventChanges();
            mTimerViewModel.stopTimer();
            timerTextView.setText(getString(R.string.timer_title));
        } else {
            timerActionImageView.setImageResource(R.drawable.ic_stop_white_24dp);
            mTimerViewModel.startTimer(Constants.TIMER_LONG);
            subscribeEventChanges();
        }

        //Change status
        isTimerActive=!isTimerActive;
    }

    /**
     * Play sound
     */
    public void ringtone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();

            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(Constants.VIBRATE_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
