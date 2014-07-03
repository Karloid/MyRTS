package com.krld.myrts.controller.actions;

import com.krld.myrts.controller.ActionBehaviour;
import com.krld.myrts.model.ActionType;
import com.krld.myrts.model.Point;
import com.krld.myrts.model.RTSWorld;
import com.krld.myrts.model.Unit;

/**
 * Created by Andrey on 7/3/2014.
 */
public class RangeSoldierBehaviour implements ActionBehaviour {
    private final IdleState idleState;
    private final MoveState moveState;
    private Unit unit;
    private int damage;
    private State state;
    private RTSWorld rtsWorld;
    private Point moveGoal;

    public RangeSoldierBehaviour() {
        idleState = new IdleState();
        moveState = new MoveState();
        state = idleState;
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
            state.actionOnEnemyUnit(unit);
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

    public State getState() {
        return state;
    }

    private class IdleState implements State {
        @Override
        public void actionOnEnemyUnit(Unit unit) {

        }

        @Override
        public void actionOnPoint(Point point) {
            moveGoal = point;
            unit.getMoveBehavior().setDestMovePoint(point, false);
            state = moveState;
        }

        @Override
        public void update() {

        }
    }

    private class MoveState implements State {
        @Override
        public void actionOnEnemyUnit(Unit unit) {

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
}
