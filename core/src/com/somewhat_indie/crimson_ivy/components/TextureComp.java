package com.somewhat_indie.crimson_ivy.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by kaholi on 7/7/15.
 */
public class TextureComp extends Component {
    public TextureRegion region = null;

    public TextureComp(){}
    public TextureComp(int color){
        Pixmap pix = new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        region = new TextureRegion(new Texture(pix));

    }

}
