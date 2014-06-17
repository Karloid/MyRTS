package com.krld.myrts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Andrey on 6/16/2014.
 */
public class WorldRenderer {
    private static final float LINE_WIDTH = 5;
    private RTSWorld rtsWorld;
    private WorldView worldView;
    private Camera camera;
    private ShapeRenderer renderer;
    private MyInputProcessor myInputProcessor;
    private Color selectionColor;
    private int width;
    private int height;
    private int unitCellSize;
    private Texture defaultTexture;
    private Texture soldierTexture;
    private Texture selectRect;

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    private boolean drawGrid;

    public WorldRenderer(RTSWorld rtsWorld) {
        setRtsWorld(rtsWorld);
    }

    public void init(WorldView worldView) {
        rtsWorld.getMapManager().loadGdxTextures();
        setWorldView(worldView);
        camera = new Camera(new Point(0, 0));
        renderer = new ShapeRenderer();
        myInputProcessor = worldView.getInputProcessor();
        initColors();
        initTextures();
    }

    private void initTextures() {
        defaultTexture = new Texture(Gdx.files.internal("unknow.png"));
        soldierTexture = new Texture(Gdx.files.internal("soldier1.png"));
        selectRect = new Texture(Gdx.files.internal("selectRect.png"));
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
        drawUnits(batch, camera.getPos());
        drawGrid(batch, camera.getPos());
        drawUI(batch, camera.getPos());
    }

    private void drawUnits(SpriteBatch batch, Point cameraPos) {
        for (Unit unit : rtsWorld.getUnits()) {
            Texture texture = defaultTexture;
            int calcedX = unit.getPos().getX() * unitCellSize - cameraPos.getX() + width / 2;
            int calcedY = unit.getPos().getY() * unitCellSize - cameraPos.getY() + height / 2;
            if (unit.getType().equals(UnitType.SOLDIER)) {
                texture = soldierTexture;
            }
            batch.draw(texture, calcedX - unitCellSize * 1.5f, calcedY - unitCellSize * 1.5f, unitCellSize * 2, unitCellSize * 2);
            if (getRtsWorld().getLogicController().getSelectedUnits() != null &&
                    getRtsWorld().getLogicController().getSelectedUnits().contains(unit)) {
                batch.draw(selectRect, calcedX - unitCellSize * 1.5f, calcedY - unitCellSize * 1.5f, unitCellSize * 2, unitCellSize * 2);
            }
        }

    }

    private void initValues() {
        height = getWorldView().getHeigth();
        width = getWorldView().getWidth();
        unitCellSize = rtsWorld.getUnitCellSize();
    }

    private void drawUI(SpriteBatch batch, Point pos) {
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
        if (!drawGrid) {
            return;
        }
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        for (int x = 0; x < width * unitCellSize; x++) {
            int calcedX = x * unitCellSize - cameraPos.getX() + width / 2;
            int scaledY = 0 - cameraPos.getY() + height / 2;
            renderer.line(calcedX, scaledY,
                    calcedX, height * unitCellSize);
        }

        for (int y = 0; y < height * unitCellSize; y++) {
            int calcedX = 0 /*- rtsWorld.getCellSize() / 2*/ - cameraPos.getX() + width / 2;
            int calcedY = y * unitCellSize /*- rtsWorld.getCellSize() / 2 */ - cameraPos.getY() + height / 2;
            renderer.line(calcedX, calcedY,
                    width * unitCellSize, calcedY);
        }
        renderer.end();
        batch.begin();
    }

    private void drawTiles(SpriteBatch batch, Point cameraPos) {
        int scaledX;
        int scaledY;
        int cellSize = getRtsWorld().getCellSize();
        for (int x = 0; x < rtsWorld.getMapManager().getMapWidth(); x++) {
            for (int y = 0; y < rtsWorld.getMapManager().getMapHeight(); y++) {
                scaledX = x * rtsWorld.getCellSize() /*- rtsWorld.getCellSize() / 2*/ - cameraPos.getX() + width / 2;
                if (scaledX > width || scaledX < -cellSize) {
                    continue;
                }
                scaledY = y * cellSize /*- rtsWorld.getCellSize() / 2*/ - cameraPos.getY() + height / 2;
                if (scaledY > height || scaledY < -cellSize) {
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
}
