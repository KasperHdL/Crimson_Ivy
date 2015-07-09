package com.somewhat_indie.crimson_ivy.components.input;

import com.badlogic.ashley.core.Component;

/**
 * Created by kaholi on 7/7/15.
 */
public class KeyboardMouseComp extends Component{
    public int leftKey;
    public int rightKey;
    public int upKey;
    public int downKey;

    public int attackKey;

    public boolean leftDown = false;
    public boolean rightDown = false;
    public boolean upDown = false;
    public boolean downDown = false;

    public boolean attackDown = false;
}
