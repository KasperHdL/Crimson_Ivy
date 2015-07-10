package com.somewhat_indie.crimson_ivy.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

/**
 * Created by kaholi on 7/10/15.
 */
public enum EnemyMeleeStates implements State<Entity> {

    COMBAT(){
        @Override
        public void enter(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "entered combat");
        }

        @Override
        public void update(Entity entity) {

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

    IDLE(){
        @Override
        public void enter(Entity entity) {
            Gdx.app.log("Enemy Melee AI", "entered idle");
        }

        @Override
        public void update(Entity entity) {

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



}
