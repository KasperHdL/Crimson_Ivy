package com.somewhat_indie.crimson_ivy.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.EntityFactory;
import com.somewhat_indie.crimson_ivy.components.AgentComp;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.LightComp;
import com.somewhat_indie.crimson_ivy.components.PlayerComp;
import com.somewhat_indie.crimson_ivy.components.input.ControllerComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardMouseComp;
import com.somewhat_indie.crimson_ivy.components.items.WeaponComp;
import com.somewhat_indie.crimson_ivy.screens.GameScreen;

/**
 * Created by kaholi on 7/6/15.
 */

public class PlayerSystem extends EntitySystem implements Telegraph{

    public static ImmutableArray<Entity> players;

    private ComponentMapper<PlayerComp>         playerMap   = ComponentMapper.getFor(PlayerComp.class);
    private ComponentMapper<WeaponComp>         weaponMap   = ComponentMapper.getFor(WeaponComp.class);
    private ComponentMapper<KeyboardMouseComp>  keyboardMap = ComponentMapper.getFor(KeyboardMouseComp.class);
    private ComponentMapper<BodyComp>           bodyMap     = ComponentMapper.getFor(BodyComp.class);

    private Engine engine;

    public void addedToEngine(Engine engine){
        //noinspection unchecked
        players = engine.getEntitiesFor(Family.all(PlayerComp.class, BodyComp.class).one(KeyboardMouseComp.class, ControllerComp.class).get());
        this.engine = engine;
    }

    public void update(float deltaTime){
        for(int i = 0;i< players.size();i++) {
            Entity entity = players.get(i);

            PlayerComp player = playerMap.get(entity);
            BodyComp bodyComp = bodyMap.get(entity);

            if(entity.getComponent(AgentComp.class).isAlive == false){
                entity.getComponent(LightComp.class).lights = null;

                engine.addEntity(EntityFactory.Player.create_corpse(bodyComp.getPosition()));
                engine.removeEntity(entity);
                GameScreen.world.destroyBody(bodyComp.body);

                //TODO make sure this does not leak

                continue;
            }

            if (player.usingKeyboardMouse){
                KeyboardMouseComp input = keyboardMap.get(entity);

                handleMovementKeyboardMouse(bodyComp, input, deltaTime);
                handleDirectionKeyboardMouse(bodyComp.body);

                capMovement(bodyComp);

                if(input.attackDown){
                    //TODO check for enemies and attack them
                }
            }

        }
    }

    private void handleMovementKeyboardMouse(BodyComp bodyComp, KeyboardMouseComp input,float deltaTime){
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

    private void capMovement(BodyComp bodyComp){
        Body body = bodyComp.body;
        // Cap the linear speed
        Vector2 velocity = body.getLinearVelocity();
        float currentSpeedSquare = velocity.len2();
        float maxLinearSpeed = bodyComp.getMaxLinearSpeed();
        if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
            body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
        }

        /*
        // Cap the angular speed --currently just setting the angle
        float maxAngVelocity = bodyComp.getMaxAngularSpeed();
        if (body.getAngularVelocity() > maxAngVelocity) {
            body.setAngularVelocity(maxAngVelocity);
        }
        */
    }

    private void handleDirectionKeyboardMouse(Body body){
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

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
