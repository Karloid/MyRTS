package com.krld.myrts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldView extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    private BitmapFont font;
    private BitmapFont fontLittle;
    private RTSWorld rtsWorld;
    private MyInputProcessor inputProcessor;
    private LwjglApplication application;
    private int heigth;
    private int width;

    @Override
	public void create () {
		batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);
        fontLittle = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        fontLittle.setColor(Color.WHITE);
        fontLittle.scale(0.01f);
        rtsWorld = new RTSWorld();
        initInputProcessor();

        rtsWorld.getWorldRenderer().init(this);
        rtsWorld.runGameLoop();
	}

    private void initInputProcessor() {
        setInputProcessor(rtsWorld.getInputProcessor());
        rtsWorld.getInputProcessor().setWorldView(this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.9f, 0, 1);
		batch.begin();
        rtsWorld.draw(batch);
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, getHeigth() - 10);
		batch.end();
	}

    public void setInputProcessor(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public MyInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setApplication(LwjglApplication application) {
        this.application = application;
    }

    public LwjglApplication getApplication() {
        return application;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }
}
