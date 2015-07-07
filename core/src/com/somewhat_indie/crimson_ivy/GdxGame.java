package com.somewhat_indie.crimson_ivy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.somewhat_indie.crimson_ivy.screens.GameScreen;
import com.somewhat_indie.crimson_ivy.screens.MainMenuScreen;

import java.util.Set;

public class GdxGame extends Game {
	public SpriteBatch batch;

	private ScreenAdapter[] screens;

	private 	ScreenState state;
	public enum ScreenState{
		MainMenu,
		Game
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		screens = new ScreenAdapter[]{
				new MainMenuScreen(this),
				new GameScreen(this)
		};

		Settings.load();
		Assets.load();

		setState(ScreenState.MainMenu);

	}

	@Override
	public void render () {
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
