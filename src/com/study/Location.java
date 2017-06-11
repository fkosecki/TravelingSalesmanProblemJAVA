package com.study;

public class Location{
    private int ID;
    private double X;
    private double Y;
    private String tagName = null;

    public Location(int ID, double X, double Y){
        this.ID = ID;
        this.X = X;
        this.Y = Y;
    }

    public Location(int ID, double X, double Y, String tagName){
        this.ID = ID;
        this.X = X;
        this.Y = Y;
        this.tagName = tagName;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getID() {
        return ID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }
}
