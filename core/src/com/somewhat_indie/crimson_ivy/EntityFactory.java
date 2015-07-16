package com.somewhat_indie.crimson_ivy;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.ai.AgentLimiter;
import com.somewhat_indie.crimson_ivy.ai.states.EnemyMeleeStates;
import com.somewhat_indie.crimson_ivy.components.*;
import com.somewhat_indie.crimson_ivy.components.input.*;
import com.somewhat_indie.crimson_ivy.components.input.mapping.linux.PS3;
import com.somewhat_indie.crimson_ivy.components.items.WeaponComp;
import com.somewhat_indie.crimson_ivy.systems.RenderSystem;

import java.util.HashMap;

/**
 * Created by kaholi on 7/6/15.
 */
public class EntityFactory {

    public static World world;
    public static RayHandler rayHandler;

    public static Entity create_camera(Entity target){
        Entity entity = new Entity();

        CameraComp camera = new CameraComp();
        camera.camera = RenderSystem.getCamera();
        camera.target = target;
        camera.drag = 0.4f;

        entity.add(camera);

        return entity;
    }

    public static class Player{
        public static Entity create_player(Vector2 pos,boolean useKeyboard) {return create_player(pos, useKeyboard, 0);}
        public static Entity create_player(Vector2 pos,boolean useKeyboard,float angle) {
            Entity entity = new Entity();

            Vector2 size = new Vector2(2f, 2f);

            entity.add(new AgentComp(50f));

            WeaponComp weapon = new WeaponComp();
            weapon.damage = 4f;
            weapon.reach = 5f;
            weapon.attackDelay = Settings.ANIMATION_FRAME_DURATION * 10;
            entity.add(weapon);

            PlayerComp player = new PlayerComp();
            entity.add(player);

            TransformComp transform = new TransformComp();
            entity.add(transform);

            if(useKeyboard) {
                KeyboardMouseComp input = new KeyboardMouseComp();
                input.leftKey = Input.Keys.A;
                input.rightKey = Input.Keys.D;
                input.upKey = Input.Keys.W;
                input.downKey = Input.Keys.S;
                input.attackKey = Input.Buttons.LEFT;
                entity.add(input);
            }else{
                ControllerComp input = new ControllerComp();

                //See crimson_ivy.components.input.mapping for OS mappings
                //the imported is used.. currently linux!

                input.horizontalMovement    = PS3.AXIS_LEFT_X;
                input.verticalMovement      = PS3.AXIS_LEFT_Y;
                input.horizontalDirection   = PS3.AXIS_RIGHT_X;
                input.verticalDirection     = PS3.AXIS_RIGHT_Y;
                input.stickDeadzone         = PS3.STICK_DEADZONE;

                input.attackButton = PS3.BUTTON_R1;

                entity.add(input);
            }

            AnimationComp animation = new AnimationComp();
            animation.size_in_meters = new Vector2(size.x,size.y);

            animation.addAnimation("idle",Assets.warrior,10,1,0,0,10,5);
            animation.addAnimation("gesture",Assets.warrior,10,1,0,1,10,5);
            animation.addAnimation("walk",Assets.warrior,10,1,0,2,10,5);
            animation.addAnimation("attack",Assets.warrior,10,1,0,3,10,5);
            animation.addAnimation("death",Assets.warrior,10,1,0,4,10,5);

            animation.addCallback("attack",5,player);

            animation.setDefaultAnimation("idle");
            animation.setAnimation("idle");
            entity.add(animation);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size.x / 2, size.y / 2);

            BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
            bodyComp.body.setTransform(pos, angle);
            bodyComp.body.setLinearDamping(10f);
            bodyComp.body.setFixedRotation(true);

            bodyComp.body.setUserData(new EntityData(EntityData.EntityType.Player, entity));
            entity.add(bodyComp);

            //movement variables
            bodyComp.setMaxLinearSpeed(80000f);
            bodyComp.setMaxLinearAcceleration(20000f);
            bodyComp.setIndependentFacing(true);


            Color lightColor = new Color(1, 1, 1, .7f);
            LightComp lightComp = new LightComp();
            lightComp.lights = new HashMap<>(4);

            PositionalLight light = new ConeLight(rayHandler, 80, lightColor, 32f, 0, 0, 0, 80);
            light.attachToBody(bodyComp.body);
            lightComp.lights.put("frontCone", light);

            lightColor = new Color(1, 1, 1, .2f);

            light = new ConeLight(rayHandler, 20, lightColor, 24f, 0, 0, 0, 20);
            light.attachToBody(bodyComp.body, 0, 0, -100);
            lightComp.lights.put("leftCone", light);

            light = new ConeLight(rayHandler, 20, lightColor, 24f, 0, 0, 0, 20);
            light.attachToBody(bodyComp.body, 0, 0, 100);
            lightComp.lights.put("rightCone", light);

            light = new ConeLight(rayHandler, 20, lightColor, 8f, 0, 0, 0, 60);
            light.attachToBody(bodyComp.body, 0, 0, 180);
            lightComp.lights.put("rightCone", light);

            entity.add(lightComp);

            shape.dispose();
            return entity;
        }
        public static Entity create_player_corpse(Vector2 pos){return create_player_corpse(pos, 0);}
        public static Entity create_player_corpse(Vector2 pos, float angle) {
            Entity entity = new Entity();

            Vector2 size = new Vector2(1.2f, 1f);

            TransformComp transform = new TransformComp();
            entity.add(transform);

            TextureComp texture = new TextureComp(0xbadaff88);
            texture.region.setRegionWidth((int) (Settings.METER_TO_PIXEL * size.x));
            texture.region.setRegionHeight((int) (Settings.METER_TO_PIXEL * size.y));
            entity.add(texture);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size.x / 2, size.y / 2);

            BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
            bodyComp.body.setTransform(pos, angle);
            bodyComp.body.setLinearDamping(1f);
            bodyComp.body.setAngularDamping(1f);
            bodyComp.body.setUserData(new EntityData(EntityData.EntityType.Corpse, entity));
            entity.add(bodyComp);

            shape.dispose();
            return entity;
        }

    }
    public static class Enemy{
        public static Entity create_melee(Vector2 pos){return create_melee(pos, 0);}
        public static Entity create_melee(Vector2 pos,float angle) {
            Entity entity = new Entity();

            Vector2 size = new Vector2(1f, 1f);

            entity.add(new AgentComp(10f));

            WeaponComp weapon = new WeaponComp();
            weapon.damage = 1f;
            weapon.reach = 3f;
            weapon.attackDelay = .3f;
            entity.add(weapon);

            TransformComp transform = new TransformComp();
            entity.add(transform);

            TextureComp texture = new TextureComp(0xff6611ff);
            texture.region.setRegionWidth((int) (Settings.METER_TO_PIXEL * size.x));
            texture.region.setRegionHeight((int) (Settings.METER_TO_PIXEL * size.y));
            entity.add(texture);

            //box2d body

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size.x / 2, size.y / 2);

            BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
            bodyComp.body.setTransform(pos,angle);
            bodyComp.body.setLinearDamping(10f);
            bodyComp.body.setAngularDamping(10f);
            bodyComp.body.setUserData(new EntityData(EntityData.EntityType.Enemy, entity));
            entity.add(bodyComp);

            //lights

            Color lightColor = new Color(1, .3f, 0, .04f);
            LightComp lightComp = new LightComp();
            lightComp.lights = new HashMap<>(1);

            PositionalLight light = new PointLight(rayHandler, 360, lightColor, 12f, 0, 0);
            light.attachToBody(bodyComp.body);
            lightComp.lights.put("centerPoint", light);

            entity.add(lightComp);

            //ai

            AIComp ai = new AIComp(entity, EnemyMeleeStates.WANDER, EnemyMeleeStates.GLOBAL_STATE);

            //steering
            ai.steeringBehaviors = new HashMap<>();
            AgentLimiter limiter = new AgentLimiter();

            Seek<Vector2> seek = new Seek<>(bodyComp);

            limiter.setMaxLinearSpeed(50000f);
            limiter.setMaxLinearAcceleration(10000f);

            limiter.setMaxAngularSpeed(50f);
            limiter.setMaxAngularAcceleration(100f);
            seek.setLimiter(limiter);
            ai.steeringBehaviors.put("seek", seek);

            Wander<Vector2> wander = new Wander<>(bodyComp);
            wander.setWanderRadius(10f);
            wander.setWanderOffset(20f);
            wander.setWanderRate(1);

            limiter = new AgentLimiter();
            limiter.setMaxLinearSpeed(5000f);
            limiter.setMaxLinearAcceleration(1000f);

            limiter.setMaxAngularSpeed(50f);
            limiter.setMaxAngularAcceleration(100f);
            wander.setLimiter(limiter);

            ai.steeringBehaviors.put("wander", wander);

            bodyComp.setSteeringBehavior(wander);
            entity.add(ai);

            shape.dispose();
            return entity;
        }
        public static Entity create_melee_corpse(Vector2 pos){return create_melee_corpse(pos, 0);}
        public static Entity create_melee_corpse(Vector2 pos,float angle) {
            Entity entity = new Entity();

            Vector2 size = new Vector2(1f, 1f);

            TransformComp transform = new TransformComp();
            entity.add(transform);

            TextureComp texture = new TextureComp(0xaa661188);
            texture.region.setRegionWidth((int) (Settings.METER_TO_PIXEL * size.x));
            texture.region.setRegionHeight((int) (Settings.METER_TO_PIXEL * size.y));
            entity.add(texture);

            //box2d body

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(size.x / 2, size.y / 2);

            BodyComp bodyComp = new BodyComp(world, BodyDef.BodyType.DynamicBody, shape, pos, 1f, 1f);
            bodyComp.body.setTransform(pos,angle);
            bodyComp.body.setLinearDamping(10f);
            bodyComp.body.setAngularDamping(10f);
            bodyComp.body.setUserData(new EntityData(EntityData.EntityType.Corpse, entity));
            entity.add(bodyComp);

            shape.dispose();
            return entity;
        }

    }




    public static Entity create_wall(Vector2 pos, Vector2 size) {

        Entity entity = new Entity();

        TransformComp transform = new TransformComp();
        entity.add(transform);

        TextureComp texture = new TextureComp(0xffda55ff);
        texture.region.setRegionWidth((int) (Settings.METER_TO_PIXEL * size.x));
        texture.region.setRegionHeight((int) (Settings.METER_TO_PIXEL * size.y));

        entity.add(texture);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        BodyComp body = new BodyComp(world, BodyDef.BodyType.StaticBody, shape, pos, 1f, 0f);
        body.body.setUserData(new EntityData(EntityData.EntityType.Wall, entity));
        entity.add(body);
        shape.dispose();

        return entity;
    }


}
