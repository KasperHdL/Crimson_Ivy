package com.somewhat_indie.crimson_ivy.systems;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.somewhat_indie.crimson_ivy.Settings;
import com.somewhat_indie.crimson_ivy.components.BodyComp;
import com.somewhat_indie.crimson_ivy.components.TextureComp;
import com.somewhat_indie.crimson_ivy.components.TransformComp;

/**
 * Created by kaholi on 7/6/15.
 */
public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<TextureComp> textureMap = ComponentMapper.getFor(TextureComp.class);
    private ComponentMapper<TransformComp> transformMap = ComponentMapper.getFor(TransformComp.class);
    private ComponentMapper<BodyComp> bodyMap = ComponentMapper.getFor(BodyComp.class);

    private SpriteBatch batch;

    public static OrthographicCamera camera;

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private Matrix4 debugMatrix;

    private RayHandler rayHandler;


    public RenderSystem(SpriteBatch batch,World world,RayHandler rayHandler){
        this.batch = batch;
        this.world = world;

        camera = new OrthographicCamera(Gdx.graphics.getWidth() * Settings.pixelToMeter,Gdx.graphics.getHeight() * Settings.pixelToMeter);
        camera.update();
        debugMatrix = new Matrix4(camera.combined);
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawBodies(true);

        this.rayHandler = rayHandler;

    }

    @Override
    public void addedToEngine(Engine engine){
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(TextureComp.class, BodyComp.class, TransformComp.class).get());
    }

    public void update(float deltaTime){
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();



        for(int i = 0;i<entities.size();i++){
            Entity entity = entities.get(i);

            TextureRegion tex = textureMap.get(entity).region;
            if(tex == null)
                continue;
            Body body = bodyMap.get(entity).body;
            TransformComp t = transformMap.get(entity);

            float width = tex.getRegionWidth() * Settings.pixelToMeter;
            float height = tex.getRegionHeight()* Settings.pixelToMeter;

            Vector2 origin = new Vector2(width/2,height/2);
            Vector2 bodyPos = body.getPosition().sub(origin);

            batch.draw(tex,bodyPos.x,bodyPos.y,origin.x,origin.y,width,height,t.scale.x,t.scale.y,body.getAngle() * MathUtils.radDeg);
        }

        batch.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        debugMatrix.set(camera.combined);
        debugRenderer.render(world, debugMatrix);
    }

    public static OrthographicCamera getCamera(){
        return camera;
    }
}
