package com.somewhat_indie.crimson_ivy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.somewhat_indie.crimson_ivy.screens.GameScreen;
import com.somewhat_indie.crimson_ivy.screens.MainMenuScreen;


public class GdxGame extends Game {
	public SpriteBatch batch;

	public static float TIME = 0f;

	private ScreenAdapter[] screens;

	private 	ScreenState state;
	public enum ScreenState{
		MainMenu,
		Game
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		Settings.load();
		Assets.load();

		screens = new ScreenAdapter[]{
				new MainMenuScreen(this),
				new GameScreen(this)
		};


		setState(ScreenState.MainMenu);

	}

	@Override
	public void render () {
		TIME += Gdx.graphics.getDeltaTime();

		GL20 gl = Gdx.gl;
		gl.glClearColor(.2f, .2f, .2f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

	}

	public void setState(ScreenState state){
		this.state = state;

		setScreen(screens[state.ordinal()]);

	}

	public ScreenState getState(){return state;}
}
