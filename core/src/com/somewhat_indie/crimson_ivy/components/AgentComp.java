package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;

/**
 * Created by kaholi on 7/12/15.
 */
public class AgentComp extends Component {

    public boolean isAlive;

    public float health;
    public float maxHealth;

    public AgentComp(float health){this.health = health;maxHealth = health;isAlive = true;}

    public boolean takeDamage(float amount){
        if(!isAlive) return false;

        health -= amount;

        Gdx.app.log("Agent",health + " health");

        if(health <= 0){
            die();
            //TODO create particle splatter
            return true;
        }else
            //TODO create particle splatter

        return false;
    }

    public void die(){
        isAlive = false;
        //TODO stop colliding with stuff
    }


}
