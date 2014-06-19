package com.krld.myrts;

import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public interface MoveBehavior {
    void setDestMovePoint(Point point, boolean ignoreUnits);

    void setUnit(Unit unit);

    void setRtsWorld(RTSWorld rtsWorld);

    List<Point> getPath();

    void update();

    void denyMove();

    void applyMove();

    Point getDestMovePoint();
}
