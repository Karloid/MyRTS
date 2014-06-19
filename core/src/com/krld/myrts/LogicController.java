package com.krld.myrts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public class LogicController implements AbstractLogicController {
    private RTSWorld rtsWorld;
    private List<Unit> selectedUnits;

    @Override
    public void update() {
        updateUnits();
    }

    private void updateUnits() {
        for (Unit unit : rtsWorld.getUnits()) {
            unit.update();
        }
        applyUnitsAction();
        Collections.sort(rtsWorld.getUnits(), (Unit o1, Unit o2) -> {
            if (o1.getPos().getY() > o2.getPos().getY()) {
                return -1;
            }
            if (o1.getPos().getY() < o2.getPos().getY()) {
                return 1;
            }
            return 0;
        });
    }

    private void applyUnitsAction() {
        for (Unit unit : rtsWorld.getUnits()) {
            if (unit.getAction().equals(ActionType.MOVE)) {
                unitMove(unit);
            }
        }
    }

    private void unitMove(Unit unit) {
        if (unit.getAction() != ActionType.MOVE) {
            return;
        }
        Direction direction = unit.getDirection();
        Point newPoint = unit.getPos().getCopy();
        rtsWorld.movePointOnDirection(direction, newPoint);

        if (rtsWorld.canMoveToPoint(newPoint, false)) {
            unit.setPos(newPoint);
            unit.getMoveBehavior().applyMove();
        } else {
            unit.getMoveBehavior().denyMove();
        }
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
            if (unit.getPlayer() == rtsWorld.getHumanPlayer() && pos.getX() >= minPoint.getX() && pos.getX() <= maxPoint.getX() &&
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

    @Override
    public void mouseAction(Point point) {
        if (selectedUnits == null) {
            return;
        }
        for (Unit unit : selectedUnits) {
            unit.setDestMovePoint(point);
        }
    }
}
