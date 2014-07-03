package com.krld.myrts.controller;

import com.krld.myrts.model.Point;
import com.krld.myrts.model.Unit;

/**
 * Created by Andrey on 6/19/2014.
 */
public interface ActionBehaviour {
    void setUnit(Unit unit);

    void actionOnPoint(Point point);

    void update();

    int getDamageAmount();

    void setDefaultDamage(int damage);

    void setRangeAttack(float range);
}
