package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;

/**
 * Created by kaholi on 7/9/15.
 */
public class AIComp<T> extends Component {

    public StateMachine<T> stateMachine;

    public AIComp(State<T> initialState){
        stateMachine = new DefaultStateMachine<T>((T) this,initialState);
    }
}
