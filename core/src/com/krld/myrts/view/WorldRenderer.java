package com.krld.myrts.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.krld.myrts.controller.actions.RangeSoldierBehaviour;
import com.krld.myrts.model.RTSWorld;
import com.krld.myrts.controller.move.AStarMoveBehavior;
import com.krld.myrts.model.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andrey on 6/16/2014.
 */
public class WorldRenderer {
    private static final float LINE_WIDTH = 5;
    private static final float HP_BAR_HEIGHT = 3;
    private static final Color ALLIES_UNIT_COLOR = Color.GREEN;
    private static final Color ENEMY_COLOR = Color.RED;
    private RTSWorld rtsWorld;
    private WorldView worldView;
    private Camera camera;
    private ShapeRenderer renderer;
    private MyInputProcessor myInputProcessor;
    private Color selectionColor;
    private int widthView;
    private int heightView;
    private int unitCellSize;
    private int heightUnits;
    private int widthUnits;
    private BitmapFont font;
    private BitmapFont fontLittle;
    private Point mouseActionPoint;
    private TextureContainer tC;

    public boolean isDrawDebug() {
        return drawDebug;
    }

    public void setDrawDebug(boolean drawDebug) {
        this.drawDebug = drawDebug;
    }

    private boolean drawDebug;

    public WorldRenderer(RTSWorld rtsWorld) {
        setRtsWorld(rtsWorld);
    }

    public void init(WorldView worldView) {
        rtsWorld.getMapManager().loadGdxTextures();
        setWorldView(worldView);
        camera = new Camera(new Point(660, 440));
        renderer = new ShapeRenderer();
        myInputProcessor = worldView.getInputProcessor();
        initColors();
        initTextures();
        initFonts();
    }

    private void initFonts() {
        font = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        font.setColor(Color.WHITE);
        font.scale(1f);
        fontLittle = new BitmapFont(Gdx.files.internal("rotorBoyShadow.fnt"),
                Gdx.files.internal("rotorBoyShadow.png"), false);
        fontLittle.setColor(Color.WHITE);
        fontLittle.scale(0.01f);
    }

    private void initTextures() {
        tC = new TextureContainer();
        tC.initTextures();

    }

    private void initColors() {
        setSelectionColor(new Color(0, 0.9f, 0.1f, 0f));
    }

    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public RTSWorld getRtsWorld() {
        return rtsWorld;
    }

    public void draw(SpriteBatch batch) {
        initValues();
        drawBackground(batch);
        drawTiles(batch, camera.getPos());
        drawCorpses(batch, camera.getPos());
        drawUnits(batch, camera.getPos());
        drawGrid(batch, camera.getPos());
        drawPaths(batch, camera.getPos());
        drawUI(batch, camera.getPos());
    }

    private void drawCorpses(SpriteBatch batch, Point cameraPos) {
        for (Corpse corpse : rtsWorld.getCorpses()) {
            Texture texture = tC.getTextureForCorpse(corpse.getType());
            if (texture == null) {
                texture = tC.defaultTexture;
            }
            int calcedX = corpse.getPos().getX() * unitCellSize - cameraPos.getX() + widthView / 2;
            int calcedY = corpse.getPos().getY() * unitCellSize - cameraPos.getY() + heightView / 2;
            batch.draw(texture, calcedX - unitCellSize * 0.5f, calcedY - unitCellSize * 0.5f, unitCellSize * 2, unitCellSize * 2);
        }
    }

    private void drawPaths(SpriteBatch batch, Point cameraPos) {
        if (!drawDebug) {
            return;
        }
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        // draw obstacles
        for (int x = 0; x < widthUnits; x++) {
            for (int y = 0; y < heightUnits; y++) {
                if (!rtsWorld.noObstacle(new Point(x, y))) {
                    int calcedX = x * unitCellSize - cameraPos.getX() + widthView / 2;
                    int calcedY = y * unitCellSize - cameraPos.getY() + heightView / 2;
                    // batch.draw(defaultTexture, calcedX - unitCellSize * 1.5f, calcedY - unitCellSize * 1.5f, unitCellSize * 2, unitCellSize * 2);
                    if (calcedX > 1200 || calcedY > 1000) {
                        continue;
                    }
                    renderer.rect(calcedX, calcedY, 8, 8);
                }
            }
        }
        if (mouseActionPoint != null) {
            int calcedX = mouseActionPoint.getX() * unitCellSize - cameraPos.getX() + widthView / 2;
            int calcedY = mouseActionPoint.getY() * unitCellSize - cameraPos.getY() + heightView / 2;
            renderer.setColor(Color.GREEN);
            renderer.rect(calcedX, calcedY, 8, 8);
        }

        renderer.end();
        batch.begin();
        for (Unit unit : rtsWorld.getUnits()) {
            List<Point> path = unit.getMoveBehavior().getPath();
            if (path != null) {
                for (Point point : path) {
                    int calcedX = point.getX() * unitCellSize - cameraPos.getX() + widthView / 2;
                    int calcedY = point.getY() * unitCellSize - cameraPos.getY() + heightView / 2;
                    batch.draw(tC.stepsTexture, calcedX - unitCellSize * 0.5f, calcedY - unitCellSize * 0.5f, unitCellSize * 2, unitCellSize * 2);
                }
            }
        }


    }

    private void drawUnits(SpriteBatch batch, Point cameraPos) {
        Iterator iter = rtsWorld.getUnits().iterator();
        while (iter.hasNext()) {
            try {
                Unit unit = (Unit) iter.next();
                Texture texture = tC.getTextureForUnit(unit);
                if (texture == null) {
                    texture = tC.defaultTexture;
                }
                int calcedX = unit.getPos().getX() * unitCellSize - cameraPos.getX() + widthView / 2;
                int calcedY = unit.getPos().getY() * unitCellSize - cameraPos.getY() + heightView / 2;
                drawDestPoint(batch, cameraPos, unit, calcedX, calcedY);
                batch.draw(texture, calcedX - unitCellSize * 0.5f, calcedY - unitCellSize * 0.5f, unitCellSize * 2, unitCellSize * 2);
                drawHpBar(batch, cameraPos, unit, calcedX, calcedY);
            } catch (Exception e) {

            }
        }

    }

    private void drawHpBar(SpriteBatch batch, Point cameraPos, Unit unit, int calcedX, int calcedY) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        int hpBarWidth = unitCellSize * 2;
        float hpBarY = calcedY + unitCellSize * 1.5f;
        int hpBarX = calcedX - unitCellSize / 2;

        renderer.setColor(Color.BLACK);
        renderer.rect(hpBarX - 1, hpBarY - 1, hpBarWidth + 2, HP_BAR_HEIGHT + 2);
        if (unit.getPlayer() == rtsWorld.getHumanPlayer()) {
            renderer.setColor(ALLIES_UNIT_COLOR);
        } else {
            renderer.setColor(ENEMY_COLOR);
        }
        hpBarWidth = hpBarWidth * unit.getHp() / unit.getMaxHp();
        renderer.rect(hpBarX, hpBarY, hpBarWidth, HP_BAR_HEIGHT);
        renderer.end();
        batch.begin();
    }

    private void drawDestPoint(SpriteBatch batch, Point cameraPos, Unit unit, int calcedX, int calcedY) {
        if (getRtsWorld().getWorldLogicController().getSelectedUnits() != null &&
                getRtsWorld().getWorldLogicController().getSelectedUnits().contains(unit)) {
            batch.draw(tC.destMoveTexture, calcedX - unitCellSize * 0.5f, calcedY - unitCellSize * 1f, unitCellSize * 2, unitCellSize * 2);
            Point destPoint = unit.getMoveBehavior().getDestMovePoint();
            if (destPoint != null) {
                calcedX = destPoint.getX() * unitCellSize - cameraPos.getX() + widthView / 2;
                calcedY = destPoint.getY() * unitCellSize - cameraPos.getY() + heightView / 2;
                batch.draw(tC.destMoveTexture, calcedX - unitCellSize * 0.5f, calcedY - unitCellSize * 0.5f, unitCellSize * 2, unitCellSize * 2);
            }
        }
    }


    private void initValues() {
        heightView = getWorldView().getHeigth();
        widthView = getWorldView().getWidth();
        widthUnits = rtsWorld.getMapManager().getMapWidth() * RTSWorld.UNIT_CELL_SIZE_RELATIONS;
        heightUnits = rtsWorld.getMapManager().getMapHeight() * RTSWorld.UNIT_CELL_SIZE_RELATIONS;
        unitCellSize = rtsWorld.getUnitCellSize();
    }

    private void drawUI(SpriteBatch batch, Point pos) {
        drawSelectRectangle(batch);
        drawUnitInfo(batch);
    }

    private void drawUnitInfo(SpriteBatch batch) {
        List<Unit> selectedUnits = rtsWorld.getWorldLogicController().getSelectedUnits();
        boolean oneUnitSelected = selectedUnits != null && selectedUnits.size() == 1;
        if (oneUnitSelected) {
            Unit unit = selectedUnits.get(0);
            List<Point> path = unit.getMoveBehavior().getPath();
            String debugString = "_";
            if (path != null && path.size() != 0)
                debugString = ((AStarMoveBehavior) (unit.getMoveBehavior())).getManhattanDistance(unit.getPos(),
                        path.get(0), false) + "";
            fontLittle.draw(batch, "id:" + unit.getId() + "; Type: " + unit.getType(), UIConstants.UNIT_TYPE.getX(), UIConstants.UNIT_TYPE.getY());
            fontLittle.draw(batch, "Action: " + unit.getAction(), UIConstants.UNIT_ACTION.getX(), UIConstants.UNIT_ACTION.getY());
            String direstionInfo;
            direstionInfo = "Direction: " + unit.getDirection();
            if (unit.getType() == UnitType.TROOPER) {
                direstionInfo += " " + ((RangeSoldierBehaviour) unit.getActionBehavior()).getState().getClass().getSimpleName();
            }
            fontLittle.draw(batch, direstionInfo, UIConstants.UNIT_DIRECTION.getX(), UIConstants.UNIT_DIRECTION.getY());
            fontLittle.draw(batch, "Debug: " + debugString, UIConstants.UNIT_DEBUG.getX(), UIConstants.UNIT_DEBUG.getY());
        } else if (selectedUnits != null) {
            fontLittle.draw(batch, "selected " + selectedUnits.size() + " units", UIConstants.UNIT_TYPE.getX(), UIConstants.UNIT_TYPE.getY());
        }
    }

    private void drawSelectRectangle(SpriteBatch batch) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(getSelectionColor());
        if (myInputProcessor.isTouchDown()) {
            int deltaX = myInputProcessor.getTouchMovePos().getX() - myInputProcessor.getTouchDownPos().getX();
            int deltaY = myInputProcessor.getTouchMovePos().getY() - myInputProcessor.getTouchDownPos().getY();
            renderer.rect(myInputProcessor.getTouchDownPos().getX(),
                    myInputProcessor.getTouchDownPos().getY(), deltaX, LINE_WIDTH);
            renderer.rect(myInputProcessor.getTouchDownPos().getX(),
                    myInputProcessor.getTouchDownPos().getY(), LINE_WIDTH, deltaY);
            renderer.rect(myInputProcessor.getTouchMovePos().getX(),
                    myInputProcessor.getTouchMovePos().getY(), deltaX * -1, LINE_WIDTH);
            renderer.rect(myInputProcessor.getTouchMovePos().getX(),
                    myInputProcessor.getTouchMovePos().getY(), LINE_WIDTH, deltaY * -1);
        /*    renderer.rect(myInputProcessor.getTouchDownPos().getX(),
                    myInputProcessor.getTouchDownPos().getY(),
                    deltaX,
                    deltaY);*/
        }
        renderer.end();
        batch.begin();
    }

    private void drawBackground(SpriteBatch batch) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, worldView.getWidth(), worldView.getHeigth());
        renderer.end();
        batch.begin();
    }

    private void drawGrid(SpriteBatch batch, Point cameraPos) {
        if (!drawDebug) {
            return;
        }
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        for (int x = 0; x < rtsWorld.getMapManager().getMapWidth() * RTSWorld.UNIT_CELL_SIZE_RELATIONS; x++) {
            int calcedX = x * unitCellSize - cameraPos.getX() + widthView / 2;
            int calcedY = 0 * unitCellSize - cameraPos.getY() + heightView / 2;
            renderer.line(calcedX, calcedY,
                    calcedX, calcedY + heightUnits * unitCellSize);
        }

        for (int y = 0; y < rtsWorld.getMapManager().getMapHeight() * RTSWorld.UNIT_CELL_SIZE_RELATIONS; y++) {
            int calcedX = 0 * unitCellSize - cameraPos.getX() + widthView / 2;
            int calcedY = y * unitCellSize - cameraPos.getY() + heightView / 2;
            renderer.line(calcedX, calcedY,
                    calcedX + heightUnits * unitCellSize, calcedY);
        }

       /* for (int y = 0; y < rtsWorld.getMapManager().getMapHeight() * RTSWorld.UNIT_CELL_SIZE_RELATIONS; y++) {
            int calcedX = 0 *//*- rtsWorld.getCellSize() / 2*//* - cameraPos.getX() + widthView / 2;
            int calcedY = y * unitCellSize *//*- rtsWorld.getCellSize() / 2 *//* - cameraPos.getY() + heightView / 2;
            renderer.line(calcedX, calcedY,
                    widthUnits * unitCellSize, calcedY);
        }*/
        renderer.end();
        batch.begin();
    }

    private void drawTiles(SpriteBatch batch, Point cameraPos) {
        int scaledX;
        int scaledY;
        int cellSize = getRtsWorld().getCellSize();
        for (int x = 0; x < rtsWorld.getMapManager().getMapWidth(); x++) {
            for (int y = 0; y < rtsWorld.getMapManager().getMapHeight(); y++) {
                scaledX = x * rtsWorld.getCellSize() /*- rtsWorld.getCellSize() / 2*/ - cameraPos.getX() + widthView / 2;
                if (scaledX > widthView || scaledX < -cellSize) {
                    continue;
                }
                scaledY = y * cellSize /*- rtsWorld.getCellSize() / 2*/ - cameraPos.getY() + heightView / 2;
                if (scaledY > heightView || scaledY < -cellSize) {
                    continue;
                }
                batch.draw(MapManager.tileTypes.get(getRtsWorld().getMap()[x][y]).getGdxTexture(),
                        scaledX, scaledY, cellSize, cellSize);
            }
        }
    }

    public void setWorldView(WorldView worldView) {
        this.worldView = worldView;
    }

    public WorldView getWorldView() {
        return worldView;
    }

    public Camera getCamera() {
        return camera;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
    }

    public void setMouseActionPoint(Point mouseActionPoint) {
        this.mouseActionPoint = mouseActionPoint;
    }

    public Point getMouseActionPoint() {
        return mouseActionPoint;
    }
}
