package com.krld.myrts.controller;

import com.krld.myrts.model.Player;
import com.krld.myrts.model.Unit;

/**
 * Created by Andrey on 6/17/2014.
 */
public interface AbsractUnitFabric {
    Unit createSoldier(int x, int y, Player player);
    Unit createWorker(int x, int y, Player player);
}
