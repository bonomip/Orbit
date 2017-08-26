package com.example.paolobonomi.eattheball.Activity.Dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.Activity.MainGameActivity;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameSensor;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by Paolo Bonomi on 11/05/2017.
 */

public class SettingsDialog extends Dialog {

    private Activity parent;
    private static final int SEEK_MAX_VALUE = 1000;
    private static final int PERCENTAGE = 80;

    public SettingsDialog(Activity activity) {
        super(activity);
        this.parent = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        setCancelable(true);

        final Button button_calibrate = (Button) findViewById(R.id.st_calibrate_button);
        button_calibrate.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_calibrate.setTextSize(GameContract.FONT_SD_BUTTON);
        button_calibrate.setTextColor(GameContract.COLOR_WHITE);
        button_calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSensor.calibrate();
            }
        });

        final Button button_flip = (Button) findViewById(R.id.st_flip_screen_button);
        button_flip.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_flip.setTextSize(GameContract.FONT_SD_BUTTON);
        button_flip.setTextColor(GameContract.COLOR_WHITE);
        button_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameContract.REVERSE_ORIENTATION = !GameContract.REVERSE_ORIENTATION;
                parent.setRequestedOrientation(
                        GameContract.REVERSE_ORIENTATION ?
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE :
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                PreferenceManager
                        .getDefaultSharedPreferences(v.getContext())
                        .edit()
                        .putBoolean(GameContract.PREFERENCE_FLIP_SCREEN, GameContract.REVERSE_ORIENTATION)
                        .apply();
            }
        });

        final TextView text_sensitivity = (TextView) findViewById(R.id.st_sensitivity_text);
        text_sensitivity.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        text_sensitivity.setTextSize(GameContract.FONT_DIALOG_TEXT);
        text_sensitivity.setTextColor(GameContract.COLOR_WHITE);

        final SeekBar seek_sensitivity = (SeekBar) findViewById(R.id.st_sensitivity_seekBar);
        seek_sensitivity.setMax(SEEK_MAX_VALUE);
        seek_sensitivity.setProgress( SEEK_MAX_VALUE - getSeekProgressValue(
                GameContract.SENSITIVITY_ADAPTER,
                GameContract.SENSITIVITY,
                PERCENTAGE
                ));
        seek_sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GameContract.SENSITIVITY_ADAPTER = sensibilityAdapter(
                                                                (float) SEEK_MAX_VALUE,
                                                                (float) SEEK_MAX_VALUE - progress,
                                                                GameContract.SENSITIVITY,
                                                                PERCENTAGE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PreferenceManager.getDefaultSharedPreferences(getContext())
                        .edit()
                        .putFloat(GameContract.PREFERENCE_SENSITIVITY, GameContract.SENSITIVITY_ADAPTER)
                        .apply();
            }
        });

        final Button button_advanced_settings = (Button) findViewById(R.id.advanced_settings);
        button_advanced_settings.setTextSize(GameContract.FONT_TEXT);
        button_advanced_settings.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_advanced_settings.setTextColor(GameContract.COLOR_WHITE);
        button_advanced_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AdvancedSettingsDialog(parent).show();
            }
        });
    }

    private static float sensibilityAdapter(float max_range, float value, float default_sensityvity, float max_min_percent){
        return (value / max_range) * (2.0f * max_min_percent * default_sensityvity / 100.0f) - ( max_min_percent * default_sensityvity / 100.0f );
    }

    private static int getSeekProgressValue( float adapt, float default_sensitivity, float max_min_percent ){
        return (int) ((adapt + ( default_sensitivity * max_min_percent / 100.0f )) / ( 2.0f * default_sensitivity * max_min_percent / 100.0f ) * SEEK_MAX_VALUE);
    }
}
