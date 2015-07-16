package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.somewhat_indie.crimson_ivy.components.AnimationComp;
import com.somewhat_indie.crimson_ivy.components.CameraComp;

/**
 * Created by kaholi on 7/16/15.
 */
public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationComp> animationMap = ComponentMapper.getFor(AnimationComp.class);

    public AnimationSystem() {
        //noinspection unchecked
        super(Family.all(AnimationComp.class).get());

        animationMap = ComponentMapper.getFor(AnimationComp.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
       animationMap.get(entity).update(deltaTime);
    }
}
