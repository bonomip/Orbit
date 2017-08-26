package com.example.paolobonomi.eattheball.Activity.Engine;

import org.jbox2d.common.Vec2;

/*
 * Created by Paolo Bonomi on 12/04/2017.
 */

public abstract class GameUtil {

    //private static final String TAG = "GameUtil";

    static Vec2 createSpawnPositionForTarget(Vec2 playerPosition){
        Vec2 newRandomPosition;
        do {
            newRandomPosition = new Vec2(   (float) (Math.random() * (
                                                                GameContract.ENGINE_MAX_WIDTH
                                                                - GameContract.TARGET_RADIUS)),
                                            (float) (Math.random() * (
                                                            GameContract.ENGINE_MAX_HEIGHT
                                                            - GameContract.TARGET_RADIUS))
                                        );
        } while ( distance( newRandomPosition, playerPosition ) < GameContract.PLAYER_RADIUS*3
                    || newRandomPosition.x < GameContract.TARGET_RADIUS*2
                    || newRandomPosition.y < GameContract.TARGET_RADIUS*2
                );

        return newRandomPosition;
    }

    private static float distance(Vec2 a, Vec2 b){
        return (float) Math.sqrt((a.x - b.x)*(a.x - b.x)+(a.y - b.y)*(a.y - b.y));
    }

    public static Vec2 accelerometerMovement(float x, float y){
        return new Vec2(x/(GameContract.SENSITIVITY+GameContract.SENSITIVITY_ADAPTER)*GameContract.PLAYER_SPEED,
                        y/(GameContract.SENSITIVITY+GameContract.SENSITIVITY_ADAPTER)*GameContract.PLAYER_SPEED);
    }

    public static boolean PlayerOutOfBorders(Vec2 position){
        return  position.y < 0.0f - GameContract.PLAYER_RADIUS ||
                position.x < 0.0f - GameContract.PLAYER_RADIUS ||
                position.x > GameContract.ENGINE_MAX_WIDTH + GameContract.PLAYER_RADIUS ||
                position.y > GameContract.ENGINE_MAX_HEIGHT + GameContract.PLAYER_RADIUS;
    }

    //calculate fps and log it
    private static long time = 0;
    private static int frame_count = 0;
    private static int fps = 0;

    static int calculateFPS(){

        long now_time = System.currentTimeMillis();

        if(now_time > time+1000) {
            fps = frame_count;
            time = now_time;
            frame_count = 0;
        }
        else frame_count++;

        return fps;
    }
}