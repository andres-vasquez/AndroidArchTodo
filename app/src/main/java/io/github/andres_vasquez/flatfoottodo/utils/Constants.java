package io.github.andres_vasquez.flatfoottodo.utils;

/**
 * Created by andresvasquez on 5/24/17.
 */

public class Constants {
    //Filter todoTasks by state
    public static final int FILTER_ALL=0;
    public static final int FILTER_PENDING =1;
    public static final int FILTER_FINISHED=2;

    //Default filterList state
    public static final int FILTER_DEFAULT=FILTER_PENDING;

    public static final long TIMER_LONG=25*60*1000;//25 minutes
    public static final long TIMER_SHORT=5*60*1000;//5 minutes

    //Vibration duration
    public static final long VIBRATE_DURATION = 3000;
}
