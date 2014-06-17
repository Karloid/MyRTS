package com.krld.myrts;

/**
 * Created by Andrey on 6/17/2014.
 */
public class Unit {
    private final Point pos;

    public Point getPos() {
        return pos;
    }

    private UnitType type;

    public MoveBehavior getMoveBehavior() {
        return moveBehavior;
    }

    public void setMoveBehavior(MoveBehavior moveBehavior) {
        this.moveBehavior = moveBehavior;
    }

    private MoveBehavior moveBehavior;

    public Unit(int x, int y) {
        pos = new Point(x,y);
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public UnitType getType() {
        return type;
    }
}
