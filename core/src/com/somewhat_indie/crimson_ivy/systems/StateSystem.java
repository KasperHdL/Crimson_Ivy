package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.somewhat_indie.crimson_ivy.components.AIComp;

/**
 * Created by kaholi on 7/10/15.
 */
public class StateSystem extends EntitySystem {


    private ImmutableArray<Entity> entities;

    private ComponentMapper<AIComp> aiMap   = ComponentMapper.getFor(AIComp.class);


    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(AIComp.class).get());
    }

    public void update(float deltaTime){
        for(int i = 0;i< entities.size();i++)
            aiMap.get(entities.get(i)).update();
    }
}
