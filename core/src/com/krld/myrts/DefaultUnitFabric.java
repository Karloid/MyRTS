package com.krld.myrts;

/**
 * Created by Andrey on 6/17/2014.
 */
public class DefaultUnitFabric implements AbsractUnitFabric {
    private com.krld.myrts.RTSWorld rtsWorld;

    public DefaultUnitFabric(RTSWorld rtsWorld) {
        setRtsWorld(rtsWorld);
    }

    @Override
    public Unit createSoldier(int x, int y, Player player) {
        Unit unit = new Unit(x, y);
        unit.setType(UnitType.SOLDIER);
        unit.setRtsWorld(getRtsWorld());
        unit.setMoveBehavior(new aStarMoveBehavior());
        unit.setPlayer(player);
        return unit;
    }

    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public RTSWorld getRtsWorld() {
        return rtsWorld;
    }
}
