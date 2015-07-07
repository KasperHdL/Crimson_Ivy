package com.somewhat_indie.crimson_ivy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.components.*;
import com.somewhat_indie.crimson_ivy.components.input.InputComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

/**
 * Created by kaholi on 7/6/15.
 */
public class EntityFactory {

    public static Entity create_player(World world, Vector2 pos) {
        Entity entity = new Entity();

        Vector2 size = new Vector2(1, 1f);

        PlayerComp player = new PlayerComp();
        entity.add(player);

        TransformComp transform = new TransformComp();
        entity.add(transform);

        KeyboardComp input = new KeyboardComp();
        input.leftKey = Input.Keys.LEFT;
        input.rightKey = Input.Keys.RIGHT;
        input.upKey = Input.Keys.UP;
        input.downKey= Input.Keys.DOWN;
        entity.add(input);

        TextureComp texture = new TextureComp(0xbadaffff);
        texture.region.setRegionWidth((int) (Settings.meterToPixel * size.x));
        texture.region.setRegionHeight((int)(Settings.meterToPixel *  size.y));
        entity.add(texture);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, .4f, .1f);
        bodyComp.body.setLinearDamping(0.8f);
        entity.add(bodyComp);

        shape.dispose();
        return entity;

    }

    public static Entity create_box_filled(World world, Vector2 pos, Vector2 size){

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


    public static Entity create_camera(RenderSystem renderSystem, Entity target){
        Entity entity = new Entity();

        CameraComp camera = new CameraComp();
        camera.camera = renderSystem.getCamera();
        camera.target = target;
        camera.drag = 0.4f;

        entity.add(camera);

        return entity;
    }
}
