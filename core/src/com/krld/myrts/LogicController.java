package com.krld.myrts;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public class LogicController implements AbstractLogicController {
    private RTSWorld rtsWorld;
    private List<Unit> selectedUnits;

    @Override
    public void update() {

    }

    @Override
    public void setRTSWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    @Override
    public void selectUnits(Point minPoint, Point maxPoint) {
        selectedUnits = new ArrayList<Unit>();
        for (Unit unit : rtsWorld.getUnits()) {
            Point pos = unit.getPos();
            if (pos.getX() >= minPoint.getX() && pos.getX() <= maxPoint.getX() &&
                    pos.getY() >= minPoint.getY() && pos.getY() <= maxPoint.getY()) {
                selectedUnits.add(unit);
            }
        }
        System.out.println("select units count: " + selectedUnits.size());
    }

    @Override
    public List<Unit> getSelectedUnits() {
        return selectedUnits;
    }
}
