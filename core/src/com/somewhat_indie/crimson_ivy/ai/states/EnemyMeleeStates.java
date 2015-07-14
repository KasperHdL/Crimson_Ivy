package com.somewhat_indie.crimson_ivy.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.somewhat_indie.crimson_ivy.EntityFactory;
import com.somewhat_indie.crimson_ivy.GameWorld;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.components.AIComp;
import com.somewhat_indie.crimson_ivy.components.AgentComp;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.LightComp;
import com.somewhat_indie.crimson_ivy.components.items.WeaponComp;
import com.somewhat_indie.crimson_ivy.systems.PlayerSystem;

/**
 * Created by kaholi on 7/10/15.
 */
@SuppressWarnings("unchecked")
public enum EnemyMeleeStates implements State<Entity> {

    COMBAT(){
        @Override
        public void enter(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "entered combat");
            BodyComp body = entity.getComponent(BodyComp.class);
            Seek<Vector2> seek;
            seek = (Seek<Vector2>) entity.getComponent(AIComp.class).steeringBehaviors.get("seek");
            if(seek.getTarget() != null)
                body.setSteeringBehavior(seek);
            else
                entity.getComponent(AIComp.class).stateMachine.changeState(WANDER);

        }

        @Override
        public void update(Entity entity) {
            AIComp ai = entity.getComponent(AIComp.class);
            if(ai.target.getComponent(AgentComp.class).isAlive){

                Vector2 pos = entity.getComponent(BodyComp.class).getPosition();
                Vector2 playerPos = ai.target.getComponent(BodyComp.class).getPosition();

                float dist = playerPos.sub(pos).len();

                if(dist < entity.getComponent(WeaponComp.class).reach) {
                    if(GdxGame.TIME > nextAllowedAttack){
                        attack(entity);
                    }
                }else if(dist > disengageRadius){
                    disengage(entity);
                }

            }else
                disengage(entity);
        }

        @Override
        public void exit(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "exited combat");
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    },

    WANDER(){
        @Override
        public void enter(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "entered wander");
            BodyComp body = entity.getComponent(BodyComp.class);
            body.setSteeringBehavior(entity.getComponent(AIComp.class).steeringBehaviors.get("wander"));
        }

        @Override
        public void update(Entity entity) {
            //check for stuff to kill
            boolean foundPlayer = false;
            Vector2 pos = entity.getComponent(BodyComp.class).getPosition();


            for (int i = 0; i < PlayerSystem.players.size(); i++) {
                Entity player = PlayerSystem.players.get(i);
                Vector2 playerPos = player.getComponent(BodyComp.class).getPosition();

                if(playerPos.sub(pos).len() < engageRadius){
                    foundPlayer = true;
                    engage(entity,player);
                    return;
                }
            }

            if(!foundPlayer){

            }

            //if not wander then wander
        }

        @Override
        public void exit(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "exited wander");
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    },

    GLOBAL_STATE(){

        @Override
        public void enter(Entity entity){
        }

        @Override
        public void update(Entity entity) {
            if(!entity.getComponent(AgentComp.class).isAlive){
                entity.getComponent(LightComp.class).lights = null;

                BodyComp body = entity.getComponent(BodyComp.class);
                GameWorld.create(EntityFactory.Enemy.create_melee_corpse(body.getPosition(),body.getOrientation()));
                GameWorld.destroy(entity);

                //TODO might be leaking!?
            }
        }

        @Override
        public void exit(Entity entity) {

        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    };

    protected float disengageRadius = 10f;
    protected float engageRadius = 6f;

    protected void engage(Entity entity, Entity target){
        ((Seek<Vector2>) entity.getComponent(AIComp.class).steeringBehaviors.get("seek")).setTarget(target.getComponent(BodyComp.class));

        AIComp ai = entity.getComponent(AIComp.class);
        ai.stateMachine.changeState(COMBAT);
        ai.target = target;
    }

    protected void disengage(Entity entity){
        ((Seek<Vector2>)entity.getComponent(AIComp.class).steeringBehaviors.get("seek")).setTarget(null);

        AIComp ai = entity.getComponent(AIComp.class);
        ai.stateMachine.changeState(WANDER);
        ai.target = null;
    }

    protected float nextAllowedAttack = 0;

    protected void attack(Entity entity){
        AIComp ai = entity.getComponent(AIComp.class);
        if(ai.target == null)return;

        WeaponComp weapon = entity.getComponent(WeaponComp.class);
        nextAllowedAttack = GdxGame.TIME + weapon.attackDelay;

        Gdx.app.log("Enemy Melee", "attacking " + ai.target);
        if(ai.target.getComponent(AgentComp.class).takeDamage(weapon.damage)){
            //killed player
            //disengage(entity);

        }

    }

}
