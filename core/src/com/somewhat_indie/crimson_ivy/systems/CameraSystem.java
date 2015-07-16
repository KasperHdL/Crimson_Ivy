package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.CameraComp;

/**
 * Created by kaholi on 7/6/15.
 */


public class CameraSystem extends IteratingSystem {
    private ComponentMapper<CameraComp> cameraMap = ComponentMapper.getFor(CameraComp.class);
    private ComponentMapper<BodyComp> bodyMap = ComponentMapper.getFor(BodyComp.class);

    public CameraSystem() {
            //noinspection unchecked
        super(Family.all(CameraComp.class).get());

        cameraMap = ComponentMapper.getFor(CameraComp.class);
        bodyMap = ComponentMapper.getFor(BodyComp.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComp cam = cameraMap.get(entity);

        if (cam.target == null) {
            return;
        }
        Body target;
        try {
             target = bodyMap.get(cam.target).body;
        }catch (NullPointerException e){
            return;
        }

        Vector2 camPos = new Vector2(cam.camera.position.x,cam.camera.position.y);
        Vector2 diff = target.getPosition().sub(camPos);
        cam.camera.position.x = camPos.x + (diff.x * cam.drag);
        cam.camera.position.y = camPos.y + (diff.y * cam.drag);
    }
}