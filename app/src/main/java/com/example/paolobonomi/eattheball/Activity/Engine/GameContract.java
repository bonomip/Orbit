package com.example.paolobonomi.eattheball.Activity.Engine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.paolobonomi.eattheball.R;

import org.jbox2d.common.Vec2;

/*
 * Created by Paolo Bonomi on 12/04/2017.
 */

public abstract class GameContract {

    //SCORE
    static final float SCORE_VALUE = 10.0f;

    //COMBO
    static final long COMBO_TIME_OUT = 2700;

    //SCREEN
    static final float ENGINE_MAX_HEIGHT = 7.25f;
    public static final float ENGINE_MAX_WIDTH = 13.0f;
    public static final float ENGINE_DIAGONAL_RAPPORT = ENGINE_MAX_WIDTH / ENGINE_MAX_HEIGHT;

    //SETTINGS
    public static boolean REVERSE_ORIENTATION;
    public static float SENSITIVITY;
    public static float SENSITIVITY_ADAPTER;

    //ADVANCED SETTINGS
    public static boolean SHOW_FPS;
    public static boolean SHOW_SCORE;
    public static boolean SHOW_COMBO;
    public static boolean SHOW_24;

    //BODY
    public static final String DESTROY_TAG = "dt";
    public static final float DAMPING = 1.5f;
    public static final float ANGULAR_DAMPING = 0.7f;


    //PLAYER
    public static final String PLAYER_TAG = "pt";
    public static final String PLAYER_DESTROY_TAG = "pdt";
        //BODY
    static float PLAYER_RADIUS = 0.295f;
    static final float PLAYER_SPEED = 20.0f;
    public static final float PLAYER_MAX_SPEED = 10.0f;
    public static final float PLAYER_MAX_ANGULAR_VELOCITY = 3.5f;
    static final Vec2 PLAYER_START_POSITION = new Vec2( GameContract.ENGINE_MAX_WIDTH/2, GameContract.ENGINE_MAX_HEIGHT/2+0.035f );
        //SKINS
    public static int PLAYER_SKIN_ID = 0;
    public static final String PLAYER_SKIN_FILE_NAME = "player";

    //TARGET VALUES
    public static final String TARGET_TAG = "e";
        //BODY
    static final float TARGET_RADIUS = 0.235f;
    public static final float TARGET_SPEED = 8.5f;
    public static final float TARGET_MAX_SPEED = 2.0f;

    //INTENT
    public static final String INTENT_NEW_SCORE = "ins";
    public static final String INTENT_IS_FROM_REGISTRATION = "ifr";

    //STORED PREFERENCES
    public static final String GAME_BALANCE = "gb";
    public static final String GAME_PLAYER_NAME = "gpn";
        //SETTINGS
    public static final String PREFERENCE_FLIP_SCREEN = "pfs";
    public static final String PREFERENCE_SENSITIVITY = "ps";
        //ADVANCED SETTINGS
    public static final String PREFERENCE_SHOW_FPS = "psf";
    public static final String PREFERENCE_SHOW_SCORE = "pss";
    public static final String PREFERENCE_SHOW_COMBO = "psc";
    public static final String PREFERENCE_SHOW_24 = "ps24";

    //FONT
    private static Typeface tf;
    public static Typeface getTypeface(Context context){
        if (tf == null)
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/Pixeled.ttf");
        return tf;
    }
        //SIZE
            //SHOP
    public static final int FONT_SH_TEXT = 10;
    public static final int FONT_SH_BUTTON = 18;
            //LAUNCH
    public static final int FONT_LC_TITLE = 30;
    public static final int FONT_LC_TEXT = 12;
            //MENU
    public static final int FONT_M_BUTTON = 25;
            //HIGHSCORE
    public static final int FONT_HG_TITLE = 28;
    public static final int FONT_HG_HEAD = 11;
    public static final int FONT_HG_TEXT = 13;
            //BUTTON
    public static final int FONT_BUTTON = 22;
            // ALERT DIALOG
    public static final int FONT_DIALOG = 16;
    public static final int FONT_DIALOG_TEXT = 12;
            // SETTINGS DIALOG
    public static final int FONT_SD_BUTTON = 20;
            //INFO TEXT
    public static final int FONT_TEXT = 10;

    //COLORS
    public static final int COLOR_WHITE = Color.parseColor("#ffffff");
        //HIGHSCORE ROW COLORS
    public static final int HG_COLOR[] = {
            Color.parseColor("#DE3163"),
            Color.parseColor("#FF7F50"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#a4ff1c"),
            Color.parseColor("#016801"),
            Color.parseColor("#32C6A6"),
            Color.parseColor("#6495ED"),
            Color.parseColor("#4169E1"),
            Color.parseColor("#0000FF"),
            Color.parseColor("#9F00FF")
    };
}
