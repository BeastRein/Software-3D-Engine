package com.zip.app.Engine;

import com.zip.app.Engine.Vector.Mat4;
import com.zip.app.Engine.Vector.Point3D;
import com.zip.app.Engine.Vector.Vec3D;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Render {
    public int[][] Zbuffer = null;
    public int[][] Framebuffer = null;
    public Render() {

    }
    public void refreshFramebuffer(int width, int height) {
        this.Framebuffer = new int[width][height];
        this.Zbuffer = new int[width][height];
    }
    public void drawTriangle(Point3D a, Point3D b, Point3D c, BufferedImage image, Color color) {
        Polygon polygon = new Polygon(new int[] {(int)a.x,(int)b.x,(int)c.x,(int)a.x}, new int[] {(int)a.y,(int)b.y,(int)c.y,(int)a.y}, 4);
        double[] plane = Util.createPlane(a.x, b.x, c.z, a.y, b.y, c.y, a.z, b.z, c.z);
        for (int y = (int)polygon.getBounds2D().getMinY(); y < polygon.getBounds2D().getMinY() + polygon.getBounds2D().getHeight(); y++) {
            if((y < 0) || (y >= 600)) continue;
            for (int x = (int) polygon.getBounds2D().getMinX(); x < polygon.getBounds2D().getMinX() + polygon.getBounds2D().getWidth(); x++) {
                if((x < 0) || (x >= 800)) continue;
                double dstZ = Zbuffer[x][y];
                double srcZ = Util.resolveZ(x, y, plane);
                if ((srcZ > 0.5)) {
                    if (Util.isInside((int) a.x, (int) a.y, (int) b.x, (int) b.y, (int) c.x, (int) c.y, x, y)) {
                        if (srcZ < dstZ) {
                            Zbuffer[x][y] = (int) srcZ;
                            Framebuffer[x][y] = color.getRGB();
                            image.setRGB(x, y, color.getRGB());
                        } else if (srcZ == dstZ) {
                            Framebuffer[x][y] = color.getRGB();
                            image.setRGB(x, y, color.getRGB());
                        }
                    }
                }
            }
        }
    }
    public BufferedImage renderObject(Object3D object, Color color, Camera camera, BufferedImage out) {
        if (object != null) {
            Object3D obj = new Object3D(new Point3D(0, 0, 0), new Point3D(0, 0, 0), object.points, object.faces);
            if (obj != null) {
                for (var p = 0; p < obj.points.length; p++) {
                    obj.points[p].x = (512 * (obj.points[p].x + obj.location.x / 2)) / (obj.points[p].z + obj.location.z);
                    obj.points[p].y = (512 * (obj.points[p].y + obj.location.y / 2)) / (obj.points[p].z + obj.location.z);
                    obj.points[p].z = (512 * (obj.points[p].z + obj.location.z / 2));
                }
                for (var p = 0; p < obj.faces.length; p++) {

                }
            }
        }
        return out;
    }
}
