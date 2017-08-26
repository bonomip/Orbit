package com.example.paolobonomi.eattheball.Activity.Engine;

import android.content.Intent;
import android.preference.PreferenceManager;

import android.util.Log;

import com.example.paolobonomi.eattheball.Activity.Activity.MainGameActivity;
import com.example.paolobonomi.eattheball.Activity.Activity.NewHighScoreActivity;
import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.GAME_OVER;
import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.INITIALIZE;
import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.PAUSE;
import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.PLAY;
import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.STANDBY;
import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.STOPPED;

/*
 * Created by paolobonomi on 04/04/2017.
 */

public class Game {

    private static final String TAG = "Game";

    private GameUI mGameUI;
    private GameEngine mGameEngine;
    private GameRenderer mGameRenderer;

    private MainGameActivity parent;
    private DBAdapter dbAdapter;

    public Game( MainGameActivity parent ){
        Log.d(TAG, "Game() "+Thread.currentThread().getName());

        final World world = new World(new Vec2(0.0f, 0.0f));

        this.mGameUI = new GameUI( parent );
        this.mGameEngine = new GameEngine( world );
        this.mGameRenderer = new GameRenderer( parent, world );

        this.parent = parent;
        this.dbAdapter = DBAdapter.getInstance(parent.getApplicationContext());

        GameState.set(INITIALIZE);
    }

    public void resume() {
        Log.d(TAG, "resume() "+Thread.currentThread().getName());

        switch (GameState.get()) {
            case INITIALIZE:
                Log.d("\t", "Initialize");
                mGameEngine.setNewGame();
                GameState.set(PLAY);
                break;
            case STANDBY:
                Log.d("\t", "Standby");
                mGameEngine.restoreWorld();
                GameState.set(PAUSE);
                mGameUI.pauseStart();
                break;
        }
    }

    public void Init() {
        Log.d(TAG, "Init() "+Thread.currentThread().getName());
        mGameRenderer.init();
    }

    public void exit(){
        Log.d(TAG, "exit() "+Thread.currentThread().getName());

        switch (GameState.get()) {
            case PLAY:
                //Log.d("\t", "play");
                GameState.set(STANDBY);
                ComboCalculator.pauseStarts(System.currentTimeMillis());
                break;
            case PAUSE:
                //Log.d("\t", "pause");
                GameState.set(STANDBY);
                ComboCalculator.pauseStarts(System.currentTimeMillis());
                break;
            default:
                //Log.d("\t", "default");
                GameState.set(STOPPED);
                ComboCalculator.pauseStarts(System.currentTimeMillis());
                break;
        }
    }

    private void incrementBalance(){
        PreferenceManager
                .getDefaultSharedPreferences(this.parent.getApplicationContext())
                .edit()
                .putInt(GameContract.GAME_BALANCE, PreferenceManager
                        .getDefaultSharedPreferences(this.parent.getApplicationContext())
                        .getInt(GameContract.GAME_BALANCE, 0)
                        +GameStats.getTargetHitten())
                .apply();
    }

    //called each frame
    public void Update(float delta) {
        //Log.d(TAG, GameState.get()+"");
        switch (GameState.get()){
            case PLAY:
                //Log.d("\t", "PLAY");
                mGameEngine.playUpdate(delta);
                break;
            case GAME_OVER:
                //Log.d("\t", "GAMEOVER");
                incrementBalance();
                mGameEngine.gameOverUpdate(delta);
                checkScore();
                break;
        }
    }

    private void checkScore(){

        if (this.dbAdapter.open().isBestTenScore(GameStats.getScore()) && GameStats.getScore() > 0 )
            goToNewHighScoreActivityAndStopped();
        else
            restartAndPause();

        this.dbAdapter.close();
    }

    //PUT GAME IN PAUSE
    private void restartAndPause(){
        mGameEngine.clearWorld();
        mGameEngine.setNewGame();
        GameState.set(PAUSE);
        this.parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameUI.pauseStart();
            }
        });
    }

    //PUT GAME IN STOPPED
    private void goToNewHighScoreActivityAndStopped(){
        try {
            this.parent.getWindow().getContext()
                    .startActivity(new Intent(this.parent.getWindow().getContext(), NewHighScoreActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(GameContract.INTENT_NEW_SCORE, GameStats.getScore()));
            GameState.set(STOPPED);
        } catch (Exception e) { e.printStackTrace(); }
    }

    //called each frame
    public void Draw(){
        mGameRenderer.draw();
    }

    //called each frame by runOnUiThread
    public void UI(){
        switch (GameState.get()) {
            case PLAY:
                mGameUI.playUpdate();
                break;
            case PAUSE:
                mGameUI.pauseUpdate();
                break;
        }
    }

    public void wannaQuit(){
        GameState.set(PAUSE);
        mGameUI.pauseStart();
        ComboCalculator.pauseStarts(System.currentTimeMillis());
        mGameUI.wannaQuit();
    }

    public void SetSize(int width, int height) {
        Log.d(TAG, "SetSize() "+Thread.currentThread().getName());
        mGameRenderer.setSize(width, height);
    }

    public void onTap(){
        if(GameState.get() == PAUSE){
            GameState.set(PLAY);
            ComboCalculator.restoreCombo(System.currentTimeMillis());
            this.mGameUI.pauseEnd();
        } else {
            GameState.set(PAUSE);
            ComboCalculator.pauseStarts(System.currentTimeMillis());
            this.mGameUI.pauseStart();
        }

    }
}
