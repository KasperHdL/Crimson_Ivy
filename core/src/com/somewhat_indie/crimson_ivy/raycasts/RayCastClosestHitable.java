package com.somewhat_indie.crimson_ivy.raycasts;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.somewhat_indie.crimson_ivy.EntityData;
import com.somewhat_indie.crimson_ivy.EntityFactory;

/**
 * Created by kaholi on 7/13/15.
 */
public class RayCastClosestHitable implements RayCastCallback {
    public boolean hit;
    public Body body;
    public Vector2 point;

    public RayCastClosestHitable(){hit = false;}

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        body = fixture.getBody();

        EntityData data = (EntityData) body.getUserData();

        switch (data.type){
            case Corpse:
                return 1;
            case Camera:
                return 1;
        }

        this.point = point;
        hit = true;
        return fraction;
    }
}
