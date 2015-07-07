package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by kaholi on 7/6/15.
 */
public class CameraComp extends Component {
    public Entity target;
    public OrthographicCamera camera;

    public float drag;

}
