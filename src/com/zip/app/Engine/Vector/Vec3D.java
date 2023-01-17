package com.zip.app.Engine.Vector;
import java.util.Locale;
public class Vec3D {
    public double x, y, z;
    public Vec3D() {
        x = y = z = 0.0f;
    }
    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3D(Vec3D vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }
    public Vec3D(Point3D point) {
        x = point.x;
        y = point.y;
        z = point.z;
    }
    public Vec3D(double[] array) {
        x = array[0];
        y = array[1];
        z = array[2];
    }
    public Vec3D add(Vec3D v) {
        return new Vec3D(x + v.x, y + v.y, z + v.z);
    }
    public Vec3D mul(double d) {
        return new Vec3D(x * d, y * d, z * d);
    }
    public Vec3D mul(Mat3 m) {
        Vec3D res = new Vec3D();
        res.x = m.mat[0][0] * x + m.mat[1][0] * y + m.mat[2][0] * z;
        res.y = m.mat[0][1] * x + m.mat[1][1] * y + m.mat[2][1] * z;
        res.z = m.mat[0][2] * x + m.mat[1][2] * y + m.mat[2][2] * z;
        return res;
    }
    public Vec3D mul(Quat q) {
        Quat p = new Quat(0, x, y, z);
        p = q.mulR(p).mulR(q.inv());
        Vec3D oVec = new Vec3D(p.i, p.j, p.k);
        return oVec;
    }
    public Vec3D mul(Vec3D v) {
        return new Vec3D(x * v.x, y * v.y, z * v.z);
    }
    public double dot(Vec3D rhs) {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }
    public Vec3D cross(Vec3D v) {
        return new Vec3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y
                * v.x);
    }
    public Vec3D normalized() {
        double len = length();
        if (len == 0.0f)
            return new Vec3D(0, 0, 0);
        return new Vec3D(x / len, y / len, z / len);
    }
    public Vec3D reversed() {
        return new Vec3D(-x, -y, -z);
    }
    public double length() {
        return (double) Math.sqrt((double) (x * x + y * y + z * z));
    }
    public String toString() {
        return String.format(Locale.US, "(%4.1f,%4.1f,%4.1f)", x, y, z);
    }
    public String toString(String format) {
        return String.format(Locale.US, "(" + format + "," + format + "," + format + ")",
                x, y, z);
    }
}