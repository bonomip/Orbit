package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.ShopItem;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by paolobonomi on 19/04/2017.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);

        checkPreference(LaunchActivity.this);
        checkDB(DBAdapter.getInstance(LaunchActivity.this));

        if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        final TextView title = (TextView) findViewById(R.id.text_appName);
        title.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        title.setTextSize(GameContract.FONT_LC_TITLE);

        final TextView tapToPlay = (TextView) findViewById(R.id.text_tapToPlay);
        tapToPlay.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        tapToPlay.setTextSize(GameContract.FONT_LC_TEXT);
    }

    private void checkDB(DBAdapter db){
        db.open();
        if(db.getNumberOfScore() == 0) db.populateHighScore();
        if(db.getNumberOfShopItem() == 0) db.populateShopList();
        db.updatePlayerSkin();
        db.close();
    }

    private void checkPreference(Context context){
        GameContract.SHOW_FPS = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GameContract.PREFERENCE_SHOW_FPS, false);
        GameContract.REVERSE_ORIENTATION = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GameContract.PREFERENCE_FLIP_SCREEN, false);
        GameContract.SHOW_SCORE = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GameContract.PREFERENCE_SHOW_SCORE, true);
        GameContract.SHOW_COMBO = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GameContract.PREFERENCE_SHOW_COMBO, true);
        GameContract.SHOW_24 = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(GameContract.PREFERENCE_SHOW_24, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onTap(View v){
        startActivity(new Intent(LaunchActivity.this, MainGameActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
    }

}
