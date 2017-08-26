package com.example.paolobonomi.eattheball.Activity.Engine;

/*
 * Created by Paolo Bonomi on 10/08/2017.
 */

import android.content.Context;
import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;

class Animator {

    private Animation[] animations;
    private Boolean[] isActive;

    Animator(Context context, String file_name, int tot_images, int frames_for_anim){
        animations = new Animation[tot_images/frames_for_anim];
        isActive = new  Boolean[tot_images/frames_for_anim];

        for (int i = 0; i < animations.length; i++) {
            animations[i] = new Animation(context,file_name, i*frames_for_anim, ( (i+1)*frames_for_anim) -1 );
            isActive[i] = false;
        }
    }

    void setRepeat(int number) {
        for (Animation animation : this.animations) animation.setRepeat(number);
    }

    void draw(Vec2 body_center, float body_angle, float size, Matrix4f view){
        for(int i = 0; i < animations.length; i++)
            if(isActive[i]) animations[i].draw(body_center, body_angle, size, view);
    }

    void setAnimations(int number){

            for (int i = 0; i < isActive.length; i++)
                isActive[i] = number >= 0 && ( i == number || ( (i+1)%4==0 && i < number) ) ;
    }

}
