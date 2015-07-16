package com.somewhat_indie.crimson_ivy.components.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

/**
 * Created by kaholi on 7/7/15.
 */
public class ControllerComp extends Component implements ControllerListener{

    public int horizontalMovement;
    public int verticalMovement;

    public int horizontalDirection;
    public int verticalDirection;

    public Vector2 movement = new Vector2();
    public Vector2 direction = new Vector2();

    public float stickDeadzone;

    public int attackButton;
    public boolean attackDown = false;



    public boolean handleMovement(BodyComp bodyComp,float deltaTime){

        if(movement.len() < stickDeadzone)
            return false;

        Vector2 force = movement.cpy().scl(bodyComp.getMaxLinearAcceleration());
        force.scl(deltaTime);
        bodyComp.body.applyForceToCenter(force, true);
        return !force.isZero();
    }

    public void handleDirection(Body body){

        if(direction.len() < stickDeadzone)
            return;

        body.setTransform(body.getPosition(), direction.angleRad());

    }


/////////////////////////////////////
//ControllerListener Implementation
/////////////////////////////////////

    @Override
    public void connected(Controller controller) {
        Gdx.app.log("Controller",controller.getName() + " connected");
    }

    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log("Controller",controller.getName() + " disconnected");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(buttonCode == attackButton){
            attackDown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if(buttonCode == attackButton){
            attackDown = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {

        boolean b = false;
        if(axisCode == horizontalMovement){
            movement.x = value;
            b = true;
        }else if(axisCode == verticalMovement) {
            movement.y  = -value;
            b = true;
        }else if(axisCode == horizontalDirection) {
            direction.x = value;
            b = true;
        }else if(axisCode == verticalDirection) {
            direction.y = -value;
            b = true;
        }
        if(b){
            return true;
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
