package com.somewhat_indie.crimson_ivy;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.ai.EnemyMeleeStates;
import com.somewhat_indie.crimson_ivy.components.*;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardMouseComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

import java.util.HashMap;

/**
 * Created by kaholi on 7/6/15.
 */
public class EntityFactory {

    public static World world;
    public static RayHandler rayHandler;

    public static Entity create_player(Vector2 pos) {
        Entity entity = new Entity();

        Vector2 size = new Vector2(1.2f, 1f);

        entity.add(new AgentComp());

        PlayerComp player = new PlayerComp();
        entity.add(player);

        TransformComp transform = new TransformComp();
        entity.add(transform);

        KeyboardMouseComp input = new KeyboardMouseComp();
        input.leftKey = Input.Keys.A;
        input.rightKey = Input.Keys.D;
        input.upKey = Input.Keys.W;
        input.downKey= Input.Keys.S ;
        input.attackKey = Input.Buttons.LEFT;
        entity.add(input);

        TextureComp texture = new TextureComp(0xbadaffff);
        texture.region.setRegionWidth((int) (Settings.meterToPixel * size.x));
        texture.region.setRegionHeight((int) (Settings.meterToPixel * size.y));
        entity.add(texture);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
        bodyComp.body.setLinearDamping(10f);
        entity.add(bodyComp);


        Color lightColor = new Color(1,1,1,.7f);
        LightComp lightComp = new LightComp();
        lightComp.lights = new HashMap<>(4);

        PositionalLight light = new ConeLight(rayHandler,80,lightColor,32f,0,0,0,80);
        light.attachToBody(bodyComp.body);
        lightComp.lights.put("frontCone", light);

        lightColor = new Color(1,1,1,.2f);

        light = new ConeLight(rayHandler,20,lightColor,24f,0,0,0,20);
        light.attachToBody(bodyComp.body, 0, 0, -100);
        lightComp.lights.put("leftCone", light);

        light = new ConeLight(rayHandler,20,lightColor,24f,0,0,0,20);
        light.attachToBody(bodyComp.body, 0, 0, 100);
        lightComp.lights.put("rightCone", light);

        light = new ConeLight(rayHandler,20,lightColor,8f,0,0,0,60);
        light.attachToBody(bodyComp.body, 0, 0, 180);
        lightComp.lights.put("rightCone", light);

        entity.add(lightComp);

        shape.dispose();
        return entity;
    }

    public static Entity create_enemy_melee(Vector2 pos){
        Entity entity = new Entity();

        Vector2 size = new Vector2(1f, 1f);

        AgentComp agent = new AgentComp();
        entity.add(agent);

        TransformComp transform = new TransformComp();
        entity.add(transform);

        TextureComp texture = new TextureComp(0xff6611ff);
        texture.region.setRegionWidth((int) (Settings.meterToPixel * size.x));
        texture.region.setRegionHeight((int) (Settings.meterToPixel * size.y));
        entity.add(texture);

        //box2d body

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
        bodyComp.body.setLinearDamping(10f);
        bodyComp.body.setAngularDamping(10f);
        entity.add(bodyComp);

        //lights

        Color lightColor = new Color(1,.3f,0,.04f);
        LightComp lightComp = new LightComp();
        lightComp.lights = new HashMap<>(1);

        PositionalLight light = new PointLight(rayHandler,360,lightColor,12f,0,0);
        light.attachToBody(bodyComp.body);
        lightComp.lights.put("centerPoint",light);

        entity.add(lightComp);

        //ai

        AIComp ai = new AIComp(entity, EnemyMeleeStates.IDLE,EnemyMeleeStates.GLOBAL_STATE);
        entity.add(ai);

        shape.dispose();
        return entity;
    }

    public static Entity create_box_filled(Vector2 pos, Vector2 size){

        Entity entity = new Entity();

        TransformComp transform = new TransformComp();
        entity.add(transform);

        TextureComp texture = new TextureComp(0xffda55ff);
        texture.region.setRegionWidth((int) (Settings.meterToPixel * size.x));
        texture.region.setRegionHeight((int) (Settings.meterToPixel * size.y));

        entity.add(texture);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        BodyComp body = new BodyComp(world, BodyDef.BodyType.StaticBody, shape, pos, 1f, 0f);
        entity.add(body);
        shape.dispose();

        return entity;
    }


    public static Entity create_camera(Entity target){
        Entity entity = new Entity();

        CameraComp camera = new CameraComp();
        camera.camera = RenderSystem.getCamera();
        camera.target = target;
        camera.drag = 0.4f;

        entity.add(camera);

        return entity;
    }
}
