package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.somewhat_indie.crimson_ivy.Settings;

import java.util.HashMap;

/**
 * Created by kaholi on 7/15/15.
 */
public class AnimationComp extends Component {
    private HashMap<String,Animation> animations = new HashMap<>(5);

    public Animation animation;
    public String current = "";
    private boolean loopCurrent = false;

    public boolean jumpToDefaultWhenFinished = true;
    private String defaultAnimation = "";


    public Vector2 size_in_meters = new Vector2(1,1);

    private float stateTime = 0;

    public void addAnimation(String name, Texture sheet, int cols, int rows){
        addAnimation(name, sheet, cols, rows, 0, 0, cols, rows);
    }
    public void addAnimation(String name, Texture sheet, int numCols, int numRows, int colOffset, int rowOffset, int totCols, int totRows){
        addAnimation(name, sheet, numCols, numRows, colOffset, rowOffset, totCols, totRows, Animation.PlayMode.NORMAL);
    }
    public void addAnimation(String name, Texture sheet, int numCols, int numRows, int colOffset, int rowOffset, int totCols, int totRows, Animation.PlayMode mode){
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/totCols, sheet.getHeight()/totRows);
        TextureRegion[] frames = new TextureRegion[numCols * numRows];

        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                frames[index++] = tmp[i+rowOffset][j+colOffset];
            }
        }
        animations.put(name, new Animation(Settings.ANIMATION_FRAME_DURATION, Array.with(frames), mode));

    }

    public void update(float deltaTime){
        stateTime += deltaTime;

        if(animation.getAnimationDuration() < stateTime ) {
            if (loopCurrent) {
                stateTime = 0;
            } else if (jumpToDefaultWhenFinished) {
                stateTime = 0;
                current = defaultAnimation;
                animation = animations.get(defaultAnimation);
            }
        }
    }


    public void setAnimation(String name){setAnimation(name,false);}
    public void setAnimation(String name,boolean loop){
        animation = animations.get(name);
        if(!current.equals(name)) {
            stateTime = 0;
            current = name;
        }
        loopCurrent = loop;
    }

    public void setDefaultAnimation(String name){
        defaultAnimation = name;
    }

    public TextureRegion getKeyFrame(){
        return animation.getKeyFrame(stateTime);
    }


}
