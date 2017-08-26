package com.example.paolobonomi.eattheball.Activity.Engine;

/*
 * Created by Paolo Bonomi on 10/05/2017.
 */

abstract class ComboCalculator {

    private static int comboValue = 1;
    private static int midCombo = 0;
    private static long lastTime;
    private static long whenPauseStarts;

    static void setTime(){

        long newTime = System.currentTimeMillis();

        if(checkComboStillOn())
            addCombo();
        else
            resetCombo();

        lastTime = newTime;
    }

    private static void addCombo(){
        if(comboValue == 1) {
            midCombo += 1;
            if(midCombo == 2) comboValue += 1; // +3 eat
        } else if (comboValue == 2) {
            midCombo += 1;
            if(midCombo == 7) comboValue += 1; // +5 eat
        } else if (comboValue == 3) {
            midCombo += 1;
            if(midCombo == 14) comboValue += 1; // +7 eat
        } else if (comboValue == 4) {
            midCombo += 1;
            if(midCombo == 23) comboValue += 1; // +9 eat
        }
    }

    static void resetCombo(){
        comboValue = 1;
        midCombo = 0;
    }

    private static boolean checkComboStillOn(){
        if(lastTime+GameContract.COMBO_TIME_OUT >= System.currentTimeMillis())
            return true;
        resetCombo();
        return false;
    }

    static void pauseStarts(long time){
        whenPauseStarts = time;
    }

    static void restoreCombo(long time){
        lastTime += time - whenPauseStarts;
    }

    static int getCombo(){
        checkComboStillOn();
        return comboValue;
    }
}
