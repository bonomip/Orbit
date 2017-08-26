package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.Score;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by Paolo Bonomi on 17/04/2017.
 */

public class NewHighScoreActivity extends AppCompatActivity {


    private String TAG = "NewHighScoreActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_new_high_score);

        if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        final long newBestScore = getIntent().getLongExtra(GameContract.INTENT_NEW_SCORE, 0);

        final DBAdapter dbAdapter = DBAdapter.getInstance(this);

        final String playerName = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getString(GameContract.GAME_PLAYER_NAME, null);

        final EditText textAddName = (EditText) findViewById(R.id.text_addName);
        if(playerName != null) textAddName.setText(playerName);
        textAddName.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        textAddName.setTextSize(16);

        final TextView text_new = (TextView) findViewById(R.id.text_newBestScore);
        text_new.setTextSize(22);
        text_new.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);

        final Button button_ok = (Button) findViewById(R.id.button_addName);
        button_ok.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        button_ok.setTextSize(20);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkSpelling(textAddName.getText().toString()))
                    textAddName.setError(getResources().getText(R.string.error_insert_name));
                else {
                    savePlayerName(textAddName.getText().toString());
                    insertName(textAddName.getText().toString());
                    goToHighScore();
                }
            }

            private boolean checkSpelling(String name) {
                return name.length() != 0 && name.length() < 9 && isAlpha(name);
            }

            private boolean isAlpha(String name){
                for( char c : name.toCharArray() ) {
                    if(!Character.isLetter(c))
                        return false;
                }
                return true;
            }

            private void savePlayerName(String name){
                if(playerName == null) PreferenceManager
                        .getDefaultSharedPreferences(NewHighScoreActivity.this)
                        .edit()
                        .putString(GameContract.GAME_PLAYER_NAME, name)
                        .apply();
            }

            private void insertName(String name){
                if(dbAdapter.open().insertScore(new Score(name, newBestScore)) > 0)
                    Log.d(TAG, "INSERT OK");
                dbAdapter.close();
            }

            private void goToHighScore() {
                startActivity(new Intent(NewHighScoreActivity.this, HighScoresActivity.class)
                        .putExtra(GameContract.INTENT_IS_FROM_REGISTRATION, true)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
    }
}
