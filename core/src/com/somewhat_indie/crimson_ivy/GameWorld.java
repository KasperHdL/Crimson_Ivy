package com.somewhat_indie.crimson_ivy;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by kaholi on 7/6/15.
 */
public class GameWorld {

    public enum World_State{
        RUNNING,
        NEXT_LEVEL,
        GAME_OVER
    }

    public World_State state;

    private Engine engine;

    public GameWorld(Engine engine,World world, RayHandler rayHandler){
        this.engine = engine;

        EntityFactory.world = world;
        EntityFactory.rayHandler = rayHandler;
    }

    public void create(){
        //Entities

        Vector2 pos = new Vector2(0, 0);

        Entity player = EntityFactory.Player.create_keyboard(pos);

        Entity camera = EntityFactory.create_camera(player);


        for(int i = 0;i<100;i++){
            engine.addEntity(
                    EntityFactory.create_box_filled(
                            new Vector2(i * 10, -5),
                            new Vector2(1, 1)
                    ));

            engine.addEntity(
                    EntityFactory.create_box_filled(
                            new Vector2(i * 10, 5),
                            new Vector2(1, 1)
                    ));
        }

        for (int i = 0; i < 10; i++) {
            engine.addEntity(
                    EntityFactory.Enemy.create_melee(
                            new Vector2(i * 10, 0),
                            player
                    ));
        }


        engine.addEntity(player);
        engine.addEntity(camera);

        Gdx.app.log("Game World", "created");
    }
}
