package com.zip.app.Engine.Vector;

import java.util.Locale;

public class Point2D {
    public double x, y, w;
    public Point2D() {
        this.x = y = 0.0f;
        this.w = 1.0f;
    }
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.w = 1.0f;
    }
    public Point2D(double x, double y, double w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }
    public Point2D(Vec2D v) {
        this.x = v.x;
        this.y = v.y;
        this.w = 1.0f;
    }
    public Point2D(Point2D p) {
        this.x = p.x;
        this.y = p.y;
        this.w = p.w;
    }
    public Point2D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.w = p.w;
    }
    public Point2D(double[] array) {
        x = array[0];
        y = array[1];
        w = array[3];
    }
    public Point2D mul(Mat3 mat) {
        Point2D res = new Point2D();
        res.x = mat.mat[0][0] * x + mat.mat[1][0] * y + mat.mat[2][0] * w;
        res.y = mat.mat[0][1] * x + mat.mat[1][1] * y + mat.mat[2][1] * w;
        res.w = mat.mat[0][2] * x + mat.mat[1][2] * y + mat.mat[2][2] * w;
        return res;
    }
    public Point2D add(Point2D p) {
        return new Point2D(x + p.x, y + p.y, w + p.w);
    }
    public Point2D add(Vec2D v) {
        return new Point2D(x + v.x, y + v.y, w);
    }
    public Point2D mul(double f) {
        return new Point2D(x * f, y * f, w * f);
    }
    public Vec2D dehomog() {
        if (w == 0.0f)
            return new Vec2D(0,0);
        return new Vec2D(x / w, y / w);
    }
    public Vec2D ignoreW() {
        return new Vec2D(x, y);
    }
    @Override
    public String toString() {
        return String.format(Locale.US, "(%4.1f,%4.1f,%4.1f)",x,y,w);
    }
    public String toString(String format) {
        return String.format(Locale.US, "("+format+","+format+","+format+")",x,y,w);
    }
}