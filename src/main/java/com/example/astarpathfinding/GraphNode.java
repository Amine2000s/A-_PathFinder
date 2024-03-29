package com.example.astarpathfinding;

import javafx.scene.shape.Rectangle;

public class GraphNode implements Comparable<GraphNode> {

    private Rectangle rectangle;
    private double x;//position x
    private double y;//position y
    private double h;//heurisitic
    private boolean visited = false;

    private int i;//general position kinda like a matrix ( 0,0) or (0,1)
    private int j;

    public GraphNode(Rectangle rectangle, int i, int j, double h) {
        this.rectangle = rectangle;
        x = rectangle.getX();
        y = rectangle.getY();

        this.i = i;
        this.j = j;
        this.h = h;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "GraphNode{"+
                "x=" + x +
                ", y=" + y +
                ", h=" + h +
                ", visited=" + visited +
                ", i=" + i +
                ", j=" + j +
                '}';
    }

    @Override
    public int compareTo(GraphNode o) {
        return Double.compare(h, o.h);
    }
}

