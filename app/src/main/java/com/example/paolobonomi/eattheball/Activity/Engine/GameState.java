package com.example.paolobonomi.eattheball.Activity.Engine;

/*
 * Created by Paolo Bonomi on 10/05/2017.
 */

public abstract class GameState {

    //dafault value is INIZIALIZE
    private static int mState = 4;

    static final int PLAY = 0;
    static final int PAUSE = 1;
    public static final int GAME_OVER = 2;
    public static final int STOPPED = 3;
    static final int INITIALIZE = 4;
    static final int STANDBY = 5;

    static int get(){
        return mState;
    }

    public static void set(int state){
        mState = state;
    }
}