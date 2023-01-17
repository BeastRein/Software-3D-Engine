package com.zip.app.Engine.Vector;

import java.util.Locale;
public class Vec2D {
    public double x, y;
    public Vec2D() {
        x = y = 0.0f;
    }
    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vec2D(Vec2D v) {
        x = v.x;
        y = v.y;
    }
    public Vec2D add(Vec2D v) {
        return new Vec2D(x + v.x, y + v.y);
    }
    public Vec2D mul(double d) {
        return new Vec2D(x * d, y * d);
    }
    public Vec2D mul(Vec2D v) {
        return new Vec2D(x * v.x, y * v.y);
    }
    public double dot(Vec2D v) {
        return x * v.x + y * v.y;
    }
    public Vec2D normalized() {
        double l = length();
        if (l == 0.0f)
            return null;
        return new Vec2D(x / l, y / l);
    }
    public Vec2D reversed() {
        return new Vec2D(-x, -y);
    }
    public double length() {
        return (double) Math.sqrt((double) (x * x + y * y));
    }
    public String toString() {
        return String.format(Locale.US, "(%4.1f,%4.1f)", x, y);
    }
    public String toString(String format) {
        return String.format(Locale.US, "(" + format + "," + format + ")",	x, y);
    }
}
