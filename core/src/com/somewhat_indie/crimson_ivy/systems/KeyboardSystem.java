package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardMouseComp;

/**
 * Created by kaholi on 7/7/15.
 */
public class KeyboardSystem extends EntitySystem implements InputProcessor {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<KeyboardMouseComp> keyboardMap = ComponentMapper.getFor(KeyboardMouseComp.class);

    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(KeyboardMouseComp.class).get());
    }


    @Override
    public boolean keyDown(int keycode) {

        for(int i = 0;i<entities.size();i++){
            KeyboardMouseComp component = keyboardMap.get(entities.get(i));

            if(keycode == component.leftKey)    component.leftDown   = true;
            if(keycode == component.rightKey)   component.rightDown  = true;
            if(keycode == component.upKey)      component.upDown     = true;
            if(keycode == component.downKey)    component.downDown   = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for(int i = 0;i<entities.size();i++){
            KeyboardMouseComp component = keyboardMap.get(entities.get(i));

            if(keycode == component.leftKey)    component.leftDown   = false;
            if(keycode == component.rightKey)   component.rightDown  = false;
            if(keycode == component.upKey)      component.upDown     = false;
            if(keycode == component.downKey)    component.downDown   = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (int i = 0; i < entities.size(); i++) {
            KeyboardMouseComp component = keyboardMap.get(entities.get(i));
            if (button == component.attackKey)
                component.attackDown = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (int i = 0; i < entities.size(); i++) {
            KeyboardMouseComp component = keyboardMap.get(entities.get(i));
            if (button == component.attackKey)
                component.attackDown = false;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
