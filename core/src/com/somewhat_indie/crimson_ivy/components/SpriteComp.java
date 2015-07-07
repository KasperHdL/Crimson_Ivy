package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kaholi on 7/7/15.
 */


public class SpriteComp extends Component {
    public Sprite sprite;

    /**
     * Create SpriteComponent
     * @param color color of sprite
     * @param width width of sprite
     * @param height height of sprite
     */
    public SpriteComp(int color,int width, int height){
        Pixmap pix = new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();

        sprite = new Sprite(new Texture(pix),width,height);
    }

//--Constructor overrides

    /**
     * Create SpriteComponent
     * @param color color of sprite
     * @param size size of sprite
     */
    public SpriteComp(int color,Vector2 size){
        this(color,size.x,size.y);
    }

    /**
     * Create SpriteComponent
     * @param color color of sprite
     * @param width width of sprite
     * @param height height of sprite
     */
    public SpriteComp(int color, float width, float height){
        this(color,(int)width,(int)height);
    }
}
