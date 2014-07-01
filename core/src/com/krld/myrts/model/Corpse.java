package com.krld.myrts.model;

/**
 * Created by Andrey on 7/1/2014.
 */
public class Corpse {
    private Point pos;
    private UnitType type;

    public Corpse(Point pos, UnitType type) {
        setPos(pos);
        setType(type);
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public UnitType getType() {
        return type;
    }
}
