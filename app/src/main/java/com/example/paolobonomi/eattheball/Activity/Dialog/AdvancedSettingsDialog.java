package com.example.paolobonomi.eattheball.Activity.Dialog;

/*
 * Created by Paolo Bonomi on 06/08/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

public class AdvancedSettingsDialog extends Dialog {

    Activity parent;

    public AdvancedSettingsDialog(Activity activity){
        super(activity);
        this.parent = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.advanced_settings_dialog);
        setCancelable(true);

        final CheckBox showFPS = (CheckBox) findViewById(R.id.showFPS);
        showFPS.setChecked(GameContract.SHOW_FPS);
        showFPS.setTypeface(GameContract.getTypeface(getContext()));
        showFPS.setTextSize(GameContract.FONT_TEXT);
        showFPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameContract.SHOW_FPS = isChecked;
                PreferenceManager
                        .getDefaultSharedPreferences(buttonView.getContext())
                        .edit()
                        .putBoolean(GameContract.PREFERENCE_SHOW_FPS, isChecked)
                        .apply();
            }
        });

        final CheckBox showScore = (CheckBox) findViewById(R.id.showScore);
        showScore.setChecked(GameContract.SHOW_SCORE);
        showScore.setTypeface(GameContract.getTypeface(getContext()));
        showScore.setTextSize(GameContract.FONT_TEXT);
        showScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameContract.SHOW_SCORE = isChecked;
                PreferenceManager
                        .getDefaultSharedPreferences(buttonView.getContext())
                        .edit()
                        .putBoolean(GameContract.PREFERENCE_SHOW_SCORE, isChecked)
                        .apply();
            }
        });

        final CheckBox showCombo = (CheckBox) findViewById(R.id.showCombo);
        showCombo.setChecked(GameContract.SHOW_COMBO);
        showCombo.setTypeface(GameContract.getTypeface(getContext()));
        showCombo.setTextSize(GameContract.FONT_TEXT);
        showCombo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameContract.SHOW_COMBO = isChecked;
                PreferenceManager
                        .getDefaultSharedPreferences(buttonView.getContext())
                        .edit()
                        .putBoolean(GameContract.PREFERENCE_SHOW_COMBO, isChecked)
                        .apply();
            }
        });

        final CheckBox show24 = (CheckBox) findViewById(R.id.show24h);
        show24.setChecked(GameContract.SHOW_24);
        show24.setTypeface(GameContract.getTypeface(getContext()));
        show24.setTextSize(GameContract.FONT_TEXT);
        show24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GameContract.SHOW_24 = isChecked;
                PreferenceManager
                        .getDefaultSharedPreferences(buttonView.getContext())
                        .edit()
                        .putBoolean(GameContract.PREFERENCE_SHOW_24, isChecked)
                        .apply();
            }
        });

    }
}
