package com.krld.myrts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 6/16/2014.
 */
public class RTSWorld {
    public static final int CELL_SIZE = 32;
    static final int UNIT_CELL_SIZE_RELATIONS = 4;
    private static final long DELAY = 20;
    private WorldRenderer worldRenderer;
    private MyInputProcessor inputProcessor;
    private MapManager mapManager;
    private int cellSize;
    private int[][] map;
    private int unitCellSize;
    private Thread runner;
    private AbstractLogicController logicController;
    private List<Unit> units;
    private AbsractUnitFabric unitFabric;

    public RTSWorld() {
        setWorldRenderer(new WorldRenderer(this));
        setInputProcessor(new MyInputProcessor(this));
        setMapManager(new MapManager());
        setMap(mapManager.loadMapFromFile("rts1v1.json"));
        setCellSize(CELL_SIZE);
        setUnitCellSize(CELL_SIZE / UNIT_CELL_SIZE_RELATIONS);
        logicController = new LogicController();
        logicController.setRTSWorld(this);
        initContainers();
    }

    private void initContainers() {
        unitFabric = new DefaultUnitFabric();
        units = new ArrayList<Unit>();
        units.add(unitFabric.createSoldier(3, 3));
        units.add(unitFabric.createSoldier(8, 5));
        units.add(unitFabric.createSoldier(4, 7));
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }

    public void startGameLoopThread() {
        runner = new Thread(new Runnable() {
            @Override
            public void run() {
                runGameLoop();
            }
        });
        runner.start();
    }

    private void runGameLoop() {
        try {
            while (true) {
                update();
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        logicController.update();
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

    public int getUnitCellSize() {
        return unitCellSize;
    }

    public void setUnitCellSize(int unitCellSize) {
        this.unitCellSize = unitCellSize;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public AbstractLogicController getLogicController() {
        return logicController;
    }
}
