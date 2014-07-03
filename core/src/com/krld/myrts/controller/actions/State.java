package com.krld.myrts.controller.actions;

import com.krld.myrts.model.Point;
import com.krld.myrts.model.Unit;

/**
 * Created by Andrey on 7/3/2014.
 */
public interface State {
    void actionOnEnemyUnit(Unit unit);

    void actionOnPoint(Point point);

    void update();
}
