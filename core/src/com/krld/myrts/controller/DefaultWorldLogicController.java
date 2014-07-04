package com.krld.myrts.controller;


import com.krld.myrts.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey on 6/17/2014.
 */
public class DefaultWorldLogicController implements WorldLogicController {
    private RTSWorld rtsWorld;
    private List<Unit> selectedUnits;
    private long tick = 0;

    @Override
    public void update() {

        tick++;
        updateUnits();
    }

    private void updateUnits() {
        for (Unit unit : rtsWorld.getUnits()) {
            unit.update();
        }
        applyUnitsAction();
        removeDeadUnits();
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

    private void removeDeadUnits() {
        ArrayList<Unit> deadUnits = null;
        for (Unit unit : rtsWorld.getUnits()) {
            if (unit.isDead()) {
                if (deadUnits == null) {
                    deadUnits = new ArrayList<Unit>();
                }
                deadUnits.add(unit);
                rtsWorld.getCorpses().add(new Corpse(unit.getPos(), unit.getType()));
            }
        }
        if (deadUnits != null) {
            rtsWorld.getUnits().removeAll(deadUnits);
        }
    }

    private void applyUnitsAction() {
        for (Unit unit : rtsWorld.getUnits()) {
            if (unit.getAction().equals(ActionType.MOVE)) {
                unitMove(unit);
            }
            if (unit.getAction().equals(ActionType.MELEE_ATTACK)) {
                unitMeleeAttack(unit);
            }
            if (unit.getAction().equals(ActionType.RANGE_ATTACK)) {
                unitRangeAttack(unit);
            }
        }
    }

    private void unitRangeAttack(Unit unit) {
        if (unit.getAction() != ActionType.RANGE_ATTACK) {
            return;
        }
        Point actionPoint = unit.getActionPoint();
        if (actionPoint == null) {
            return;
        }
        Unit enemy = rtsWorld.getEnemyUnitInPoint(actionPoint, unit.getPlayer());
        if (enemy != null) {
            enemy.receiveDamage(unit.getActionBehavior().getDamageAmount());
        }
    }

    private void unitMeleeAttack(Unit unit) {
        if (unit.getAction() != ActionType.MELEE_ATTACK) {
            return;
        }
        Point actionPoint = rtsWorld.getPointOnDirection(unit.getPos(), unit.getDirection());
        Unit enemy = rtsWorld.getEnemyUnitInPoint(actionPoint, unit.getPlayer());
        if (enemy != null) {
            enemy.receiveDamage(unit.getActionBehavior().getDamageAmount());
            //       fwewef
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
            unit.actionOnPoint(point);
        }
    }

    @Override
    public void stopCommand() {
        if (selectedUnits == null) {
            return;
        }
        for (Unit unit : selectedUnits) {
            unit.stopAction();
        }
    }
}
