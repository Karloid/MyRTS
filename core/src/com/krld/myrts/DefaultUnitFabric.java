package com.krld.myrts;

/**
 * Created by Andrey on 6/17/2014.
 */
public class DefaultUnitFabric implements AbsractUnitFabric {
    @Override
    public Unit createSoldier(int x, int y) {
        Unit unit = new Unit(x,y);
        unit.setType(UnitType.SOLDIER);
        unit.setMoveBehavior(new StandartMoveBehavior());
        return unit;
    }
}
