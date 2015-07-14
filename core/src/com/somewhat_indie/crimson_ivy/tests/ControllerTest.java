package com.somewhat_indie.crimson_ivy.tests;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.somewhat_indie.crimson_ivy.Assets;
import com.somewhat_indie.crimson_ivy.GdxGame;
import com.somewhat_indie.crimson_ivy.screens.GameScreen;
import com.somewhat_indie.crimson_ivy.screens.MainMenuScreen;

import java.util.ArrayList;

/**
 * Created by kaholi on 7/14/15.
 */

public class ControllerTest extends GdxTest {

    float axis_deadzone = 0.25f;

    @Override
    public void create () {
        initialize();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render () {

        GL20 gl = Gdx.gl;
        gl.glClearColor(.2f, .2f, .2f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    void print (String message) {
        Gdx.app.log("print",message);
    }
    private void initialize () {
        // print the currently connected controllers to the console
        print("Controllers: " + Controllers.getControllers().size);
        int i = 0;
        for (Controller controller : Controllers.getControllers()) {
            print("#" + i++ + ": " + controller.getName());
        }
        if (Controllers.getControllers().size == 0) print("No controllers attached");

        // setup the listener that prints events to the console
        Controllers.addListener(new ControllerListener() {
            public int indexOf (Controller controller) {
                return Controllers.getControllers().indexOf(controller, true);
            }

            @Override
            public void connected (Controller controller) {
                print("connected " + controller.getName());
                int i = 0;
                for (Controller c : Controllers.getControllers()) {
                    print("#" + i++ + ": " + c.getName());
                }
            }

            @Override
            public void disconnected (Controller controller) {
                print("disconnected " + controller.getName());
                int i = 0;
                for (Controller c : Controllers.getControllers()) {
                    print("#" + i++ + ": " + c.getName());
                }
                if (Controllers.getControllers().size == 0) print("No controllers attached");
            }

            @Override
            public boolean buttonDown (Controller controller, int buttonIndex) {
                print("#" + indexOf(controller) + ", button " + buttonIndex + " down");
                return false;
            }

            @Override
            public boolean buttonUp (Controller controller, int buttonIndex) {
                print("#" + indexOf(controller) + ", button " + buttonIndex + " up");
                return false;
            }

            @Override
            public boolean axisMoved (Controller controller, int axisIndex, float value) {
                if(Math.abs(value) < axis_deadzone)return false;

                print("#" + indexOf(controller) + ", axis " + axisIndex + ": " + value);
                return false;
            }

            @Override
            public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
                print("#" + indexOf(controller) + ", pov " + povIndex + ": " + value);
                return false;
            }

            @Override
            public boolean xSliderMoved (Controller controller, int sliderIndex, boolean value) {
                print("#" + indexOf(controller) + ", x slider " + sliderIndex + ": " + value);
                return false;
            }

            @Override
            public boolean ySliderMoved (Controller controller, int sliderIndex, boolean value) {
                print("#" + indexOf(controller) + ", y slider " + sliderIndex + ": " + value);
                return false;
            }

            @Override
            public boolean accelerometerMoved (Controller controller, int accelerometerIndex, Vector3 value) {
                // not printing this as we get to many values
                return false;
            }
        });


    }
}
