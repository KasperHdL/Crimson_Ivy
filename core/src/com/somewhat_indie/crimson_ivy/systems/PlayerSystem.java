package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.somewhat_indie.crimson_ivy.EntityData;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.components.*;
import com.somewhat_indie.crimson_ivy.components.input.ControllerComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardMouseComp;
import com.somewhat_indie.crimson_ivy.components.items.WeaponComp;
import com.somewhat_indie.crimson_ivy.raycasts.RayCastClosestHitable;

/**
 * Created by kaholi on 7/6/15.
 */

public class PlayerSystem extends EntitySystem implements Telegraph{

    public static ImmutableArray<Entity> players;

    private ComponentMapper<PlayerComp>         playerMap       = ComponentMapper.getFor(PlayerComp.class);
    private ComponentMapper<AnimationComp>      animationMap    = ComponentMapper.getFor(AnimationComp.class);
    private ComponentMapper<WeaponComp>         weaponMap       = ComponentMapper.getFor(WeaponComp.class);
    private ComponentMapper<KeyboardMouseComp>  keyboardMap     = ComponentMapper.getFor(KeyboardMouseComp.class);
    private ComponentMapper<ControllerComp>     controllerMap   = ComponentMapper.getFor(ControllerComp.class);
    private ComponentMapper<BodyComp>           bodyMap         = ComponentMapper.getFor(BodyComp.class);


    public void addedToEngine(Engine engine){
        //noinspection unchecked
        players = engine.getEntitiesFor(Family.all(PlayerComp.class, BodyComp.class,AnimationComp.class).one(KeyboardMouseComp.class, ControllerComp.class).get());
    }

    public void update(float deltaTime){
        for(int i = 0;i< players.size();i++) {
            Entity entity = players.get(i);

            PlayerComp player = playerMap.get(entity);
            BodyComp bodyComp = bodyMap.get(entity);

            AnimationComp animation = animationMap.get(entity);

            if(!entity.getComponent(AgentComp.class).isAlive){
                entity.getComponent(LightComp.class).lights = null;
                animation.setAnimation("death");
                animation.jumpToDefaultWhenFinished = false;
                entity.remove(PlayerComp.class);

                //GameWorld.create(EntityFactory.Player.create_player_corpse(bodyComp.getPosition(), bodyComp.getOrientation()));
                //GameWorld.destroy(entity);

                continue;
            }



            //TODO refactor so controller & keyboard uses an abstract class or interface
            if (keyboardMap.has(entity)){
                KeyboardMouseComp keyboard = keyboardMap.get(entity);

                keyboard.handleMovement(bodyComp, deltaTime);
                keyboard.handleDirection(bodyComp.body);

                if(keyboard.attackDown && player.nextAllowedAttack < GdxGame.TIME){
                    attack(player,bodyComp,weaponMap.get(entity));
                }
            }else if(controllerMap.has(entity)){
                ControllerComp controller = controllerMap.get(entity);

                if(!animation.current.equals("attack")) {
                    if(controller.handleMovement(bodyComp, deltaTime)){
                        animation.setAnimation("walk",true);
                    }else
                        animation.setAnimation("idle", true);
                }

                controller.handleDirection(bodyComp.body);

                if(controller.attackDown && player.nextAllowedAttack < GdxGame.TIME){
                    animation.setAnimation("attack");
                }




            }
            if(player.attack())
                attack(player,bodyComp,weaponMap.get(entity));

            capMovement(bodyComp);

        }
    }

    private boolean attack(PlayerComp player, BodyComp bodyComp, WeaponComp weapon){

        player.nextAllowedAttack = GdxGame.TIME + weapon.attackDelay;

        Vector2 dir = new Vector2(1, 0).rotate(bodyComp.getOrientation() * MathUtils.radDeg);

        Vector2 pos = bodyComp.getPosition();
        RayCastClosestHitable raycast = new RayCastClosestHitable();
        bodyComp.body.getWorld().rayCast(
                raycast,
                pos.cpy(),
                pos.cpy().add(dir.cpy().scl(weapon.reach))
        );

        if(raycast.hit){
            EntityData data = (EntityData) raycast.body.getUserData();

            raycast.body.applyForceToCenter(dir.scl(4000f),true);

            switch (data.type){
                case Wall:
                    break;
                case Player:
                    break;
                case Enemy:
                    data.entity.getComponent(AgentComp.class).takeDamage(weapon.damage);
                    return true;
                case Corpse:
                    break;
                case Camera:
                    break;
            }

        }

        return false;
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

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

}
