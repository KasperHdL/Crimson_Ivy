package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.somewhat_indie.crimson_ivy.components.BodyComp;

/**
 * Created by kaholi on 7/11/15.
 */
public class SteeringSystem extends EntitySystem {


    private ImmutableArray<Entity> entities;

    private ComponentMapper<BodyComp> bodyMap   = ComponentMapper.getFor(BodyComp.class);


    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(BodyComp.class).get());
    }

    public void update(float deltaTime){
        for(int i = 0;i< entities.size();i++)
            bodyMap.get(entities.get(i)).update(deltaTime);
    }
}
