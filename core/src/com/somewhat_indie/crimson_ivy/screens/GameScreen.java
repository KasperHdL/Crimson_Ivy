package com.somewhat_indie.crimson_ivy.screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.Assets;
import com.somewhat_indie.crimson_ivy.GameWorld;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.systems.*;

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

    RayHandler rayHandler;

    public static World world;

    GameWorld gameWorld;

    Engine engine;

    public GameScreen(GdxGame game){
        this.game = game;

        state = Game_State.GAME_READY;

        guiCam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        guiCam.position.set(guiCam.viewportWidth/2,guiCam.viewportHeight/2,0);

        engine = new Engine();

        world = new World(Vector2.Zero,true);
        rayHandler = new RayHandler(world);

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        rayHandler.setAmbientLight(.06f, 0.03f, 0.03f, 1f);
        rayHandler.setBlurNum(3);

        gameWorld = new GameWorld(engine,world,rayHandler);

        engine.addSystem(new CameraSystem());

        KeyboardSystem k = new KeyboardSystem();
        Gdx.input.setInputProcessor(k);
        engine.addSystem(k);

        engine.addSystem(new PlayerSystem());

        engine.addSystem(new StateSystem());
        engine.addSystem(new SteeringSystem());
        engine.addSystem(new RenderSystem(game.batch,world,rayHandler));

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
        engine.update(deltaTime);
        world.step(deltaTime,6,2);
    }

    private void update_gamePaused(float deltaTime){

    }

    private void update_gameOver(float deltaTime){

    }

    public void drawUI () {
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.begin();

        Assets.font.draw(game.batch, state.name(), 5,Gdx.graphics.getHeight()-5);
        game.batch.end();

    }
    @Override
    public void render(float deltaTime){
        update(deltaTime);
        MessageManager.getInstance().update(deltaTime);
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


        RenderSystem.camera.translate(velX * deltaTime, velY * deltaTime);

    }

}