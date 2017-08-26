package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by Paolo Bonomi on 14/04/2017.
 */

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenu";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main_menu);

        if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        final Button button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, MainGameActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) );
            }
        });
        button_start.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        button_start.setTextSize(GameContract.FONT_M_BUTTON);
        button_start.setTextColor(GameContract.COLOR_WHITE);

        final Button button_shop = (Button) findViewById(R.id.button_shop);
        button_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, ShopActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
            }
        });
        button_shop.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        button_shop.setTextSize(GameContract.FONT_M_BUTTON);
        button_shop.setTextColor(GameContract.COLOR_WHITE);

        Button button_high_score = (Button) findViewById(R.id.button_topScores);
        button_high_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, HighScoresActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) );
            }
        });
        button_high_score.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        button_high_score.setTextSize(GameContract.FONT_M_BUTTON);
        button_high_score.setTextColor(GameContract.COLOR_WHITE);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
