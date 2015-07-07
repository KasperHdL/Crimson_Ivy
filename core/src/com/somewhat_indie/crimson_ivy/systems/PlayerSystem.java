package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.PlayerComp;
import com.somewhat_indie.crimson_ivy.components.input.ControllerComp;
import com.somewhat_indie.crimson_ivy.components.input.InputComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardComp;

/**
 * Created by kaholi on 7/6/15.
 */

public class PlayerSystem extends EntitySystem{

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PlayerComp> playerMap = ComponentMapper.getFor(PlayerComp.class);
    private ComponentMapper<KeyboardComp> keyboardMap = ComponentMapper.getFor(KeyboardComp.class);
    private ComponentMapper<BodyComp> bodyMap = ComponentMapper.getFor(BodyComp.class);

    private Engine engine;

    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(PlayerComp.class, BodyComp.class).one(InputComp.class,KeyboardComp.class, ControllerComp.class).get());

        this.engine = engine;
    }

    public void update(float deltaTime){

        for(int i = 0;i<entities.size();i++) {
            Entity entity = entities.get(i);

            PlayerComp player = playerMap.get(entity);
            Body body = bodyMap.get(entity).body;

            float inputX = 0;
            float inputY = 0;

            if (player.usingKeyboard){
                KeyboardComp keyboard = keyboardMap.get(entity);

                if (keyboard.rightDown) {
                    inputX = 1;
                } else if (keyboard.leftDown) {
                    inputX = -1;
                }

                if (keyboard.upDown) {
                    inputY = 1;
                } else if (keyboard.downDown) {
                    inputY = -1;
                }

                Vector2 force = new Vector2(inputX,inputY).nor().scl(player.force);

                body.applyForceToCenter(force, true);

                Vector3 mouse = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);

                Vector2 delta;
                {
                    Vector3 v = RenderSystem.getCamera().unproject(mouse);
                    Vector2 wMouse = new Vector2(v.x, v.y);
                    delta = wMouse.sub(body.getPosition());
                }


                float angle = MathUtils.degRad * delta.angle();

                body.setTransform(body.getPosition(),angle);

            }





        }
    }
}
