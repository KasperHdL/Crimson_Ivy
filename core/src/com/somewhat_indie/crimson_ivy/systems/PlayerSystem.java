package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.components.AgentComp;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.PlayerComp;
import com.somewhat_indie.crimson_ivy.components.input.ControllerComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardMouseComp;
import com.somewhat_indie.crimson_ivy.components.items.WeaponComp;

/**
 * Created by kaholi on 7/6/15.
 */

public class PlayerSystem extends EntitySystem{

    private ImmutableArray<Entity> players;

    private ComponentMapper<PlayerComp>         playerMap   = ComponentMapper.getFor(PlayerComp.class);
    private ComponentMapper<AgentComp>          agentMap    = ComponentMapper.getFor(AgentComp.class);
    private ComponentMapper<WeaponComp>         weaponMap   = ComponentMapper.getFor(WeaponComp.class);
    private ComponentMapper<KeyboardMouseComp>  keyboardMap = ComponentMapper.getFor(KeyboardMouseComp.class);
    private ComponentMapper<BodyComp>           bodyMap     = ComponentMapper.getFor(BodyComp.class);

    private Engine engine;

    public void addedToEngine(Engine engine){
        //noinspection unchecked
        players = engine.getEntitiesFor(Family.all(PlayerComp.class, AgentComp.class, BodyComp.class).one(KeyboardMouseComp.class, ControllerComp.class).get());

        this.engine = engine;
    }

    public void update(float deltaTime){

        for(int i = 0;i< players.size();i++) {
            Entity entity = players.get(i);

            PlayerComp player = playerMap.get(entity);
            AgentComp agent = agentMap.get(entity);
            Body body = bodyMap.get(entity).body;


            if (player.usingKeyboardMouse){
                KeyboardMouseComp input = keyboardMap.get(entity);

                handleMovementKeyboardMouse(agent, body, input);
                handleDirectionKeyboardMouse(body);

                if(input.attackDown){

                }
            }

        }
    }

    private void handleMovementKeyboardMouse(AgentComp agent, Body body, KeyboardMouseComp input){
        float x = 0;
        float y = 0;

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

        Vector2 force = new Vector2(x,y).nor().scl(agent.force);

        body.applyForceToCenter(force, true);
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
}
