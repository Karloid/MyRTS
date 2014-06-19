package com.krld.myrts;

import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public interface AbstractLogicController {
    void update();

    void setRTSWorld(RTSWorld rtsWorld);

    void selectUnits(Point minPoint, Point maxPoint);

    List<Unit> getSelectedUnits();

    void mouseAction(Point point);
}
