package com.example.paolobonomi.eattheball.Activity.Engine.Script;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameStats;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.List;

/*
 * Created by Paolo Bonomi on 14/04/2017.
 */

public class TargetScript extends Script {

    //private static final String TAG = "TargetScript";

    private static World mWorld;
    private static List<Body> mBodies;
    private Body mBody;
    private String mTag;


    public TargetScript(World world, List<Body> bodies){
        super();
        mWorld = world;
        mBodies = bodies;
        this.mTag = GameContract.TARGET_TAG;

        GameStats.addTarget();
    }

    public void addBody(Body b){
        this.mBody = b;
    }

    public void update(){
        //Log.d(TAG, "im alive");
        if(this.mTag.equals(GameContract.DESTROY_TAG))
            destroyThisTarget();
    }

    private void destroyThisTarget(){
        try {
        mBodies.remove(this.mBody);
        mWorld.destroyBody(this.mBody);
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally { GameStats.playerDestroyAnEat(); }
    }

    @Override
    public String toString() {
        return this.mTag;
    }

    public void setTag(String tag){
        this.mTag = tag;
    }
}
