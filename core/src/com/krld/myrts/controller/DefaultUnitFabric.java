package com.krld.myrts.controller;

import com.krld.myrts.controller.actions.MeleeSoldierBehaviour;
import com.krld.myrts.controller.actions.RangeSoldierBehaviour;
import com.krld.myrts.controller.move.AStarMoveBehavior;
import com.krld.myrts.model.Player;
import com.krld.myrts.model.RTSWorld;
import com.krld.myrts.model.Unit;
import com.krld.myrts.model.UnitType;

/**
 * Created by Andrey on 6/17/2014.
 */
public class DefaultUnitFabric implements AbsractUnitFabric {
    private RTSWorld rtsWorld;

    public DefaultUnitFabric(RTSWorld rtsWorld) {
        setRtsWorld(rtsWorld);
    }

    @Override
    public Unit createSoldier(int x, int y, Player player) {
        Unit unit = new Unit(x, y);
        unit.setType(UnitType.SOLDIER);
        unit.setRtsWorld(getRtsWorld());
        unit.setMoveBehavior(new AStarMoveBehavior());
        unit.setPlayer(player);
        unit.setActionBehavior(new MeleeSoldierBehaviour());
        unit.setMaxHp(100);
        unit.setHp(unit.getMaxHp());
        unit.getActionBehavior().setDefaultDamage(10);
        return unit;
    }

    @Override
    public Unit createTrooper(int x, int y, Player player) {
        Unit trooper = new Unit(x, y);
        trooper.setType(UnitType.TROOPER);
        trooper.setRtsWorld(getRtsWorld());
        trooper.setMoveBehavior(new AStarMoveBehavior());
        trooper.setPlayer(player);
        trooper.setActionBehavior(new RangeSoldierBehaviour());
        trooper.setMaxHp(100);
        trooper.setHp(trooper.getMaxHp());
        trooper.getActionBehavior().setDefaultDamage(10);
        trooper.getActionBehavior().setRangeAttack(10f);
        return trooper;
    }

    @Override
    public Unit createWorker(int x, int y, Player player) {
        return null;
    }

    @Override
    public Unit create(UnitType type, int x, int y, Player player) {
        if (type == UnitType.SOLDIER) {
            return createSoldier(x, y, player);
        } else if (type == UnitType.UNDEAD_SOLDIER) {
            return createUndeadSoldier(x, y, player);
        } else if (type == UnitType.TROOPER) {
            return createTrooper(x, y, player);
        }
        return null;
    }

    public Unit createUndeadSoldier(int x, int y, Player player) {
        Unit unit = new Unit(x, y);
        unit.setType(UnitType.UNDEAD_SOLDIER);
        unit.setRtsWorld(getRtsWorld());
        unit.setMoveBehavior(new AStarMoveBehavior());
        unit.setPlayer(player);
        unit.setActionBehavior(new MeleeSoldierBehaviour());
        unit.setMaxHp(70);
        unit.setHp(unit.getMaxHp());
        unit.getActionBehavior().setDefaultDamage(8);
        return unit;
    }

    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public RTSWorld getRtsWorld() {
        return rtsWorld;
    }
}
