package com.krld.myrts.controller;

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
    public Unit createWorker(int x, int y, Player player) {
        return null;
    }

    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public RTSWorld getRtsWorld() {
        return rtsWorld;
    }
}
