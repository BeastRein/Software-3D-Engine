package com.zip.app.engine;

public class point {
    public double x;
    public double y;
    public double z;
    public boolean visible;
    public point(double x, double y, double z) {
        //initialize point data
        this.x = x;
        this.y = y;
        this.z = z;
        this.visible = true;
    }
}