package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by kaholi on 7/6/15.
 */

public class BodyComp extends Component implements Steerable<Vector2> {

    public Body body;

    //////////////////
    //GDX AI Varialbes

    public BodyComp target = null;

    float boundingRadius = 10f;
    boolean tagged = false;

    float maxLinearSpeed = 100;
    float maxLinearAcceleration = 100f;
    float maxAngularSpeed = 100f;
    float maxAngularAcceleration = 100f;

    boolean independentFacing = false;

    protected SteeringBehavior<Vector2> steeringBehavior;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


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


    /////////////////////////////////
    //GDX AI Steerable methods

    public boolean isIndependentFacing () {
        return independentFacing;
    }

    public void setIndependentFacing (boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    @Override
    public Vector2 getPosition () {
        return body.getPosition();
    }

    @Override
    public float getOrientation () {
        return body.getAngle();
    }

    @Override
    public Vector2 getLinearVelocity () {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity () {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius () {
        return boundingRadius;
    }

    @Override
    public boolean isTagged () {
        return tagged;
    }

    @Override
    public void setTagged (boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return MathUtils.degRad * vector.angle();
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        return new Vector2(1,0).rotate(MathUtils.radDeg * angle);
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public void update (float deltaTime) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

			/*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 *
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(steeringOutput, deltaTime);
        }
    }

    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            Vector2 force = steeringOutput.linear.scl(deltaTime);
            body.applyForceToCenter(force, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                body.applyTorque(steeringOutput.angular * deltaTime, true);
                anyAccelerations = true;
            }
        }
        else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(0.001f)) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        if (anyAccelerations) {
            // body.activate();

            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
            }

            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

    //
    // Limiter implementation
    //

    @Override
    public float getMaxLinearSpeed () {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed (float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration () {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration (float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed () {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed (float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration () {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration (float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }


}