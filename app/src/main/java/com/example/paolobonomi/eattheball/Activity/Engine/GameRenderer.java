package com.example.paolobonomi.eattheball.Activity.Engine;

import android.content.Context;
import android.opengl.GLES20;

import com.example.paolobonomi.eattheball.Activity.Activity.MainGameActivity;
import com.example.paolobonomi.eattheball.R;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/*
 * Created by Paolo Bonomi on 09/05/2017.
 */

class GameRenderer {

    //private static final String TAG = "GameRender";

    private MainGameActivity mParent;
    private World mWorld;

    //SPRITE THINGS
    private Sprite playerSprite;
    private Animation targetAnim;
    //private Animator orbitAnim;

    private Matrix4f view;

    GameRenderer(final MainGameActivity parent, World world){
        this.mParent = parent;
        this.mWorld = world;
        this.view = new Matrix4f();
    }

    void init(){
        //Enable 2D Textures
        Square.InitSquare();

        // Enable Culling
        //GLES20.glFrontFace(GLES20.GL_CCW);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glCullFace(GLES20.GL_BACK);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        final Context context = this.mParent.getApplicationContext();

        this.playerSprite = new Sprite( new Texture(
                context,
                context.getResources().getIdentifier(
                        GameContract.PLAYER_SKIN_FILE_NAME+GameContract.PLAYER_SKIN_ID,
                        "drawable",
                        context.getPackageName())));

        this.targetAnim = new Animation( context, "e", 0, 29 );
        this.targetAnim.setRepeat(2);

        //this.orbitAnim = new Animator(context, "e", 480 , 30);
        //this.orbitAnim.setRepeat(2);

        GLES20.glClearColor( 0.0f / 255.0f, 56.0f / 255.0f, 79.0f / 255.0f, 255.0f / 255.0f);
    }

    void draw(){
        //Log.d(TAG, "draw");

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        int num_body = this.mWorld.getBodyCount();

        if(num_body>= 0){
            Body body = this.mWorld.getBodyList();
            for(int i = 0; i < num_body; i++){
                Square.Color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

                //increase ratio to reduce png size on screen
                switch(body.getUserData().toString()) {
                    case GameContract.PLAYER_TAG:
                        this.playerSprite.Draw( body.getWorldCenter(), body.getAngle(), 1.05f / 350.0f, this.view );
                        //this.orbitAnim.setAnimations(GameStats.getTargetHitten()-1);
                        //this.orbitAnim.draw(body.getWorldCenter(), body.getAngle(), 1.05f/ 150.0f, this.view);
                        break;
                    case GameContract.TARGET_TAG:
                        this.targetAnim.draw(body.getWorldCenter(), body.getAngle(), 1.05f / 350.0f, this.view );
                }
                body = body.getNext();
            }
        }
    }

    void setSize(int width, int height){

        final float height_ratio = ((float)height)/((float)width);
        //final float pixels_per_unit = 100.0f;
        //final float base_units = 13f;
        float virtual_width = 13f;
        float virtual_height = virtual_width * height_ratio;

        this.view = new Matrix4f().ortho(0,virtual_width,0,virtual_height,1,-1);
        //.orthoM(View,0,0,Width,0,Height,1,-1);
    }

}
