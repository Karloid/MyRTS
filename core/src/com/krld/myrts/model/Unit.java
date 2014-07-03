package com.krld.myrts.model;

import com.krld.myrts.controller.ActionBehaviour;
import com.krld.myrts.controller.move.MoveBehavior;

/**
 * Created by Andrey on 6/17/2014.
 */
public class Unit {
    private static int curId = 0;
    private Point pos;
    private Player player;
    private Direction direction;
    private ActionType action;
    private RTSWorld rtsWorld;
    private final int id;
    private ActionBehaviour actionBehavior;
    private int maxHp;
    private Point actionPoint;
    private float rangeAttack;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    private int hp;

    public Point getPos() {
        return pos;
    }

    private UnitType type;

    public MoveBehavior getMoveBehavior() {
        return moveBehavior;
    }

    public void setMoveBehavior(MoveBehavior moveBehavior) {
        this.moveBehavior = moveBehavior;
        moveBehavior.setUnit(this);
        moveBehavior.setRtsWorld(rtsWorld);
    }

    private MoveBehavior moveBehavior;

    public Unit(int x, int y) {
        pos = new Point(x, y);
        direction = Direction.SELF;
        action = ActionType.NOTHING;
        id = curId++;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public UnitType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDestMovePoint(Point point) {
        getActionBehavior().actionOnPoint(point);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public ActionType getAction() {
        return action;
    }

    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public RTSWorld getRtsWorld() {
        return rtsWorld;
    }

    public void update() {
        actionBehavior.update();

    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setActionBehavior(ActionBehaviour actionBehavior) {
        this.actionBehavior = actionBehavior;
        actionBehavior.setUnit(this);
    }

    public ActionBehaviour getActionBehavior() {
        return actionBehavior;
    }

    public void receiveDamage(int damageAmount) {
        hp -= damageAmount;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }


    public void setActionPoint(Point actionPoint) {
        this.actionPoint = actionPoint;
    }

    public Point getActionPoint() {
        return actionPoint;
    }

}
