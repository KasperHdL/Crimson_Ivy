package com.somewhat_indie.crimson_ivy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by kaholi on 7/6/15.
 */
public class Assets {

    public static Texture warrior;
    public static String[] warrior_animation_names = new String[]{
            "idle",
            "gesture",
            "walk",
            "attack",
            "death"
    };



    public static BitmapFont font;

    public static Texture loadTexture(String file){
        return new Texture(Gdx.files.internal(file));
    }

    public static void load(){

        //Textures
        Texture tex = loadTexture("warrior.png");
        warrior = new TextureRegion(tex,0,0,320,320).getTexture();

        //Animations

        //Music

        //SFX

        //Font
        font = new BitmapFont();
    }
}
