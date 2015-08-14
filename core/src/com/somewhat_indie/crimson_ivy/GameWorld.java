package com.somewhat_indie.crimson_ivy;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.input.ControllerComp;

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

    private static Engine engine;

    private static World world;
    private static RayHandler rayHandler;

    public GameWorld(Engine engine,World world, RayHandler rayHandler){
        GameWorld.engine = engine;

        GameWorld.world = world;
        GameWorld.rayHandler = rayHandler;

        EntityFactory.world = world;
        EntityFactory.rayHandler = rayHandler;
    }

    public void create(){
        //Entities

        Vector2 pos = new Vector2(0, 0);

        Entity player;
        int num_controllers = Controllers.getControllers().size;

        if(num_controllers == 1) {
            player = EntityFactory.Player.create_player(pos, false);
            for (Controller controller : Controllers.getControllers())
                controller.addListener(player.getComponent(ControllerComp.class));
        }else
            player = EntityFactory.Player.create_player(pos, true);

        Entity camera = EntityFactory.create_camera(player);


        for(int i = 0;i<100;i++){
            engine.addEntity(
                    EntityFactory.create_wall(
                            new Vector2(i * 10, -5),
                            new Vector2(1, 1)
                    ));

            engine.addEntity(
                    EntityFactory.create_wall(
                            new Vector2(i * 10, 5),
                            new Vector2(1, 1)
                    ));
        }

        for (int i = 0; i < 10; i++) {
            engine.addEntity(
                    EntityFactory.Enemy.create_melee(
                            new Vector2(20 + i * 10, 0)
                    ));
        }


        engine.addEntity(player);
        engine.addEntity(camera);

        Gdx.app.log("Game World", "created");
    }

    public static void create(Entity entity){
        engine.addEntity(entity);
    }

    public static void destroy(Entity entity){
        Body body = entity.getComponent(BodyComp.class).body;
        if(body != null)
            world.destroyBody(body);

        engine.removeEntity(entity);

        //TODO might be leaking!?
    }
}
