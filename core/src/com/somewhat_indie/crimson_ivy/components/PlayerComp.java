package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.somewhat_indie.crimson_ivy.AnimationCallback;

/**
 * Created by kaholi on 7/7/15.
 */
public class PlayerComp extends Component implements AnimationCallback{
    public boolean usingKeyboardMouse = true;

    public float nextAllowedAttack;

    private boolean canAttack = false;

    public boolean attack(){
        if(canAttack){
            canAttack = false;
            return true;
        }

        return false;
    }

    @Override
    public void animationCall(String animationName) {
        if(animationName.equals("attack"))
            canAttack = true;
    }
}
