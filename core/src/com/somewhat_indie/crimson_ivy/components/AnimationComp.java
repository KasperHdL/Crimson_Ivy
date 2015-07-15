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

    public boolean jumpToDefaultWhenFinished = true;
    public String defaultAnimation = "";

    public Vector2 size_in_meters = new Vector2(1,1);

    private float stateTime = 0;

    public void addAnimation(String name, Texture sheet, int cols, int rows){addAnimation(name, sheet,cols,rows,0,0,cols,rows);}
    public void addAnimation(String name, Texture sheet, int numCols, int numRows, int colOffset, int rowOffset, int totCols, int totRows){
        addAnimation(name, sheet,numCols,numRows,colOffset, rowOffset,totCols,totRows, Animation.PlayMode.NORMAL);
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
        animations.put(name, new Animation(Settings.ANIMATION_FRAMES_PER_SECOND, Array.with(frames), mode));

    }

    public void setAnimation(String name){
        animation = animations.get(name);
        current = name;
    }

    public TextureRegion getKeyFrame(float deltaTime){
        stateTime += deltaTime;
        if(animation.isAnimationFinished(stateTime) && jumpToDefaultWhenFinished){
            Gdx.app.log("animation", "last " + current + " now " + defaultAnimation + " " + stateTime);
            current = defaultAnimation;
            animation = animations.get(current);
            stateTime = 0;
        }

        return animation.getKeyFrame(stateTime);
    }


}
