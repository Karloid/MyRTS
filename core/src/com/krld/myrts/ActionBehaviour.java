package com.krld.myrts;

/**
 * Created by Andrey on 6/19/2014.
 */
public interface ActionBehaviour {
    void setUnit(Unit unit);

    void actionOnPoint(Point point);

    void update();
}
