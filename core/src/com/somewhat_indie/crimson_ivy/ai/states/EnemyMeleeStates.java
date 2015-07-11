package com.somewhat_indie.crimson_ivy.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.somewhat_indie.crimson_ivy.components.AIComp;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.screens.GameScreen;
import com.somewhat_indie.crimson_ivy.systems.PlayerSystem;

/**
 * Created by kaholi on 7/10/15.
 */
public enum EnemyMeleeStates implements State<Entity> {

    COMBAT(){
        @Override
        public void enter(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "entered idle");
            BodyComp body = entity.getComponent(BodyComp.class);
            Seek<Vector2> seek = (Seek<Vector2>) entity.getComponent(AIComp.class).steeringBehaviors.get("seek");
            if(seek.getTarget() != null)
                body.setSteeringBehavior(seek);
            else
                entity.getComponent(AIComp.class).stateMachine.changeState(WANDER);

        }

        @Override
        public void update(Entity entity) {
            Seek<Vector2> seek = (Seek < Vector2 >) entity.getComponent(AIComp.class).steeringBehaviors.get("seek");
            if(seek.getTarget() != null){

                Vector2 pos = seek.getOwner().getPosition();
                Vector2 playerPos = seek.getTarget().getPosition();

                if(playerPos.sub(pos).len() > disengageRadius){
                    seek.setTarget(null);
                    entity.getComponent(AIComp.class).stateMachine.changeState(WANDER);
                }
            }else
                entity.getComponent(AIComp.class).stateMachine.changeState(WANDER);
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
            Gdx.app.log("Enemy Melee AI", "entered idle");
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
                    ((Seek<Vector2>) entity.getComponent(AIComp.class).steeringBehaviors.get("seek")).setTarget(player.getComponent(BodyComp.class));
                    entity.getComponent(AIComp.class).stateMachine.changeState(COMBAT);
                    return;
                }
            }

            if(!foundPlayer){

            }

            //if not wander then wander
        }

        @Override
        public void exit(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "exited idle");
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

}
