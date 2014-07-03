package com.krld.myrts.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.krld.myrts.controller.DefaultWorldLogicController;
import com.krld.myrts.controller.DefaultUnitFabric;
import com.krld.myrts.controller.AbsractUnitFabric;
import com.krld.myrts.controller.WorldLogicController;
import com.krld.myrts.view.MyInputProcessor;
import com.krld.myrts.view.WorldRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey on 6/16/2014.
 */
public class RTSWorld {
    public static final int CELL_SIZE = 32;
    public static final int UNIT_CELL_SIZE_RELATIONS = 4;
    private static final long DELAY = 100;
    private static final int PASS_MAP = 0;
    private WorldRenderer worldRenderer;
    private MyInputProcessor inputProcessor;
    private MapManager mapManager;
    private int cellSize;
    private int[][] map;
    private int unitCellSize;
    private Thread runner;
    private WorldLogicController worldLogicController;
    private List<Unit> units;
    private AbsractUnitFabric unitFabric;
    private Player humanPlayer;
    private List<Player> players;
    private Player CPUPlayer;
    private int[][] obstacleMap;
    private List<Corpse> corpses;

    public RTSWorld() {
        setCellSize(CELL_SIZE);
        setUnitCellSize(CELL_SIZE / UNIT_CELL_SIZE_RELATIONS);
        setWorldRenderer(new WorldRenderer(this));
        setInputProcessor(new MyInputProcessor(this));
        setMapManager(new MapManager());
        mapManager.setRtsWorld(this);
        setMap(mapManager.loadMapFromFile("rts1v1.json"));
        //  setMap(mapManager.loadMapFromFile("river.json"));
        // setMap(mapManager.loadMapFromFile("test.json"));
        obstacleMap = mapManager.createObstacleMap();
        worldLogicController = new DefaultWorldLogicController();
        worldLogicController.setRTSWorld(this);
        initContainers();
    }

    private void initContainers() {
        players = new ArrayList<Player>();
        players.add(new Player("Human"));
        setHumanPlayer(players.get(0));
        players.add(new Player("CPU"));
        setCPUPlayer(players.get(1));

        corpses = new ArrayList<Corpse>();
        initUnits();
    }

    private void initUnits() {
        unitFabric = new DefaultUnitFabric(this);
        units = new ArrayList<Unit>();
        units = Collections.synchronizedList(units);
        //  addTestUnitsTwo();
        //  addTestUnits();
        addTestUnitsRandom(UnitType.TROOPER,10, 15, 35, 35, getHumanPlayer());
        addTestUnitsRandom(UnitType.SOLDIER,10, 15, 35, 35, getHumanPlayer());
        addTestUnitsRandom(UnitType.UNDEAD_SOLDIER,10, 15, 55, 55, getCPUPlayer());
        addTestUnitsRandom(UnitType.UNDEAD_SOLDIER,30, 15, 99, 99, getCPUPlayer());
    }

    private void addTestUnitsRandom(UnitType type, double delta, int n, double x, double y, Player player) {
        for (int i = 0; i < n; i++) {
            addUnitIfCan(unitFabric.create(type, (int) (x + Math.random() * delta), (int) (y + Math.random() * delta), player));
        }
    }


    private void addUnitIfCan(Unit unit) {
        if (noObstacle(unit.getPos())) {
            units.add(unit);
        }
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
        worldLogicController.update();
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

    public WorldLogicController getWorldLogicController() {
        return worldLogicController;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public void setCPUPlayer(Player CPUPlayer) {
        this.CPUPlayer = CPUPlayer;
    }

    public Player getCPUPlayer() {
        return CPUPlayer;
    }

    public Direction getDirectionByPoints(Point curPos, Point nextPos) {
        if (nextPos.getX() + 1 == curPos.getX()) {
            return Direction.LEFT;
        } else if (nextPos.getX() - 1 == curPos.getX()) {
            return Direction.RIGHT;
        } else if (nextPos.getY() + 1 == curPos.getY()) {
            return Direction.DOWN;
        } else if (nextPos.getY() - 1 == curPos.getY()) {
            return Direction.UP;
        }
        return null;
    }

    public void movePointOnDirection(Direction direction, Point newPoint) {
        if (direction == Direction.LEFT) {
            newPoint.setX(newPoint.getX() - 1);
        } else if (direction == Direction.RIGHT) {
            newPoint.setX(newPoint.getX() + 1);
        } else if (direction == Direction.DOWN) {
            newPoint.setY(newPoint.getY() - 1);
        } else if (direction == Direction.UP) {
            newPoint.setY(newPoint.getY() + 1);
        }
    }

    public boolean canMoveToPoint(Point point, boolean ignoreUnits) {
        if (point == null) {
            return false;
        }
        if (inMap(point) && noObstacle(point) && (!unitInPoint(point) || ignoreUnits)) {
            return true;
        }
        return false;
    }

    private boolean unitInPoint(Point point) {
        for (Unit unit : units) {
            if (unit.getPos().getX() == point.getX() && unit.getPos().getY() == point.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean inMap(Point point) {
        if (point.getX() > getMapManager().getMapWidth() && point.getX() < 0 && point.getY() > getMapManager().getMapHeight() && point.getY() < 0) {
            return false;
        }
        return true;
    }

    public boolean noObstacle(Point point) {
        try {
            if (obstacleMap == null || obstacleMap[point.getX()][point.getY()] == PASS_MAP) {
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //do nothing
        }
        return false;
    }

    public Unit getEnemyUnitInPoint(Point point, Player requestPlayer) {
        for (Unit unit : units) {
            if (unit.getPlayer() != requestPlayer && unit.getPos().equals(point)) {
                return unit;
            }
        }
        return null;
    }

    public Point getPointOnDirection(Point point, Direction direction) {
        point = point.getCopy();
        if (direction == Direction.SELF) {
            return point;
        }
        if (direction == Direction.LEFT) {
            point.setX(point.getX() - 1);
            return point;
        }
        if (direction == Direction.RIGHT) {
            point.setX(point.getX() + 1);
            return point;
        }
        if (direction == Direction.UP) {
            point.setY(point.getY() + 1);
            return point;
        }
        if (direction == Direction.DOWN) {
            point.setY(point.getY() - 1);
            return point;
        }
        return point;
    }

    public List<Corpse> getCorpses() {
        return corpses;
    }

    public void setCorpses(List<Corpse> corpses) {
        this.corpses = corpses;
    }
}
