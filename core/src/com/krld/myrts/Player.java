package com.krld.myrts;

/**
 * Created by Andrey on 6/18/2014.
 */
public class Player {
    private final String name;
    static private int curId = 0;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private final int id;

    public Player(String name) {
        this.name = name;
        curId++;
        this.id = curId;
    }
}
