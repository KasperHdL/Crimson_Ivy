package com.somewhat_indie.crimson_ivy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.Settings;

/**
 * Created by kaholi on 7/3/15.
 *
 */
public class MainMenuScreen extends ScreenAdapter {
    GdxGame game;

    OrthographicCamera guiCam;

    public MainMenuScreen(GdxGame game){
        this.game = game;

        guiCam = new OrthographicCamera(Settings.SCREEN_WIDTH,Settings.SCREEN_HEIGHT);
        guiCam.position.set(Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2, 0);

    }

    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.setState(GdxGame.ScreenState.Game);
    }

    public void draw(){
        GL20 gl = Gdx.gl;
        gl.glClearColor(.1f, .1f, .1f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);

    }

    @Override
    public void render(float delta){
        update();
        draw();
    }
}
