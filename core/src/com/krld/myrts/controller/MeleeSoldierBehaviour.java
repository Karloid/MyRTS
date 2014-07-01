package com.krld.myrts.controller;

import com.krld.myrts.controller.AStarMoveBehavior;
import com.krld.myrts.controller.ActionBehaviour;
import com.krld.myrts.controller.MoveBehavior;
import com.krld.myrts.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 6/19/2014.
 */
public class MeleeSoldierBehaviour implements ActionBehaviour {
    private Unit attackedUnit;
    private int defaultDamage;

    public Unit getUnit() {
        return unit;
    }

    private Unit unit;
    private RTSWorld rtsWorld;

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
        this.rtsWorld = unit.getRtsWorld();
    }

    @Override
    public void actionOnPoint(Point point) {
        Unit enemyUnit = rtsWorld.getEnemyUnitInPoint(point, unit.getPlayer());
        if (enemyUnit != null) {
            attackedUnit = enemyUnit;
        } else {
            attackedUnit = null;
            unit.getMoveBehavior().setDestMovePoint(point, false);
        }
    }

    @Override
    public void update() {
        if (attackedUnit != null) {
            attackingUnit();
        } else {
            unit.getMoveBehavior().update();
        }
    }

    @Override
    public int getDamageAmount() {
        return getDefaultDamage();
    }

    private void attackingUnit() {
        MoveBehavior moveBehavior = unit.getMoveBehavior();
        if (((AStarMoveBehavior) moveBehavior).getManhattanDistance(attackedUnit.getPos(), unit.getPos(), false) == 1) {
            meleeAttackUnit(attackedUnit);
        } else {
            goToAttackedUnit(moveBehavior);
        }
    }

    private void goToAttackedUnit(MoveBehavior moveBehavior) {
        if (moveBehavior.getDestMovePoint() == null || !moveBehavior.getDestMovePoint().equals(getClosestOpenPointToAttackedUnit())) {
            Point closestPoint = getClosestOpenPointToAttackedUnit();
            if (closestPoint == null) {
                attackedUnit = null;
                return;
            }
            moveBehavior.setDestMovePoint(closestPoint, false);
        } else {
            moveBehavior.update();
        }
    }

    private Point getClosestOpenPointToAttackedUnit() {
        List<Point> availablePointsToMove = new ArrayList<Point>();
        fillAvailablePointsToMoveNearAttackedUnit(availablePointsToMove);
        Point openPoint = null;
        for (Point point : availablePointsToMove) {
            if (openPoint == null || Point.getManhattanDistance(openPoint, unit.getPos()) > Point.getManhattanDistance(point, unit.getPos())) {
                openPoint = point;
            }
        }
        return openPoint;
    }

    private List<Point> fillAvailablePointsToMoveNearAttackedUnit(List<Point> availablePointsToMove) {
        Point testPoint;
        testPoint = attackedUnit.getPos().getCopy();
        testPoint.setY(testPoint.getY() + 1);
        if (rtsWorld.canMoveToPoint(testPoint, false)) {
            availablePointsToMove.add(testPoint);
        }
        testPoint = attackedUnit.getPos().getCopy();
        testPoint.setX(testPoint.getX() + 1);
        if (rtsWorld.canMoveToPoint(testPoint, false)) {
            availablePointsToMove.add(testPoint);
        }
        testPoint = attackedUnit.getPos().getCopy();
        testPoint.setY(testPoint.getY() - 1);
        if (rtsWorld.canMoveToPoint(testPoint, false)) {
            availablePointsToMove.add(testPoint);
        }
        testPoint = attackedUnit.getPos().getCopy();
        testPoint.setX(testPoint.getX() - 1);
        if (rtsWorld.canMoveToPoint(testPoint, false)) {
            availablePointsToMove.add(testPoint);
        }
        return availablePointsToMove;
    }

    private void meleeAttackUnit(Unit attackedUnit) {
        Direction direction = rtsWorld.getDirectionByPoints(unit.getPos(), attackedUnit.getPos());
        unit.setDirection(direction);
        unit.setAction(ActionType.MELEE_ATTACK);
    }

    public int getDefaultDamage() {
        return defaultDamage;
    }

    public void setDefaultDamage(int defaultDamage) {
        this.defaultDamage = defaultDamage;
    }
}
