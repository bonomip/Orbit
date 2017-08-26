package com.example.paolobonomi.eattheball.Activity.Engine;

import android.util.Log;

import com.example.paolobonomi.eattheball.Activity.Engine.Script.TargetScript;
import com.example.paolobonomi.eattheball.Activity.Engine.Script.PlayerScript;
import com.example.paolobonomi.eattheball.Activity.Engine.Script.Script;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

/*
 * Created by Paolo Bonomi on 09/05/2017.
 *
 *
 * Vec2 direction = new Vec2(-velocity.y,velocity.x);
 *
 */

class GameEngine {

    private static final String TAG = "GameEngine";

    private World mWorld;
    private ArrayList<Body> mBodies;

    GameEngine( World world ){
        this.mWorld = world;
        this.mWorld.setContactListener(new GameContactListener());
        this.mBodies = new ArrayList<>();
    }

    void playUpdate(float delta){
        updateAllBodies();
        checkStage();
        step(delta);
    }

    void clearWorld(){
        this.mBodies.clear();
        for (Body b = this.mWorld.getBodyList(); b != null; b = b.getNext())
            this.mWorld.destroyBody(b);
    }

    void setNewGame(){
        GameStats.reset();
        CreatePlayer(GameContract.PLAYER_START_POSITION, new PlayerScript( this.mWorld, this.mBodies ));
        SpawnTarget(PlayerScript.getBodyPosition(), new TargetScript( this.mWorld, this.mBodies));
    }

    void restoreWorld(){
        Log.d(TAG, "restore world");
        destroyAllBodyInWorld();
        restoreAllBodyFromBodyList();
    }

    void gameOverUpdate(float delta){
        Log.d(TAG, "game over update");
        step(delta);
    }

    private void destroyAllBodyInWorld(){

        //qua lo schermo è ancora completamente nero non formattato con il layout full screen

        Log.d(TAG, "destroy all body in world");

        for (int i = 0; i < this.mBodies.size(); i++)
            this.mWorld.destroyBody(this.mBodies.get(i));
    }

    private void restoreAllBodyFromBodyList(){
        Log.d(TAG, "restore all body from list");

        ArrayList<Body> temp = new ArrayList<>();
        for(int i = 0; i < this.mBodies.size(); i++ )
            temp.add(mBodies.get(i));
        this.mBodies.clear();

        for(int i = 0; i < temp.size(); i++ )
            switch (temp.get(i).getUserData().toString())
            {
                case GameContract.TARGET_TAG:
                    RestoreTarget(
                            temp.get(i).getPosition(),
                            temp.get(i).getAngle(),
                            temp.get(i).getLinearVelocity(),
                            temp.get(i).getAngularVelocity(),
                            (TargetScript) temp.get(i).getUserData()
                    );
                    break;
                case GameContract.PLAYER_TAG:
                    RestorePlayer(
                            temp.get(i).getPosition(),
                            temp.get(i).getAngle(),
                            temp.get(i).getLinearVelocity(),
                            temp.get(i).getAngularVelocity(),
                            (PlayerScript) temp.get(i).getUserData()
                    );
                    break;
            }

    }

    private void updateAllBodies(){
        for (int i = 0; i < this.mBodies.size(); i++){
            Script s = (Script) this.mBodies.get(i).getUserData();
            s.update();
        }
    }

    private void checkStage(){

        while(GameStats.getTargetCount() < 1)
            SpawnTarget(PlayerScript.getBodyPosition(), new TargetScript(this.mWorld, this.mBodies));

        /*

        if(score >= 1000){
            while(GameStats.getEatCount() < 2) // two eat if score is 1.000 or more
                SpawnEat(PlayerScript.getBodyPosition(), new TargetScript(this.mWorld, this.mBodies));

            if(score >=1500 && GameStats.canSpawnAngryEat(System.currentTimeMillis()) ) // add angry eat if score >= 1500
                SpawnEat(PlayerScript.getBodyPosition(), new AngryEatScript(this.mWorld, this.mBodies));
        } else
            while(GameStats.getEatCount() < 1) // one eat if score less than 1.000
                SpawnEat(PlayerScript.getBodyPosition(), new TargetScript(this.mWorld, this.mBodies));

        if(GameStats.getEatCount() == mWorld.getBodyCount() && GameState.get() != GameState.GAME_OVER)
            CreatePlayer(GameContract.PLAYER_START_POSITION, new PlayerScript( this.mWorld, this.mBodies ));

           */

    }

    private void step(float delta){
        this.mWorld.step(delta, 20, 20);
    }

    private void CreatePlayer(Vec2 position, PlayerScript player){
        Log.d(TAG, "CreatePlayer()"+Thread.currentThread().getName());
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = new Vec2(0.0f, 0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = GameContract.DAMPING;
        bodyDef.angularDamping = GameContract.ANGULAR_DAMPING;
        bodyDef.userData = player;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(GameContract.PLAYER_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = player;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 0.75f;
        fixtureDef.isSensor = false;

        Body b = this.mWorld.createBody(bodyDef);
        b.createFixture(fixtureDef);

        player.addBody(b);
        this.mBodies.add(b);

    }

    private void RestorePlayer(Vec2 position, float angle, Vec2 linearVelocity, float angularVelocity, PlayerScript player){
        Log.d(TAG, "RestorePlayer()"+Thread.currentThread().getName());
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = angle;
        bodyDef.linearVelocity = linearVelocity;
        bodyDef.angularVelocity = angularVelocity;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = GameContract.DAMPING;
        bodyDef.angularDamping = GameContract.ANGULAR_DAMPING;
        bodyDef.userData = player;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(GameContract.PLAYER_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = player;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 0.75f;
        fixtureDef.isSensor = false;

        Body b = this.mWorld.createBody(bodyDef);
        b.createFixture(fixtureDef);

        player.addBody(b);
        this.mBodies.add(b);
    }

    private void RestoreTarget(Vec2 position, float angle, Vec2 linearVelocity, float angularVelocity, Script eat){
        Log.d(TAG, "RestoreTarget()"+Thread.currentThread().getName());

        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = angle;
        bodyDef.linearVelocity = linearVelocity;
        bodyDef.angularVelocity = angularVelocity;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = GameContract.DAMPING;
        bodyDef.angularDamping = GameContract.ANGULAR_DAMPING;
        bodyDef.userData = eat;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();

        shape.setRadius(GameContract.TARGET_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = eat;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 0.75f;
        fixtureDef.isSensor = true;

        Body b = this.mWorld.createBody(bodyDef);
        b.createFixture(fixtureDef);

        eat.addBody(b);
        this.mBodies.add(b);
    }

    private void SpawnTarget(Vec2 position, Script eat){
        Log.d(TAG, "SpawnTarget()"+Thread.currentThread().getName());

        BodyDef bodyDef = new BodyDef();

        bodyDef.position = GameUtil.createSpawnPositionForTarget(position);
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = new Vec2(0.0f, 0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = GameContract.DAMPING;
        bodyDef.angularDamping = GameContract.ANGULAR_DAMPING;
        bodyDef.userData = eat;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();

        shape.setRadius(GameContract.TARGET_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = eat;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 0.75f;
        fixtureDef.isSensor = true;

        Body b = this.mWorld.createBody(bodyDef);
        b.createFixture(fixtureDef);

        eat.addBody(b);
        this.mBodies.add(b);
    }
}
