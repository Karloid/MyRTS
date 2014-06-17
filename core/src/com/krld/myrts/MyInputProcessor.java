package com.krld.myrts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Andrey on 6/16/2014.
 */
public class MyInputProcessor implements InputProcessor {
    private WorldView worldView;
    private com.krld.myrts.RTSWorld RTSWorld;

    public Point getTouchMovePos() {
        return touchMovePos;
    }

    public void setTouchMovePos(Point touchMovePos) {
        this.touchMovePos = touchMovePos;
    }

    private Point touchMovePos;

    public Point getTouchDownPos() {
        return touchDownPos;
    }

    public void setTouchDownPos(Point touchDownPos) {
        this.touchDownPos = touchDownPos;
    }

    private Point touchDownPos;

    public boolean isTouchDown() {
        return touchDown;
    }

    public void setTouchDown(boolean touchDown) {
        this.touchDown = touchDown;
    }

    private boolean touchDown;

    public MyInputProcessor(RTSWorld rtsWorld) {
        setRTSWorld(rtsWorld);
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.W) {
            getRTSWorld().getWorldRenderer().getCamera().move(Directions.UP);
        }
        if (keyCode == Input.Keys.S) {
            getRTSWorld().getWorldRenderer().getCamera().move(Directions.DOWN);
        }
        if (keyCode == Input.Keys.D) {
            getRTSWorld().getWorldRenderer().getCamera().move(Directions.RIGHT);
        }
        if (keyCode == Input.Keys.A) {
            getRTSWorld().getWorldRenderer().getCamera().move(Directions.LEFT);
        }

        if (keyCode == Input.Keys.F1) {
            getRTSWorld().getWorldRenderer().setDrawGrid(!getRTSWorld().getWorldRenderer().isDrawGrid());
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = worldView.getHeigth() - screenY;
        if (button == Input.Buttons.LEFT) {
            touchDownPos = new Point(screenX, screenY);
            touchMovePos = new Point(screenX, screenY);
            System.out.println("touch down: screenX:" + screenX + " screenY:" + screenY);
            touchDown = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = worldView.getHeigth() - screenY;
        if (button == Input.Buttons.LEFT) {
            touchDown = false;
            handleSelectRectangle();
        }
        return false;
    }

    private void handleSelectRectangle() {
        int xMin = Math.min(touchDownPos.getX(), touchMovePos.getX());
        int yMin = Math.min(touchDownPos.getY(), touchMovePos.getY());
        int xMax = Math.max(touchDownPos.getX(), touchMovePos.getX());
        int yMax = Math.max(touchDownPos.getY(), touchMovePos.getY());
        Point cameraPos = getRTSWorld().getWorldRenderer().getCamera().getPos();
        int width = getRTSWorld().getWorldRenderer().getWorldView().getWidth();
        int height = getRTSWorld().getWorldRenderer().getWorldView().getHeigth();
        xMin = calcX(xMin, cameraPos, width);
        yMin = calcY(yMin, cameraPos, height);
        xMax = calcX(xMax, cameraPos, width);
        yMax = calcY(yMax, cameraPos, height);
        getRTSWorld().getLogicController().selectUnits(new Point(xMin, yMin), new Point(xMax, yMax));
    }

    private int calcX(int x, Point cameraPos, int width) {
        return (x + cameraPos.getX() - width / 2) / getRTSWorld().getUnitCellSize();
    }

    private int calcY(int y, Point cameraPos, int height) {
        return (y + cameraPos.getY() - height / 2) / getRTSWorld().getUnitCellSize();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenY = worldView.getHeigth() - screenY;
        if (touchDown) {
            touchMovePos = new Point(screenX, screenY);
            //  System.out.println("move: screenX:" + screenX + " screenY:" + screenY);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setWorldView(WorldView worldView) {
        this.worldView = worldView;
    }

    public WorldView getWorldView() {
        return worldView;
    }

    public void setRTSWorld(RTSWorld RTSWorld) {
        this.RTSWorld = RTSWorld;
    }

    public RTSWorld getRTSWorld() {
        return RTSWorld;
    }
}
