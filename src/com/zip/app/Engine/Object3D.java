package com.zip.app.Engine;

import com.zip.app.Engine.Vector.Mat4;
import com.zip.app.Engine.Vector.Mat4Identity;
import com.zip.app.Engine.Vector.Point3D;

public class Object3D {
    public Point3D[] points;
    public Point3D location;
    public Point3D rotation;
    public int[][] faces;
    public Point3D[] projected_points;
    public Mat4 transformMatrix = new Mat4Identity();
    int id = 0;
    public Object3D(Point3D location, Point3D rotation, Point3D[] points, int[][] faces) {
        //init object parts
        this.location = location;
        this.rotation = rotation;
        this.points = new Point3D[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new Point3D(points[i].x,points[i].y,points[i].z);
        }
        this.faces = faces;
        this.projected_points = this.points.clone();
    }
}