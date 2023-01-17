package com.zip.app.Engine;

import com.zip.app.Engine.Vector.*;

public class Camera {
    public Point3D location;
    Point3D rotation;
    public Mat4 sceneMatrix;
    public void computeMatrix() {
        Vec3D eyeVector = new Vec3D((double) (Math.cos(rotation.x) * Math.cos(rotation.y)),
                (double) (Math.sin(rotation.x) * Math.cos(rotation.y)), (double) Math
                .sin(rotation.y));
        Vec3D pos = new Vec3D(location);
        Vec3D eye = pos.add(eyeVector.mul(-1 * 1.0f));
        this.sceneMatrix = new Mat4ViewRH(pos, eyeVector.mul(1.0f), new Vec3D(
                (double) (Math.cos(rotation.x) * Math
                        .cos(rotation.y + Math.PI / 2)), (double) (Math
                .sin(rotation.x) * Math.cos(rotation.y + Math.PI / 2)),
                (double) Math.sin(rotation.y + Math.PI / 2)));
        Mat4 perspMatrix = new Mat4PerspRH(Math.PI/4.0,0.75, 0.1, 200.0);
        this.sceneMatrix = this.sceneMatrix.mul(perspMatrix);
    }
    Camera(Point3D location, Point3D rotation) {
        this.location = location;
        this.rotation = rotation;
        this.sceneMatrix = new Mat4Identity();
    }
    public void move(float angle, float distance) {
        this.location.x = this.location.x - ((float)distance/100)*(Math.sin(this.rotation.x + Math.toRadians(angle)));
        this.location.z = this.location.z + ((float)distance/100)*(Math.cos(this.rotation.x + Math.toRadians(angle)));
    }
    public void turn(Point3D angle) {
        this.rotation.x += Math.toRadians(angle.x);
        this.rotation.y += Math.toRadians(angle.x);
        this.rotation.z += Math.toRadians(angle.x);
    }
}

