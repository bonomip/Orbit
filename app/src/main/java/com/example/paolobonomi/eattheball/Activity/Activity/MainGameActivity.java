package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.paolobonomi.eattheball.Activity.Engine.Game;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameSensor;
import com.example.paolobonomi.eattheball.R;

import java.util.Calendar;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * Created by paolobonomi on 03/04/2017.
 */

public class MainGameActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static final String TAG = "MainGame";

    private final Semaphore semaphore = new Semaphore(1, true);
    private GLSurfaceView glSurfaceView = null;

    private Game game;

    private long lastTrick = -1;
    private float accumulator = 0.0f;
    private boolean control = true;


    private GameSensor gameSensor;
    private SensorManager sManager;
    private Sensor accelerometer;

    private final Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                semaphore.acquire(1);
                if(control) {
                    findViewById(R.id.layout_gui).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_body).setVisibility(View.VISIBLE);
                    control = false;
                }
                game.UI();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally { semaphore.release(1); }
        }
    };

     @Override
    public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         Log.d(TAG, "onCreate() " + Thread.currentThread().getName());

         setContentView(R.layout.activity_main_game);

         if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

         this.glSurfaceView = (GLSurfaceView) findViewById(R.id.game_canvas);
         this.glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
         this.glSurfaceView.setEGLContextClientVersion(2);
         this.glSurfaceView.setRenderer(this);
         this.glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

         this.sManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
         this.accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

         GameContract.SENSITIVITY = accelerometer.getMaximumRange();
         GameContract.SENSITIVITY_ADAPTER = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                 .getFloat(GameContract.PREFERENCE_SENSITIVITY, 0.0f);

         this.game = new Game(this);
         this.gameSensor = new GameSensor();

         this.sManager.registerListener(this.gameSensor, accelerometer, SensorManager.SENSOR_DELAY_GAME);
     }

    @Override
    protected void onPause() {
        super.onPause();
            Log.d(TAG, "onPause() " + Thread.currentThread().getName());
            this.sManager.unregisterListener(this.gameSensor);
            this.glSurfaceView.onPause();
            this.game.exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
            Log.d(TAG, "onResume() "+Thread.currentThread().getName());
            this.sManager.registerListener(this.gameSensor, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            this.glSurfaceView.onResume();
            this.game.resume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() ");
    }

    public void onTap(View v){
        try {

            this.semaphore.acquire(1);
            this.game.onTap();
        } catch (Exception e) { Log.e(TAG, e.getMessage()+" "+Thread.currentThread().getName()); } finally { this.semaphore.release(1); }

    }

    @Override
    public void onBackPressed(){
        try {
            this.semaphore.acquire(1);
            game.wannaQuit();
        } catch (Exception e ){ Log.e(TAG, e.getMessage()+" "+Thread.currentThread().getName()); } finally { this.semaphore.release(1); }
    }

    //implements Renderer.class methods

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        try {
            this.semaphore.acquire(1);
            //27.0f, 20.0f, 100.0f
            Log.d(TAG, "onSurfaceCreated() "+Thread.currentThread().getName());
            GLES20.glClearColor(0.0f / 255.0f, 0.0f / 255.0f, 0.0f / 255.0f, 0.0f / 255.0f);
            this.game.Init();
            runOnUiThread(this.uiRunnable);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally { this.semaphore.release(1); }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height){
        try {
            this.semaphore.acquire(1);
            Log.d(TAG, "onSurfaceChanged() "+Thread.currentThread().getName());
            gl.glViewport(0, 0, width, height);
            this.game.SetSize(width, height);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally { this.semaphore.release(1); }
    }

    @Override
    public void onDrawFrame(GL10 gl){

        try {
            semaphore.acquire(1);

            //UPDATING ENGINE
            if(lastTrick == -1)
                lastTrick = Calendar.getInstance().getTime().getTime();
            final float minTimestep = 1.0f / 150.0f;
            //calculate Delta Tricks
            long nowTricks = Calendar.getInstance().getTime().getTime();
            accumulator += (float) (nowTricks - lastTrick) / 1000.0f;
            lastTrick = nowTricks;
            // Update for the total amount of time and any remainder. This ensures smoothest framerate.
            while(accumulator > minTimestep) {
                game.Update(minTimestep);
                accumulator -= minTimestep;
            }
            //game.Update(total_delta);
            game.Draw();

            runOnUiThread(uiRunnable);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally { semaphore.release(1); }
    }

}
