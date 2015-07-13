package com.somewhat_indie.crimson_ivy.components.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

/**
 * Created by kaholi on 7/7/15.
 */
public class KeyboardMouseComp extends Component{
    public int leftKey;
    public int rightKey;
    public int upKey;
    public int downKey;

    public int attackKey;

    public boolean leftDown = false;
    public boolean rightDown = false;
    public boolean upDown = false;
    public boolean downDown = false;

    public boolean attackDown = false;



    public void handleMovement(BodyComp bodyComp, KeyboardMouseComp input,float deltaTime){
        float x = 0;
        float y = 0;

        Body body = bodyComp.body;

        if (input.rightDown) {
            x = 1;
        } else if (input.leftDown) {
            x = -1;
        }

        if (input.upDown) {
            y = 1;
        } else if (input.downDown) {
            y = -1;
        }

        Vector2 force = new Vector2(x,y).nor().scl(bodyComp.getMaxLinearAcceleration());
        force.scl(deltaTime);
        body.applyForceToCenter(force, true);

    }

    public void handleDirection(Body body){
        Vector3 mouse = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);

        Vector2 delta;
        {
            Vector3 v = RenderSystem.getCamera().unproject(mouse);
            Vector2 wMouse = new Vector2(v.x, v.y);
            delta = wMouse.sub(body.getPosition());
        }


        float angle = MathUtils.degRad * delta.angle();

        body.setTransform(body.getPosition(), angle);
    }
}
