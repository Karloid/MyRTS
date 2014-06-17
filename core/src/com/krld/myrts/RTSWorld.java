package com.krld.myrts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.security.AlgorithmParameters;

/**
 * Created by Andrey on 6/16/2014.
 */
public class RTSWorld {
    public static final int CELL_SIZE = 32;
    private WorldRenderer worldRenderer;
    private MyInputProcessor inputProcessor;
    private MapManager mapManager;
    private int cellSize;
    private int[][] map;

    public RTSWorld() {
        setWorldRenderer(new WorldRenderer(this));
        setInputProcessor(new MyInputProcessor(this));
        setMapManager(new MapManager());
        setMap(mapManager.loadMapFromFile("rts1v1.json"));
        setCellSize(CELL_SIZE);
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }

    public void runGameLoop() {

    }

    public MyInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public void draw(SpriteBatch batch) {
        worldRenderer.draw(batch);

    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }
}
