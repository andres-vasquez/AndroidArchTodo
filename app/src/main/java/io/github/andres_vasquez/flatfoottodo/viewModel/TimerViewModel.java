/*
 * Copyright 2017, The Android Open Source Project
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

package io.github.andres_vasquez.flatfoottodo.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import io.github.andres_vasquez.flatfoottodo.utils.Constants;

public class TimerViewModel extends ViewModel {

    private static final String LOG =TimerViewModel.class.getSimpleName();
    private static final int ONE_SECOND = 1000;

    private CountDownTimer timerSync;
    private MutableLiveData<String> elapsedTime = new MutableLiveData<>();


    public TimerViewModel() {
    }

    /**
     * Start timer method
     * @param time
     */
    public void startTimer(final long time){
        stopTimer();
        Log.i(LOG, "Init Timer");
        timerSync=new CountDownTimer(time,ONE_SECOND) {
            public void onTick(long millisUntilFinished) {
                Log.i(LOG, "TimerSync.Tick"+millisUntilFinished);
                float minutes=(millisUntilFinished/1000)/60;
                float seconds = (millisUntilFinished - minutes*1000*60)/1000;

                elapsedTime.setValue(leftPad((int)minutes)+":"+leftPad((int)seconds));
            }
            public void onFinish() {
                elapsedTime.setValue("00:00");
                if(time == Constants.TIMER_LONG){
                    startTimer(Constants.TIMER_SHORT);
                } else {
                    startTimer(Constants.TIMER_LONG);
                }
            }
        };
        timerSync.start();
    }

    /**
     * Stop timer method
     */
    public void stopTimer(){
        Log.i(LOG, "TimerSync.Cancel");
        if(timerSync!=null){
            timerSync.cancel();
        }
    }

    /**
     * Getter ElapseTime variable
     * @return liveData elapsedTime
     */
    public LiveData<String> getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Fill with zeros
     * @param value minutes/seconds
     * @return value in String format with 0
     */
    private String leftPad(int value){
        return String.format("%02d", value);
    }
}
