package com.somewhat_indie.crimson_ivy.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.somewhat_indie.crimson_ivy.components.input.InputComp;
import com.somewhat_indie.crimson_ivy.components.input.KeyboardComp;

/**
 * Created by kaholi on 7/7/15.
 */
public class KeyboardSystem extends EntitySystem implements InputProcessor {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<KeyboardComp> keyboardMap = ComponentMapper.getFor(KeyboardComp.class);

    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(KeyboardComp.class).get());
    }


    @Override
    public boolean keyDown(int keycode) {

        for(int i = 0;i<entities.size();i++){
            Entity entity = entities.get(i);
            KeyboardComp keyboard = keyboardMap.get(entity);

            if(keycode == keyboard.leftKey) keyboard.leftDown   = true;
            if(keycode == keyboard.rightKey)keyboard.rightDown  = true;
            if(keycode == keyboard.upKey)   keyboard.upDown     = true;
            if(keycode == keyboard.downKey) keyboard.downDown   = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for(int i = 0;i<entities.size();i++){
            Entity entity = entities.get(i);
            KeyboardComp keyboard = keyboardMap.get(entity);

            if(keycode == keyboard.leftKey)keyboard.leftDown    = false;
            if(keycode == keyboard.rightKey)keyboard.rightDown  = false;
            if(keycode == keyboard.upKey)keyboard.upDown        = false;
            if(keycode == keyboard.downKey)keyboard.downDown    = false;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
