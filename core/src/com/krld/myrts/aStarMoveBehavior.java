package com.krld.myrts;

import java.util.*;

/**
 * Created by Andrey on 6/17/2014.
 */
public class aStarMoveBehavior implements MoveBehavior {

    private static final int START_NODE = 1;
    private static final int COMMON_NODE = 0;
    private static final double RANDOM_WAY_RATIO = 0f;
    private static final double MOVE_COST = 1;
    private static final boolean BREAK_TIES = true;
    private static final int MAX_LENGTH_PATH = 100;
    private static final long WAITING_ASTAR_TIME = 100;
    private Unit unit;
    private RTSWorld rtsWorld;
    private PriorityQueue<Node> openNodes;
    private ArrayDeque<Node> closedNodes;
    private Point goalPosition;
    private Node startNode;
    private List<Point> path;
    private boolean aStarWorking;

    @Override
    public void update() {
        //    System.out.println("AStar update");
        //    if (true) return;
/*        if (goalPosition == null || unit.getPos().equals(goalPosition) || unit.getPos().equals(path.get(path.size() - 1))) {
            if (unit.getPos().equals(goalPosition)) {
        *//*        if (unit.getDestination() == AntDestination.TO_NEST) {
                    unit.setDestination(AntDestination.FROM_NEST);
                    unit.getNest().antArrive(unit);
                } else {
                    unit.setDestination(AntDestination.TO_NEST);
                    WayPoint wayPoint = rtsWorld.getWayPointByPosition(unit.getPosition());
                    if (wayPoint != null)
                        wayPoint.antArrive(unit);
                }*//*
            }
            //  goalPosition = pickGoalPosition();
            //    aStarCalc();
            runOnPath();
        } else {
            runOnPath();
        }*/
        runOnPath();
        if (goalPosition != null && !goalPositionReached(3) && path == null) {
            runAStarCalcInThread();
        }
        if (goalPosition != null && goalPositionReached(1)) {
            goalPosition = null;
            unit.setAction(ActionType.NOTHING);
        }
    }

    private boolean goalPositionReached(int distance) {
        return getManhattanDistance(unit.getPos(), goalPosition, false) <= distance;
    }

    private void runOnPath() {
        if (path == null) {
            return;
        }
        if (aStarWorking) {
            return;
        }
        int indexCurrentPos = 0;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).equals(unit.getPos())) {
                indexCurrentPos = i;
                break;
            }
        }
        if (indexCurrentPos + 1 >= path.size()) {
            path = null;
            return;
        }
        Point nextPoint = path.get(indexCurrentPos + 1);
        Direction direction = rtsWorld.getDirectionByPoints(unit.getPos(), nextPoint);
        if (direction == null) {
            System.out.println(" directions is null");
            // dirt
            path = null;
         /*   List<Point> newPath = new ArrayList<Point>();
            newPath.add(new Point((unit.getPos().getX() + path.get(0).getX()) / 2, (unit.getPos().getY() + path.get(0).getY()) / 2));
            newPath.addAll(path);
            path = newPath;*/
           // path.add(0, new Point((unit.getPos().getX() + path.get(0).getX()) / 2, (unit.getPos().getY() + path.get(0).getY()) / 2));
        }
        //      if (direction != null)
        unit.setDirection(direction);
        // unit.setAction(ActionType.MOVE);
    }

    private void aStarCalc() {

        closedNodes = new ArrayDeque<Node>();
        openNodes = new PriorityQueue<Node>(300, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() < o2.getF()) {
                    return -1;
                } else if (o1.getF() == o2.getF()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        startNode = new Node(unit.getPos(), START_NODE);
        calcF(startNode);
        openNodes.add(startNode);
        while (!openNodes.peek().getPosition().equals(goalPosition) && !(openNodes.peek().getParentsCount() > MAX_LENGTH_PATH)) {
            Node current = openNodes.peek();
            openNodes.remove(current);
            closedNodes.add(current);
            for (Node neighbor : getNeighbors(current)) {
                double costG = current.getG() + MOVE_COST;
                if (openNodes.contains(neighbor) && costG < neighbor.getG()) {
                    openNodes.remove(neighbor);
                }
                if (closedNodes.contains(neighbor) && costG < neighbor.getG()) {
                    closedNodes.remove(neighbor);
                }
                if (!openNodes.contains(neighbor) && !closedNodes.contains(neighbor)) {
                    neighbor.setG(costG);
                    neighbor.recalcF();
                    openNodes.add(neighbor);
                    //        sortOpenNodes();
                    neighbor.setParent(current);
                }
            }
        }
        //    System.out.println("done AStar");
        //   System.out.println("" + openNodes.get(0).printParents());
        calcPath();
    }

    private void calcPath() {
        path = new ArrayList<Point>();
        Node node = openNodes.peek();
        while (true) {
            path.add(node.getPosition());
            node = node.getParent();
            if (node == null) {
                break;
            }
        }
        Collections.reverse(path);
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        Direction direction = Direction.UP;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.LEFT;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.RIGHT;
        addPointIfCan(direction, node, neighbors);
        direction = Direction.DOWN;
        addPointIfCan(direction, node, neighbors);
        return neighbors;
    }

    private void addPointIfCan(Direction direction, Node node, List<Node> neighbors) {
        Point point = node.getPosition().getCopy();
        rtsWorld.movePointOnDirection(direction, point);
        if (rtsWorld.canMoveToPoint(point, false)) {

            Node nodeToAdd = findNodeByPosition(point);
            if (nodeToAdd == null) {
                nodeToAdd = new Node(point, COMMON_NODE);
                nodeToAdd.setParent(node);
                calcF(nodeToAdd);
            }
            neighbors.add(nodeToAdd);

        }
    }

    private Node findNodeByPosition(Point point) {
        for (Node node : openNodes) {
            if (node.getPosition().equals(point)) {
                return node;
            }
        }
        for (Node node : closedNodes) {
            if (node.getPosition().equals(point)) {
                return node;
            }
        }
        return null;
    }

    private void calcF(Node node) {
        double heuristik = getManhattanDistance(node.getPosition(), goalPosition, true);
        double pathCost = (node.getParent() == null ? 0 : node.getParent().getG() + MOVE_COST);
        double f = heuristik + pathCost;
        node.setF(f);
        node.setG(pathCost);
        node.setH(heuristik);
    }

    private void sortOpenNodes() {
      /*  Collections.sort(openNodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getF() < o2.getF()) {
                    return -1;
                } else if (o1.getF() == o2.getF()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });*/
    }

/*    private Point pickGoalPosition() {
        if (unit.getDestination() == AntDestination.FROM_NEST) {
            Double minDistantce = null;
            WayPoint pickedWayPoint = null;
            for (WayPoint wayPoint : rtsWorld.getWayPoints()) {
                if ((minDistantce == null || getManhattanDistance(unit.getPosition(), wayPoint.getPosition()) < minDistantce) && Math.random() > RANDOM_WAY_RATIO) {
                    minDistantce = getManhattanDistance(unit.getPosition(), wayPoint.getPosition());
                    pickedWayPoint = wayPoint;
                }
            }
            return pickedWayPoint.getPosition().getCopy();
        } else if (unit.getDestination() == AntDestination.TO_NEST) {
            return unit.getNest().getPosition();
        }
        return new Point(10, 10);
    }*/

    public double getManhattanDistance(Point position, Point position1, boolean breakTiesParam) {
        double dx = Math.abs(position.getX() - position1.getX());
        double dy = Math.abs(position.getY() - position1.getY());
        if (breakTiesParam && BREAK_TIES && startNode != null) {
            double dx1 = position.getX() - position1.getX();
            double dy1 = position.getY() - position1.getY();
            double dx2 = startNode.getPosition().getX() - position1.getX();
            double dy2 = startNode.getPosition().getY() - position1.getY();
            double cross = Math.abs(dx1 * dy2 - dx2 * dy1);
            return MOVE_COST * (dx + dy) + cross * 0.001d;
        } else {
            return MOVE_COST * (dx + dy);
        }
    }

    @Override
    public void setRtsWorld(RTSWorld rtsWorld) {
        this.rtsWorld = rtsWorld;
    }

    public Point getGoalPosition() {
        return goalPosition;
    }

    public PriorityQueue<Node> getOpenNodes() {
        return openNodes;
    }

    public ArrayDeque<Node> getClosedNodes() {
        return closedNodes;
    }

    public List<Point> getPath() {
        return path;
    }

    public class Node {
        private final int nodeType;
        private double f;
        private double g;
        private double h;
        private Node parent;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (!position.equals(node.position)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }

        public Point getPosition() {
            return position;
        }

        private final Point position;

        public Node(Point position, int nodeType) {
            this.nodeType = nodeType;
            this.position = position;
        }

        public double getF() {
            return f;
        }

        public void setF(double f) {
            this.f = f;
        }

        public void setG(double g) {
            this.g = g;
        }

        public double getG() {
            return g;
        }

        public void setH(double h) {
            this.h = h;
        }

        public double getH() {
            return h;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "f: " + getF() + "; g: " + getG() + "; h: " + getH() + "; pos: " + getPosition();
        }

        public void recalcF() {
            setF(getG() + getH());
        }

        public String printParents() {
            if (getParent() != null) {
                return this + " > " + getParent().printParents();
            } else {
                return this.toString();
            }
        }

        public int getParentsCount() {
            int count = 0;
            Node node = this;
            while (true) {
                node = node.getParent();
                if (node == null) {
                    return count;
                }
                count++;
            }
        }
    }

    @Override
    public void setDestMovePoint(Point point) {
        if (!rtsWorld.canMoveToPoint(point, false)) {
            return;
        }
        goalPosition = point;
        path = null;
        runAStarCalcInThread();
        //aStarCalc();
        unit.setAction(ActionType.MOVE);
    }

    private void runAStarCalcInThread() {
        while (aStarWorking) {
            try {
                Thread.sleep(WAITING_ASTAR_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread runner = new Thread(new Runnable() {
            @Override
            public void run() {
                aStarWorking = true;
              //  path = null;
                try {
                    aStarCalc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                aStarWorking = false;
            }
        });
        runner.start();

    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}
