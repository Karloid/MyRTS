package com.krld.myrts;

import com.badlogic.gdx.graphics.Color;
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
        drawBackground(batch);
        drawTiles(batch, camera.getPos());
        drawGrid(batch, camera.getPos());
        drawUI(batch, camera.getPos());

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

    private void drawGrid(SpriteBatch batch, Point pos) {

    }

    private void drawTiles(SpriteBatch batch, Point cameraPos) {
        int scaledX;
        int scaledY;
        int cellSize = getRtsWorld().getCellSize();
        int height = getWorldView().getHeigth();
        int width = getWorldView().getWidth();
        for (int x = 0; x < rtsWorld.getMapManager().getMapWidth(); x++) {
            for (int y = 0; y < rtsWorld.getMapManager().getMapHeight(); y++) {
                scaledX = x * rtsWorld.getCellSize() - rtsWorld.getCellSize() / 2 - cameraPos.getX() + width / 2;
                if (scaledX > width || scaledX < -cellSize) {
                    continue;
                }
                scaledY = y * cellSize - rtsWorld.getCellSize() / 2 - cameraPos.getY() + height / 2;
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
