package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by kaholi on 7/6/15.
 */

public class BodyComp extends Component {

    public Body body;

    //Fixtures


    /**
     * Constructor for body component
     * @param world Box2d world
     * @param type Box2d type
     * @param shape Box2d shape (i.e. PolygonShape, CircleShape so on)
     * @param pos position to be placed
     * @param density density of object
     * @param restitution restitution of object
     */
    public BodyComp(World world, BodyDef.BodyType type, Shape shape, Vector2 pos, float density, float restitution){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);

    }


}
