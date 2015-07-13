package com.somewhat_indie.crimson_ivy;

import com.badlogic.ashley.core.Entity;

/**
 * Created by kaholi on 7/13/15.
 */
public class EntityData{
    public enum EntityType {
        Wall,
        Player,
        Enemy,
        Corpse,
        Camera
    }
    public EntityType type;
    public Entity entity;
    public EntityData(EntityType type, Entity entity){this.type = type;this.entity = entity;}
}