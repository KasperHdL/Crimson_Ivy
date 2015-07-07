package com.somewhat_indie.crimson_ivy.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.Assets;
import com.somewhat_indie.crimson_ivy.GameWorld;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.Settings;
import com.somewhat_indie.crimson_ivy.systems.CameraSystem;
import com.somewhat_indie.crimson_ivy.systems.KeyboardSystem;
import com.somewhat_indie.crimson_ivy.systems.PlayerSystem;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

/**
 * Created by kaholi on 7/3/15.
 */
public class GameScreen extends ScreenAdapter {

    public enum Game_State{
        GAME_READY,
        GAME_RUNNING,
        GAME_PAUSED,
        GAME_OVER
    }

    private Game_State state;

    GdxGame game;

    OrthographicCamera guiCam;


    World world;

    GameWorld gameWorld;

    Engine engine;

    public GameScreen(GdxGame game){
        this.game = game;

        state = Game_State.GAME_READY;

        guiCam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        guiCam.position.set(guiCam.viewportWidth/2,guiCam.viewportHeight/2,0);

        engine = new Engine();
        world = new World(new Vector2(0,0),true);

        gameWorld = new GameWorld(engine,world);

        engine.addSystem(new CameraSystem());

        KeyboardSystem k = new KeyboardSystem();
        Gdx.input.setInputProcessor(k);
        engine.addSystem(k);

        engine.addSystem(new PlayerSystem());

        engine.addSystem(new RenderSystem(game.batch));

        gameWorld.create();

        state = Game_State.GAME_RUNNING;

    }

    public void update(float deltaTime){

        switch(state){
            case GAME_READY:
                update_gameReady(deltaTime);
                break;
            case GAME_RUNNING:
                update_gameRunning(deltaTime);
                break;
            case GAME_PAUSED:
                update_gamePaused(deltaTime);
                break;
            case GAME_OVER:
                update_gameOver(deltaTime);
                break;
        }
    }

    private void update_gameReady(float deltaTime){

    }

    private void update_gameRunning(float deltaTime){

        world.step(deltaTime,6,2);
        engine.update(deltaTime);
    }

    private void update_gamePaused(float deltaTime){

    }

    private void update_gameOver(float deltaTime){

    }

    public void drawUI () {
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.begin();

        Assets.font.draw(game.batch, "Game Running", Gdx.graphics.getWidth() / 2, 20);
        game.batch.end();

    }
    @Override
    public void render(float deltaTime){
        update(deltaTime);
        drawUI();
    }


    private void moveCamera(float deltaTime){
        //camera control

        float velX = 0;
        float velY = 0;
        float speed = 10;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            velX = -speed;
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            velX = speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            velY = -speed;
        }else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            velY = speed;
        }


        engine.getSystem(RenderSystem.class).camera.translate(velX * deltaTime, velY * deltaTime);

    }

}