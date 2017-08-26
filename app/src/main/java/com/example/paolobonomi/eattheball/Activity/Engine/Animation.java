package com.example.paolobonomi.eattheball.Activity.Engine;

/*
 * Created by Paolo Bonomi on 02/08/2017.
 */

import android.content.Context;
import android.util.Log;

import com.example.paolobonomi.eattheball.R;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;

import javax.microedition.khronos.opengles.GL;

class Animation {

    private Sprite[] sprites;
    private int start;
    private int end;

    private boolean haveToRepeat;
    private int repeat_number = 0;
    private int count = 0;

    private int cursor;

    Animation(Context context, String file_name, int start, int end){
        this.sprites = new Sprite[end-start+1];
        loadSprite(context, file_name, start, end);
        this.end = end;
        this.start = start;

        this.cursor = 0;
        this.haveToRepeat = false;
    }

    void setRepeat(int number){
        if(number < 0) {
            this.haveToRepeat = false;
            this.repeat_number = 0;
            return;
        }

        this.haveToRepeat = true;
        this.repeat_number = number;
        this.count = 0;
    }

    void draw(Vec2 body_center, float body_angle, float size, Matrix4f view) {
        try {
            if (this.cursor < start || this.cursor > this.end)
                this.cursor = start;

            this.sprites[this.cursor-this.start].Draw(body_center, body_angle, size, view);

            if (this.haveToRepeat) {
                this.count += 1;
                if (this.count == this.repeat_number) {
                    this.count = 0;
                    this.cursor += 1;
                }
            } else this.cursor++;
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSprite(Context context, String file_name, int start, int end){

        for ( int i = start; i <= end; i++ ) {
            final int id = context.getResources().getIdentifier(file_name + i, "drawable", context.getPackageName());

            if (id == 0)
                this.sprites[i-start] = null;
            else
                this.sprites[i-start] = new Sprite(new Texture(context, id));
        }
    }
}
