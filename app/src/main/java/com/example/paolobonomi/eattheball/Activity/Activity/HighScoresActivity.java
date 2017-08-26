package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.DBContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by paolobonomi on 14/04/2017.
 */

public class HighScoresActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivty_high_scores);

        if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        final TextView head = (TextView) findViewById(R.id.text_ranking);
        head.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        head.setTextSize(GameContract.FONT_HG_TITLE);

        final TextView headers[] = new TextView[3];
        headers[0] = (TextView) findViewById(R.id.text_rk_l);
        headers[1] = (TextView) findViewById(R.id.text_sc_l);
        headers[2] = (TextView) findViewById(R.id.text_nm_l);

        for ( TextView h : headers ) {
            h.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
            h.setTextSize(GameContract.FONT_HG_HEAD);
        }

        final TextView text_topScores[][] = new TextView[10][3];
        text_topScores[0][0] = (TextView) findViewById(R.id.text_topScore00);
        text_topScores[1][0] = (TextView) findViewById(R.id.text_topScore01);
        text_topScores[2][0] = (TextView) findViewById(R.id.text_topScore02);
        text_topScores[3][0] = (TextView) findViewById(R.id.text_topScore03);
        text_topScores[4][0] = (TextView) findViewById(R.id.text_topScore04);
        text_topScores[5][0] = (TextView) findViewById(R.id.text_topScore05);
        text_topScores[6][0] = (TextView) findViewById(R.id.text_topScore06);
        text_topScores[7][0] = (TextView) findViewById(R.id.text_topScore07);
        text_topScores[8][0] = (TextView) findViewById(R.id.text_topScore08);
        text_topScores[9][0] = (TextView) findViewById(R.id.text_topScore09);
        text_topScores[0][1] = (TextView) findViewById(R.id.text_topScore10);
        text_topScores[1][1] = (TextView) findViewById(R.id.text_topScore11);
        text_topScores[2][1] = (TextView) findViewById(R.id.text_topScore12);
        text_topScores[3][1] = (TextView) findViewById(R.id.text_topScore13);
        text_topScores[4][1] = (TextView) findViewById(R.id.text_topScore14);
        text_topScores[5][1] = (TextView) findViewById(R.id.text_topScore15);
        text_topScores[6][1] = (TextView) findViewById(R.id.text_topScore16);
        text_topScores[7][1] = (TextView) findViewById(R.id.text_topScore17);
        text_topScores[8][1] = (TextView) findViewById(R.id.text_topScore18);
        text_topScores[9][1] = (TextView) findViewById(R.id.text_topScore19);
        text_topScores[0][2] = (TextView) findViewById(R.id.text_topScore20);
        text_topScores[1][2] = (TextView) findViewById(R.id.text_topScore21);
        text_topScores[2][2] = (TextView) findViewById(R.id.text_topScore22);
        text_topScores[3][2] = (TextView) findViewById(R.id.text_topScore23);
        text_topScores[4][2] = (TextView) findViewById(R.id.text_topScore24);
        text_topScores[5][2] = (TextView) findViewById(R.id.text_topScore25);
        text_topScores[6][2] = (TextView) findViewById(R.id.text_topScore26);
        text_topScores[7][2] = (TextView) findViewById(R.id.text_topScore27);
        text_topScores[8][2] = (TextView) findViewById(R.id.text_topScore28);
        text_topScores[9][2] = (TextView) findViewById(R.id.text_topScore29);
        for (int i = 0; i < text_topScores.length; i++)
            for(int j = 0; j < text_topScores[0].length; j++){
                text_topScores[i][j].setTextColor(GameContract.HG_COLOR[i]);
                text_topScores[i][j].setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
                text_topScores[i][j].setTextSize(GameContract.FONT_HG_TEXT);
            }

        final Cursor result = DBAdapter.getInstance(this).open().getTopTenScores();
        DBAdapter.getInstance(this).close();
        result.moveToFirst();

        //update High score according to DB
        // 1st, 2nd, 3rd, 4th ... 10th
        for(int i = 0; i < 10; i++){
            text_topScores[i][0].setText(getResources().getStringArray(R.array.position)[i]);
        }

        int index = 0;
        int score_id = 0;

        if(getIntent().getBooleanExtra(GameContract.INTENT_IS_FROM_REGISTRATION, false)) {

            for (int i = 0; i < result.getCount(); i++) {
                if(score_id < result.getInt(result.getColumnIndex(DBContract.Scores._ID))) {
                    index = i;
                    score_id = result.getInt(result.getColumnIndex(DBContract.Scores._ID));
                }
                text_topScores[i][1].setText(String.valueOf(result.getLong(result.getColumnIndex(DBContract.Scores.COLUMN_NAME_SCORE))));
                text_topScores[i][2].setText(result.getString(result.getColumnIndex(DBContract.Scores.COLUMN_NAME_NAME)).toUpperCase());
                result.moveToNext();
            }

            final Animation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(500);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setStartTime(0);

            text_topScores[index][0].startAnimation(anim);
            text_topScores[index][1].startAnimation(anim);
            text_topScores[index][2].startAnimation(anim);
        } else
            for (int i = 0; i < result.getCount(); i++) {
                text_topScores[i][1].setText(String.valueOf(result.getInt(result.getColumnIndex(DBContract.Scores.COLUMN_NAME_SCORE))));
                text_topScores[i][2].setText(result.getString(result.getColumnIndex(DBContract.Scores.COLUMN_NAME_NAME)).toUpperCase());
                result.moveToNext();
            }
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
    public void onBackPressed(){
        onClick(this.getWindow().getDecorView().getRootView());
    }

    public void onClick(View v) {
        startActivity(new Intent(HighScoresActivity.this, MainMenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) );
    }
}
