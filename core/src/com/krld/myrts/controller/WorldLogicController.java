package com.krld.myrts.controller;

import com.krld.myrts.model.RTSWorld;
import com.krld.myrts.model.Point;
import com.krld.myrts.model.Unit;

import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public interface WorldLogicController {
    void update();

    void setRTSWorld(RTSWorld rtsWorld);

    void selectUnits(Point minPoint, Point maxPoint);

    List<Unit> getSelectedUnits();

    void mouseAction(Point point);

    void stopCommand();

    long getTick();
}
