package com.example.paolobonomi.eattheball.Activity.Engine.Script;

import android.util.Log;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameSensor;
import com.example.paolobonomi.eattheball.Activity.Engine.GameState;
import com.example.paolobonomi.eattheball.Activity.Engine.GameUtil;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.List;

import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.GAME_OVER;

/*
 * Created by Paolo Bonomi on 14/04/2017.
 */

public class PlayerScript extends Script {

    //private static final String TAG = "PlayerScript";

    private static World mWorld;
    private static List<Body> mBodies;
    private static Body mBody;
    private static String mTag;

    public PlayerScript( World world, List<Body> bodies){
        super();
        mBodies = bodies;
        mWorld = world;
        mTag = GameContract.PLAYER_TAG;
    }

    public void addBody(Body b){
        mBody = b;
    }

    public void update(){
        checkBodyOutOfBordersAndDeleteIt();

        //costrain max angularVelocity
        if(Math.abs(mBody.getAngularVelocity()) > GameContract.PLAYER_MAX_ANGULAR_VELOCITY)
            mBody.setAngularVelocity(mBody.getAngularVelocity() > 0 ? GameContract.PLAYER_MAX_ANGULAR_VELOCITY : -GameContract.PLAYER_MAX_ANGULAR_VELOCITY);

        move();
    }

    private void checkBodyOutOfBordersAndDeleteIt(){
        if(GameUtil.PlayerOutOfBorders(mBody.getPosition()) || mTag.equals(GameContract.PLAYER_DESTROY_TAG))
            gameOver();
    }

    private void gameOver(){
        GameState.set(GAME_OVER);
        mWorld.destroyBody(mBody);
        mBodies.remove(mBody);
    }

    private void move(){

        //costrin max force x and y to 6.0f
        //apply linear movement on x and y

        if(mBody.getLinearVelocity().length() < GameContract.PLAYER_MAX_SPEED)
            mBody.applyForceToCenter(
                    GameUtil.accelerometerMovement(
                        (GameSensor.x - GameSensor.cx) >= 0 ?
                                Math.min((GameSensor.x - GameSensor.cx), 5.0f)
                                : Math.max((GameSensor.x - GameSensor.cx), -5.0f),
                        (GameSensor.y - GameSensor.cy) >= 0 ?
                                Math.min((GameSensor.y - GameSensor.cy), 5.0f)
                                : Math.max((GameSensor.y - GameSensor.cy), -5.0f)
                    )
            );
    }

    @Override
    public String toString() {
        return mTag;
    }

    public void setTag(String tag){
        mTag = tag;
    }

    public static Vec2 getBodyPosition(){
        return mBody.getPosition();
    }
}
