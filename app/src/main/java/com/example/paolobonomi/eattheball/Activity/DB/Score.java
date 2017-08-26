package com.example.paolobonomi.eattheball.Activity.DB;

import android.content.ContentValues;

/*
 * Created by Paolo Bonomi on 15/04/2017.
 */

public class Score {

    private long score;
    private String name;

    public Score(String name, long score){
        super();
        this.name = name;
        this.score = score;
    }

    ContentValues getAsContentValue(){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Scores.COLUMN_NAME_NAME, this.name);
        cv.put(DBContract.Scores.COLUMN_NAME_SCORE, this.score);
        return cv;
    }

    @Override
    public String toString(){
        return "\nName="+name+"\n"+
                "Score="+score;
    }

    /*
    public static Score buildFromBundle(Bundle bundle){
        String name = bundle.getString(DBContract.Scores.COLUMN_NAME_NAME);
        long score = bundle.getInt(DBContract.Scores.COLUMN_NAME_SCORE);

        return new Score(name, score);
    }

    */

}
