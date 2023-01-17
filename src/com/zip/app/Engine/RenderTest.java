package com.zip.app.Engine;

import com.zip.app.Engine.Vector.Mat4;
import com.zip.app.Engine.Vector.Point3D;
import com.zip.app.Engine.Vector.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class RenderTest {
    protected final Vec3D switchCoordsVec = new Vec3D(1.0, -1.0, 1.0);
    protected final Vec3D translateVec = new Vec3D(1.0, 1.0, 0.0);
    protected Vec3D transfVec;

    public void clearCanvas() {
        //this.FrameBuffer.setBgColor(this.FrameBuffer.getBgColor());
    }

    //complexrenderer

    private static final int objectTopology = 1;
    private static final double W_MIN = 0.5;
    private double[][] zBuffer;
    private static final double Z_BUFFER_MAX_VAL = 1.0;
    BufferedImage FrameBuffer;
    JFrame Frame;

    public RenderTest() {

    }

    private void renderTriangle(Point3D a, Point3D b, Point3D c, Color color, Object3D object, Camera camera) {
        a = Util.rotate(new Point3D(a.x + camera.location.x + object.location.x, a.y + camera.location.y + object.location.y, a.z + camera.location.z + object.location.z), camera.rotation);
        b = Util.rotate(new Point3D(b.x + camera.location.x + object.location.x, b.y + camera.location.y + object.location.y, b.z + camera.location.z + object.location.z), camera.rotation);
        c = Util.rotate(new Point3D(c.x + camera.location.x + object.location.x, c.y + camera.location.y + object.location.y, c.z + camera.location.z + object.location.z), camera.rotation);

        if (b.w > a.w) { // A - B comparison
            Point3D tmpB = b;
            b = a;
            a = tmpB;
        }
        if (c.w > b.w) { // B - C comparison
            Point3D tmpC = c;
            c = b;
            b = tmpC;
        }
        if (b.w > a.w) { // (B - C) - A comparison
            Point3D tmpA = a;
            a = b;
            b = tmpA;
        }
        Point3D[] o1;
        Point3D[] o2 = null;
        //System.out.println("Step 1");
        if (a.w < W_MIN) { // Triangle is not visible
            return;
        } else if (c.w > W_MIN) { // Whole triangle is visible
            o1 = new Point3D[]{a, b, c};
        } else if (b.w > W_MIN) { // Triangle is partially visible
            double t = (b.w - W_MIN) / (b.w - c.w);
            Point3D vA = b.mul(1.0 - t).add(c.mul(t));
            t = (a.w - W_MIN) / (a.w - c.w);
            Point3D vB = a.mul(1.0 - t).add(c.mul(t));
            o1 = new Point3D[]{a, b, vA};
            o2 = new Point3D[]{a, vA, vB};
        } else { // Triangle is partially visible
            double t = (a.w - W_MIN) / (a.w - b.w);
            Point3D vA = a.mul(1.0 - t).add(b.mul(t));
            t = (a.w - W_MIN) / (a.w - c.w);
            Point3D vB = a.mul(1.0 - t).add(c.mul(t));
            o1 = new Point3D[]{a, vA, vB};
        }
        //System.out.println("Step 2");
        rasterizeTriangle(o1[0], o1[1], o1[2], color);
        if (o2 != null) {
            rasterizeTriangle(o2[0], o2[1], o2[2], color);
        }
    }

    private void rasterizeTriangle(Point3D a, Point3D b, Point3D c, Color color) {
        Vec3D vecA = a.dehomog();
        Vec3D vecB = b.dehomog();
        Vec3D vecC = c.dehomog();
        vecA = vecA.mul(switchCoordsVec).add(translateVec).mul(transfVec);
        vecB = vecB.mul(switchCoordsVec).add(translateVec).mul(transfVec);
        vecC = vecC.mul(switchCoordsVec).add(translateVec).mul(transfVec);
        // We need to sort points by the value of y-coord.
        // Such that a.y < b.y < c.y
        if (vecA.y > vecB.y) {
            Vec3D tmpVec = vecA;
            vecA = vecB;
            vecB = tmpVec;
        }
        if (vecB.y > vecC.y) {
            Vec3D tmpVec = vecB;
            vecB = vecC;
            vecC = tmpVec;
        }
        if (vecA.y > vecB.y) {
            Vec3D tmpVec = vecA;
            vecA = vecB;
            vecB = tmpVec;
        }
        //System.out.println("Step 3");
        rasterizePartially(vecA, vecC, vecA, vecB, color, a, b, c); // A -> B
        rasterizePartially(vecA, vecC, vecB, vecC, color, a, b, c); // B -> C
    }

    private void rasterizePartially(Vec3D vecA, Vec3D vecC, Vec3D upperBound, Vec3D lowerBound, Color color, Point3D a, Point3D b, Point3D c) {
        //System.out.println("Step 4");
        for (int y = Math.max((int) upperBound.y + 1, 0); y <= Math.min(lowerBound.y, this.FrameBuffer.getHeight() - 1); y++) {
            //System.out.println("Step 4");
            double s1 = (y - upperBound.y) / (lowerBound.y - upperBound.y);
            double x1 = upperBound.x * (1.0 - s1) + lowerBound.x * s1;
            double z1 = upperBound.z * (1.0 - s1) + lowerBound.z * s1;
            double s2 = (y - vecA.y) / (vecC.y - vecA.y);
            double x2 = vecA.x * (1.0 - s2) + vecC.x * s2;
            double z2 = vecA.z * (1.0 - s2) + vecC.z * s2;
            // x1 must be smaller than x2
            // If x2 is greater than x1, switch them (+ associated z)
            if (x1 > x2) {
                double tmpX1 = x1;
                x1 = x2;
                x2 = tmpX1;
                double tmpZ1 = z1;
                z1 = z2;
                z2 = tmpZ1;
            }
            double t;
            double z;
            //System.out.println("Step 5");
            for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, this.FrameBuffer.getWidth() - 1); x++) {
                //System.out.println("Step 6");
                t = (x - x1) / (x2 - x1);
                z = z1 * (1.0 - t) + z2 * t;
                // Visibility check
                //System.out.println("ZBUFF: "+ zBuffer[x][y] + ", Z:"+z);
                if (z < zBuffer[x][y] && z >= 0.0) {
                    //System.out.println("Step 7");
                    if (Util.isInside((int) a.x, (int) a.y, (int) b.x, (int) b.y, (int) c.z, (int) c.y, x, y)) {
                        zBuffer[x][y] = z;
                        this.FrameBuffer.setRGB(x, y, Color.RED.getRGB());
                    }
                }
            }
        }
    }

    public void refreshZBuffer() {
        // Performance improvement - iterate over columns first.
        for (int y = 0; y < zBuffer[0].length; y++) {
            for (int x = 0; x < zBuffer.length; x++) {
                zBuffer[x][y] = Double.MAX_VALUE;
            }
        }
    }

    public BufferedImage renderObject(Object3D object, Color color, Camera camera, BufferedImage FrameBuffer) {
        this.FrameBuffer = FrameBuffer;
        this.transfVec = new Vec3D((this.FrameBuffer.getWidth() - 1) / 2.0, (this.FrameBuffer.getHeight() - 1) / 2.0, 1.0);
        this.zBuffer = new double[FrameBuffer.getWidth()][FrameBuffer.getHeight()];
        refreshZBuffer();
        int colorIndex = 0;
        //camera.computeMatrix();
        for (int i = 0; i < object.faces.length; i++) {
            renderTriangle(new Point3D(object.points[object.faces[i][0]].x, object.points[object.faces[i][0]].y, object.points[object.faces[i][0]].z),
                    new Point3D(object.points[object.faces[i][1]].x, object.points[object.faces[i][1]].y, object.points[object.faces[i][1]].z),
                    new Point3D(object.points[object.faces[i][2]].x, object.points[object.faces[i][2]].y, object.points[object.faces[i][2]].z),
                    new Color(colorIndex),
                    object,
                    camera);
            colorIndex++;
        }
        return this.FrameBuffer;
    }
}
