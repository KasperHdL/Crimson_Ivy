package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

/**
 * Created by kaholi on 7/9/15.
 */
public class AIComp extends Component implements Telegraph {

    public StateMachine<Entity> stateMachine;

    public AIComp(Entity owner, State<Entity> initialState){
        stateMachine = new DefaultStateMachine<>(owner,initialState);
    }
    public AIComp(Entity owner, State<Entity> initialState, State<Entity> globalState){
        stateMachine = new DefaultStateMachine<>(owner,initialState,globalState);
    }

    public void update(){
        stateMachine.update();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
}
