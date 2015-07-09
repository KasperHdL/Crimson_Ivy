package com.somewhat_indie.crimson_ivy.components;

import box2dLight.PositionalLight;
import com.badlogic.ashley.core.Component;

import java.util.HashMap;

/**
 * Created by kaholi on 7/9/15.
 */
public class LightComp extends Component {
    public HashMap<String,PositionalLight> lights;
}
