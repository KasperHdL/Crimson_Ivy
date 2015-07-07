package com.somewhat_indie.crimson_ivy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by kaholi on 7/6/15.
 */
public class Assets {

    public static Texture background;

    public static TextureRegion bg_mainmenu;

    public static BitmapFont font;

    public static Texture loadTexture(String file){
        return new Texture(Gdx.files.internal(file));
    }

    public static void load(){

        //Textures
        background = loadTexture("badlogic.jpg");
        bg_mainmenu = new TextureRegion(background,0,0,1,1);

        //Animations

        //Music

        //SFX

        //Font
        font = new BitmapFont();
    }
}
