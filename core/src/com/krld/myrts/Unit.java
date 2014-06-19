package com.krld.myrts;

/**
 * Created by Andrey on 6/17/2014.
 */
public class Unit {
    private static int curId = 0;
    private Point pos;
    private Player player;
    private Direction direction;
    private ActionType action;
    private com.krld.myrts.RTSWorld rtsWorld;
    private int id;

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

        getMoveBehavior().setDestMovePoint(point);
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
        moveBehavior.update();
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }
}
