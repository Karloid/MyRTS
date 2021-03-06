package com.krld.myrts.controller.actions;

import com.krld.myrts.controller.ActionBehaviour;
import com.krld.myrts.controller.move.AStarMoveBehavior;
import com.krld.myrts.controller.move.MoveBehavior;
import com.krld.myrts.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrey on 6/19/2014.
 */
public class MeleeSoldierBehaviour implements ActionBehaviour {
    private Unit attackedUnit;
    private int defaultDamage;
    private double findEnemyDistance = 15;

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
            findAndAttackNearbyEnemy();
            attackingUnit();
        } else {
            unit.getMoveBehavior().update();
        }
        if (!unit.getAction().equals(ActionType.NOTHING)) {
            return;
        }
        findAndAttackNearbyEnemy();

    }

    private void findAndAttackNearbyEnemy() {
        Unit candidatUnit = null;
        for (Unit curUnit : rtsWorld.getUnits()) {
            if (curUnit.getPlayer() != unit.getPlayer()) {
                if (AStarMoveBehavior.getManhattanDistance(unit.getPos(), curUnit.getPos()) < getFindEnemyDistance()
                        && (candidatUnit == null ||
                        AStarMoveBehavior.getManhattanDistance(unit.getPos(), curUnit.getPos()) <
                                AStarMoveBehavior.getManhattanDistance(unit.getPos(), candidatUnit.getPos()))) {
                    candidatUnit = curUnit;
                }
                /*     if (getEuclideDistance(unit.getPos(), curUnit.getPos()) < getFindEnemyDistance()
                        && (candidatUnit == null || getEuclideDistance(unit.getPos(), curUnit.getPos()) < (getEuclideDistance(unit.getPos(), candidatUnit.getPos())))) {
                    candidatUnit = curUnit;
                }*/
            }
        }
        if (candidatUnit != null)
            attackedUnit = candidatUnit;
    }

    @Override
    public int getDamageAmount() {
        return getDefaultDamage();
    }

    private void attackingUnit() {
        if (attackedUnit.isDead()) {
            attackedUnit = null;
            unit.setAction(ActionType.NOTHING);
            return;
        }
        MoveBehavior moveBehavior = unit.getMoveBehavior();
        if (((AStarMoveBehavior) moveBehavior).getManhattanDistance(attackedUnit.getPos(),
                unit.getPos(), false) == 1) {
            meleeAttackUnit(attackedUnit);
        } else {
            goToAttackedUnit(moveBehavior);
        }
    }

    private void goToAttackedUnit(MoveBehavior moveBehavior) {
        if (moveBehavior.getDestMovePoint() == null ||
                !moveBehavior.getDestMovePoint().equals(getClosestOpenPointToAttackedUnit())) {
            Point closestPoint = getClosestOpenPointToAttackedUnit();
            if (closestPoint == null) {
                attackedUnit = null;
                return;
            }
            moveBehavior.update();
            moveBehavior.setDestMovePoint(closestPoint, false);
        } else {
         //   if (validePathPicked()) {
                moveBehavior.update();
         //   }
        }
    }

    private boolean validePathPicked() {
        List<Point> path = unit.getMoveBehavior().getPath();
        if (path == null) {
            return true;
        }
        Point lastPointPath = path.get(path.size() - 1);
        if (AStarMoveBehavior.getManhattanDistance(unit.getPos(), attackedUnit.getPos()) <=
                (AStarMoveBehavior.getManhattanDistance(lastPointPath, attackedUnit.getPos()))) {
            return false;
        }
        return true;
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
        Collections.shuffle(availablePointsToMove);
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

    @Override
    public void setRangeAttack(float range) {

    }

    @Override
    public void stopCommand() {

    }

    @Override
    public void setDelayAttack(int delay) {

    }

    public double getFindEnemyDistance() {
        return findEnemyDistance;
    }

    public void setFindEnemyDistance(double findEnemyDistance) {
        this.findEnemyDistance = findEnemyDistance;
    }
}
