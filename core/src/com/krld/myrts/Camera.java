package com.krld.myrts;

/**
 * Created by Andrey on 6/16/2014.
 */
public class Camera {
    public static final int DELTA_MOVE = 32;
    private static final long CAMERA_UPDATE_DELAY = 50;
    private Point pos;
    private int deltaMove;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveRight;
    private boolean moveLeft;
    private Thread runner;

    public Camera(Point point) {
        setPos(point);
        setDeltaMove(DELTA_MOVE);
        runCameraPosUpdater();

    }

    private void runCameraPosUpdater() {
        runner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    updateCameraPos();
                    try {
                        Thread.sleep(CAMERA_UPDATE_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        runner.start();

    }

    private void updateCameraPos() {
        if (isMoveUp()) {
            pos.setY(pos.getY() + deltaMove);
        }
        if (isMoveDown()) {
            pos.setY(pos.getY() - deltaMove);
        }
        if (isMoveLeft()) {
            pos.setX(pos.getX() - deltaMove);
        }
        if (isMoveRight()) {
            pos.setX(pos.getX() + deltaMove);
        }
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    public int getDeltaMove() {
        return deltaMove;
    }

    public void setDeltaMove(int deltaMove) {
        this.deltaMove = deltaMove;
    }

    public void move(Direction direction) {
        if (direction == Direction.UP) {
            pos.setY(pos.getY() + getDeltaMove());
        }
        if (direction == Direction.DOWN) {
            pos.setY(pos.getY() - getDeltaMove());
        }
        if (direction == Direction.LEFT) {
            pos.setX(pos.getX() - getDeltaMove());
        }
        if (direction == Direction.RIGHT) {
            pos.setX(pos.getX() + getDeltaMove());
        }
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }
}
