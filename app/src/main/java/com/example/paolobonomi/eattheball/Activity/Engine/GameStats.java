package com.example.paolobonomi.eattheball.Activity.Engine;

/*
 * Created by Paolo Bonomi on 09/05/2017.
 */

public abstract class GameStats {

    private static long score;
    private static int targetHitten;
    private static int targetCount;

    static long getScore(){
        return score;
    }

    private static void addScore(){
        score += GameContract.SCORE_VALUE*ComboCalculator.getCombo();

    }

    static int getTargetHitten(){
        return targetHitten;
    }

    private static void targetHitten(){ targetHitten += 1;
    }

    static int getTargetCount(){
        return targetCount;
    }

    public static void addTarget(){
        targetCount += 1;
    }

    private static void subTarget(){
        targetCount -= 1;
    }

    public static void playerDestroyAnEat(){
        addScore();
        targetHitten();
        subTarget();
    }

    static void reset(){
        score = 0;
        targetHitten = 0;
        targetCount = 0;
        ComboCalculator.resetCombo();
    }
}
