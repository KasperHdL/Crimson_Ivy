package com.somewhat_indie.crimson_ivy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.components.CameraComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

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

    private World world;
    private Engine engine;

    public GameWorld(Engine engine,World world){
        this.engine = engine;
        this.world = world;
    }

    public void create(){
        //Entities

        Vector2 pos = new Vector2(0, 0);

        Entity player = EntityFactory.create_player(world, pos);
        Entity camera = EntityFactory.create_camera(player);


        for(int i = 0;i<100;i++){
            engine.addEntity(
                    EntityFactory.create_box_filled(
                            world,
                            new Vector2(i * 10, -5),
                            new Vector2(1, 1)
                    ));

            engine.addEntity(
                    EntityFactory.create_box_filled(
                            world,
                            new Vector2(i * 10, 5),
                            new Vector2(1, 1)
                    ));
        }


        engine.addEntity(player);
        engine.addEntity(camera);

        Gdx.app.log("Game World", "created");
    }

    public World getWorld(){
        return world;
    }



}
