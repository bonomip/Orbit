package com.example.paolobonomi.eattheball.Activity.Engine;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.Calendar;

import com.example.paolobonomi.eattheball.Activity.Dialog.QuitGameDialog;
import com.example.paolobonomi.eattheball.Activity.Activity.MainGameActivity;
import com.example.paolobonomi.eattheball.Activity.Dialog.SettingsDialog;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by Paolo Bonomi on 09/05/2017.
 */

class GameUI {

    //private static String TAG = "GameUI";

    private final MainGameActivity mParent;

    private final TextView textScore;
    private final TextView textCombo;
    private final TextView textFPS;
    private final TextView clock;
    private final RadioGroup pauseGroup;
    private final Animation anim;
    private Calendar calendar;

    GameUI(final MainGameActivity parent){
        this.mParent = parent;

        //LAYOUT_GUI

        //display score
        this.textScore = (TextView) this.mParent.findViewById(R.id.text_score);
        this.textScore.setTypeface(GameContract.getTypeface(this.mParent.getApplicationContext()), Typeface.BOLD);
        this.textScore.setTextSize(GameContract.FONT_TEXT);
        this.textScore.setTextColor(GameContract.COLOR_WHITE);

        //display combo
        this.textCombo = (TextView) this.mParent.findViewById(R.id.text_combo);
        this.textCombo.setTypeface(GameContract.getTypeface(this.mParent.getApplicationContext()), Typeface.BOLD);
        this.textCombo.setTextSize(GameContract.FONT_TEXT);
        this.textCombo.setTextColor(GameContract.COLOR_WHITE);

        //display fps
        this.textFPS = (TextView) this.mParent.findViewById(R.id.text_fps);
        this.textFPS.setTypeface(GameContract.getTypeface(this.mParent.getApplicationContext()), Typeface.BOLD);
        this.textFPS.setTextSize(GameContract.FONT_TEXT);

        //LAYOUT PAUSE

        //group
        this.pauseGroup = (RadioGroup) this.mParent.findViewById(R.id.pause_group);
        this.pauseGroup.setVisibility(View.GONE);

        //settings button
        final Button button_settings = (Button) this.mParent.findViewById(R.id.button_settings);
        button_settings.setTypeface(GameContract.getTypeface(this.mParent.getApplicationContext()), Typeface.BOLD);
        button_settings.setTextSize(GameContract.FONT_BUTTON);
        button_settings.setTextColor(GameContract.COLOR_WHITE);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SettingsDialog(mParent).show();
            }
        });

        //main menu button
        final Button button_main_menu = (Button) this.mParent.findViewById(R.id.button_mainMenu);
        button_main_menu.setTypeface(GameContract.getTypeface(this.mParent.getApplicationContext()), Typeface.BOLD);
        button_main_menu.setTextSize(GameContract.FONT_BUTTON);
        button_main_menu.setTextColor(GameContract.COLOR_WHITE);
        button_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wannaQuit();
            }
        });

        //LAYOUT CLOCK

        //display clock
        this.clock = (TextView) this.mParent.findViewById(R.id.textClock);
        this.clock.setTypeface(GameContract.getTypeface(mParent.getApplicationContext()), Typeface.BOLD);
        this.clock.setTextSize(GameContract.FONT_TEXT);
        this.clock.setTextColor(GameContract.COLOR_WHITE);
        this.clock.setVisibility(View.GONE);

        //COMBO ANIMATION
        anim = new AlphaAnimation(1.0f, 0);
        anim.setDuration(300);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(3);
        anim.setStartTime(0);
    }

    void pauseEnd(){
        this.pauseGroup.setVisibility(View.GONE);
        this.clock.setVisibility(View.INVISIBLE);
        this.textScore.setVisibility(GameContract.SHOW_SCORE ? View.VISIBLE : View.INVISIBLE);
        this.textFPS.setVisibility(GameContract.SHOW_FPS ? View.VISIBLE : View.INVISIBLE);
        this.textCombo.setVisibility(GameContract.SHOW_COMBO ? View.VISIBLE : View.INVISIBLE);
    }

    void playUpdate(){
        if(GameContract.SHOW_SCORE)
            this.textScore.setText(String.format(this.mParent.getResources().getString(R.string.score)+"%d", GameStats.getScore()));
        if(GameContract.SHOW_FPS)
            this.textFPS.setText("FPS:" + GameUtil.calculateFPS());
        if(GameContract.SHOW_COMBO)
            manageTextCombo();
    }

    void pauseStart(){
        this.pauseGroup.setVisibility(View.VISIBLE);
        this.clock.setVisibility(View.VISIBLE);
        this.textCombo.setVisibility(View.INVISIBLE);
        this.textFPS.setVisibility(View.INVISIBLE);
    }

    void pauseUpdate(){
        manageTextClock();
    }

    void wannaQuit(){
        new QuitGameDialog(this.mParent).show();
    }

    private int lastCombo = 1;

    private void manageTextCombo(){
        int combo = ComboCalculator.getCombo();
        this.textCombo.setText(combo > 1 ? String.format("x%o", combo) : " ");
        if(combo > this.lastCombo)
            this.textCombo.startAnimation(this.anim);
        this.lastCombo = combo;
    }

    private void manageTextClock(){

        calendar = Calendar.getInstance();

        if(GameContract.SHOW_24){
            int hour = this.calendar.get( Calendar.HOUR_OF_DAY );
            int minute = this.calendar.get(Calendar.MINUTE);
            this.clock.setText( ( hour >= 10 ? ""+hour : "0"+hour )+":"+( minute >= 10 ? ""+minute : "0"+minute ));
        } else {
            String am_pm = this.calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";
            int hour = this.calendar.get(Calendar.HOUR_OF_DAY);
            int minute = this.calendar.get(Calendar.MINUTE);
            if(hour > 12) hour -= 12;
            this.clock.setText( ( hour >= 10 ? ""+hour : "0"+hour )+":"+( minute >= 10 ? ""+minute : "0"+minute )+" "+am_pm );
        }
    }
}
