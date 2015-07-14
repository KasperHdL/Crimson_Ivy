package com.somewhat_indie.crimson_ivy.tests;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kaholi on 7/14/15.
 */
public class TestStarter extends JFrame {
    ////EDIT THIS TO TEST SOMETHING
    static GdxTest test = new ControllerTest();


    ///FORKED from libgdx.... test

    /**
     * Runs the {@link GdxTest} with the given name.
     * @return {@code true} if the test was found and run, {@code false} otherwise
     */
    public static boolean runTest () {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 480;
        config.title = "Test";
        config.forceExit = false;
        new LwjglApplication(test, config);
        return true;
    }


    /**
     * Runs a libgdx test.
     *
     * @param argv command line arguments
     */
    public static void main (String[] argv) throws Exception {
        runTest();
    }
}
