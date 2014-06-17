package com.krld.myrts;

/**
 * Created by Andrey on 6/16/2014.
 */
public class Camera {
    private Point pos;
    private int deltaMove;

    public Camera(Point point) {
        setPos(point);
        setDeltaMove(64);
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

    public void move(Directions direction) {
        if (direction == Directions.UP) {
            pos.setY(pos.getY() + getDeltaMove());
        }
        if (direction == Directions.DOWN) {
            pos.setY(pos.getY() - getDeltaMove());
        }
        if (direction == Directions.LEFT) {
            pos.setX(pos.getX() - getDeltaMove());
        }
        if (direction == Directions.RIGHT) {
            pos.setX(pos.getX() + getDeltaMove());
        }
    }
}
