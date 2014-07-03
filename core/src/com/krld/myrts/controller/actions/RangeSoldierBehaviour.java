package com.krld.myrts.controller.actions;

import com.krld.myrts.controller.ActionBehaviour;
import com.krld.myrts.controller.move.AStarMoveBehavior;
import com.krld.myrts.model.ActionType;
import com.krld.myrts.model.Point;
import com.krld.myrts.model.RTSWorld;
import com.krld.myrts.model.Unit;

/**
 * Created by Andrey on 7/3/2014.
 */
public class RangeSoldierBehaviour implements ActionBehaviour {
    private State state;
    private State attackTargetState;
    private final IdleState idleState;
    private final MoveState moveState;
    private Unit unit;
    private int damage;

    private RTSWorld rtsWorld;
    private Point moveGoal;

    private Unit attackTarget;
    private double findEnemyDistance;
    private State goToTargetState;
    private double rangeAttack;

    public RangeSoldierBehaviour() {
        idleState = new IdleState();
        moveState = new MoveState();
        attackTargetState = new AttackTargetState();
        goToTargetState = new GoToTargetState();
        state = idleState;

        setFindEnemyDistance(33);
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
        this.rtsWorld = unit.getRtsWorld();
    }

    @Override
    public void actionOnPoint(Point point) {
        Unit enemyUnit = rtsWorld.getEnemyUnitInPoint(point, unit.getPlayer());
        if (enemyUnit != null) {
            state.actionOnEnemyUnit(enemyUnit);
        } else {
            state.actionOnPoint(point);
        }

    }

    @Override
    public void update() {
        state.update();
    }

    @Override
    public int getDamageAmount() {
        return damage;
    }

    @Override
    public void setDefaultDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void setRangeAttack(float range) {
        this.rangeAttack = range;
    }

    public State getState() {
        return state;
    }


    private Unit findNearbyEnemy() {
        Unit candidatUnit = null;
        for (Unit curUnit : rtsWorld.getUnits()) {
            if (curUnit.getPlayer() != unit.getPlayer()) {
                if (getEuclideDistance(unit.getPos(), curUnit.getPos()) < getFindEnemyDistance()
                        && (candidatUnit == null || getEuclideDistance(unit.getPos(), candidatUnit.getPos()) < getFindEnemyDistance())) {
                    candidatUnit = curUnit;
                }
            }
        }
        return candidatUnit;
    }

    private double getEuclideDistance(Point pos, Point pos1) {
        return AStarMoveBehavior.getEuclideDistance(pos, pos1);
    }

    public double getFindEnemyDistance() {
        return findEnemyDistance;
    }

    public void setFindEnemyDistance(double findEnemyDistance) {
        this.findEnemyDistance = findEnemyDistance;
    }

    public double getRangeAttack() {
        return rangeAttack;
    }

    public void setRangeAttack(double rangeAttack) {
        this.rangeAttack = rangeAttack;
    }

    private class IdleState implements State {
        @Override
        public void actionOnEnemyUnit(Unit enemyUnit) {
            attackTarget = enemyUnit;
            state = attackTargetState;
            state.update();
        }

        @Override
        public void actionOnPoint(Point point) {
            moveGoal = point;
            unit.getMoveBehavior().setDestMovePoint(point, false);
            state = moveState;
        }

        @Override
        public void update() {
            Unit candidat = findNearbyEnemy();
            if (candidat != null) {
                attackTarget = candidat;
                state = attackTargetState;
                state.update();
            }

        }
    }

    private class MoveState implements State {
        @Override
        public void actionOnEnemyUnit(Unit enemyUnit) {
            attackTarget = enemyUnit;
            state = attackTargetState;
            state.update();
        }

        @Override
        public void actionOnPoint(Point point) {
            moveGoal = point;
            unit.getMoveBehavior().setDestMovePoint(point, false);
        }

        @Override
        public void update() {
            unit.getMoveBehavior().update();
            if (unit.getAction() == ActionType.NOTHING) {
                state = idleState;
            }
        }
    }

    private class AttackTargetState implements State {
        @Override
        public void actionOnEnemyUnit(Unit unit) {
            attackTarget = unit;
            state = attackTargetState;
            state.update();
        }

        @Override
        public void actionOnPoint(Point point) {
            moveGoal = point;
            unit.getMoveBehavior().setDestMovePoint(point, false);
            state = moveState;
        }

        @Override
        public void update() {
            if (attackTarget == null || attackTarget.isDead()) {
                attackTarget = null;
                if (moveGoal != null) {
                    unit.getMoveBehavior().setDestMovePoint(moveGoal, false);
                    state = moveState;
                } else {
                    state = idleState;
                }
            } else {
                if (getEuclideDistance(unit.getPos(), attackTarget.getPos()) > getRangeAttack()) {
                    state = goToTargetState;
                } else {
                    unit.setAction(ActionType.RANGE_ATTACK);
                    unit.setActionPoint(attackTarget.getPos().getCopy());
                }
            }
        }
    }

    private class GoToTargetState implements State {
        @Override
        public void actionOnEnemyUnit(Unit enemyUnit) {
            attackTarget = enemyUnit;
            state = attackTargetState;
            state.update();
        }

        @Override
        public void actionOnPoint(Point point) {
            moveGoal = point;
            unit.getMoveBehavior().setDestMovePoint(point, false);
            state = moveState;
        }

        @Override
        public void update() {
                   thrthrth
        }
    }
}
