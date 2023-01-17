package com.zip.app.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Window extends JFrame {
    Point3D rot = new Point3D(0,Math.toRadians(10),0);
    Texture t1 = new Texture("unnamed.png");
    int[][] FrameBuffer;
    double[][] ZBuffer;
    int count = 0;
    public void drawfacetex(Point3D[] a) {
        rot.y += 0.005;
        count++;
        Point3D[] b = new Point3D[a.length];

        for (int i = 0; i < a.length; i++) {
            b[i] = Util.rotate(new Point3D(a[i].x, a[i].y, a[i].z), new Point3D(rot.x, rot.y, rot.z));
            b[i].x = (8 * (b[i].x + 400 / 2)) / ((b[i].z + 1000) / 100);
            b[i].y = (8 * (b[i].y + 400 / 2)) / ((b[i].z + 1000) / 100);
        }
        Point3D[] points;

        points = new Point3D[]{
                new Point3D(b[0].x, b[0].y, 0), //tl
                new Point3D(b[1].x, b[1].y, 0), //bl
                new Point3D(b[2].x, b[2].y, 0), //br
                new Point3D(b[3].x, b[3].y, 0)  //tr
        };
        BufferedImage tex = t1.project(points, getWidth(), getHeight());
        Polygon polygon = new Polygon(new int[]{(int) b[0].x, (int) b[1].x, (int) b[2].x, (int) b[3].x, (int) b[0].x}, new int[]{(int) b[0].y, (int) b[1].y, (int) b[2].y, (int) b[3].y, (int) b[0].y}, 4);
        double[] plane = Util.createPlane(b[0].x, b[1].x, b[2].x, b[0].y, b[1].y, b[2].y, b[0].z, b[1].z, b[2].z);
        double[] plane1 = Util.createPlane(b[0].x, b[3].x, b[2].x, b[0].y, b[3].y, b[2].y, b[0].z, b[3].z, b[2].z);
        polygon.contains(new Point(0, 0));

        for (int y = (int) polygon.getBounds().getMinY(); y < polygon.getBounds().getMaxY(); y++) {
            for (int x = (int) polygon.getBounds().getMinX(); x < polygon.getBounds().getMaxX(); x++) {
                if (polygon.contains(x, y)) {
                    double z = (Util.resolveZ(x, y, plane));
                    double z1 = (Util.resolveZ(x, y, plane1));
                    if (x > 0) {
                        if (y > 0) {
                            if (y < getHeight()) {
                                if (x < getWidth()) {
                                    if (z < ZBuffer[y][x]) {
                                        if (tex.getRGB(x, y) > Color.BLACK.getRGB()) {
                                            ZBuffer[y][x] = z;
                                            FrameBuffer[y][x] = tex.getRGB(x, y);
                                        }
                                    }
                                    if (z1 < ZBuffer[y][x]) {
                                        if (tex.getRGB(x, y) > Color.BLACK.getRGB()) {
                                            ZBuffer[y][x] = z1;
                                            FrameBuffer[y][x] = tex.getRGB(x, y);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void paint(Graphics g) {
        ZBuffer = new double[getHeight()][getWidth()];
        FrameBuffer = new int[getHeight()][getWidth()];
        for (int y = 0; y < ZBuffer.length; y++) {
            Arrays.fill(ZBuffer[y], Double.MAX_VALUE);
        }
        Graphics2D g2d = (Graphics2D) g;

        //cube
        Point3D[] points = new Point3D[]{
                new Point3D(-100, -100, -100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(100, 100, -100), //br
                new Point3D(100, -100, -100) //bl
        };
        drawfacetex(points);
        Point3D[] points1 = new Point3D[]{
                new Point3D(-100, -100, 100), //tl
                new Point3D(-100, 100, 100), //bl
                new Point3D(100, 100, 100), //br
                new Point3D(100, -100, 100) //bl
        };
        drawfacetex(points1);
        Point3D[] points2 = new Point3D[]{
                new Point3D(100, -100, -100), //tl
                new Point3D(100, 100, -100), //bl
                new Point3D(100, 100, 100), //br
                new Point3D(100, -100, 100) //bl
        };
        drawfacetex(points2);
        Point3D[] points3 = new Point3D[]{
                new Point3D(-100, -100, -100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(-100, 100, 100), //br
                new Point3D(-100, -100, 100) //bl
        };
        drawfacetex(points3);
        Point3D[] points4 = new Point3D[]{
                new Point3D(-100, 100,  100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(100, 100, -100), //br
                new Point3D( 100, 100, 100) //bl
        };
        drawfacetex(points4);
        Point3D[] points5 = new Point3D[]{
                new Point3D(-100, -100,  100), //tl
                new Point3D(-100, -100, -100), //bl
                new Point3D(100, -100, -100), //br
                new Point3D( 100, -100, 100) //bl
        };
        drawfacetex(points5);

        BufferedImage out = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < getHeight()-1; y++) {
            for (int x = 0; x < getWidth()-1; x++) {
                out.setRGB(x,y,(int)FrameBuffer[y][x]);
            }
        }
        out.getGraphics().drawImage(out, 0, 0, null);
        g2d.drawImage(out, 0, 0, null);
    }
    public Window() {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setIgnoreRepaint(true);
        this.setTitle("Cube");
        this.setMinimumSize(new Dimension(800, 600));
        (new Thread() {
            public void run() {
                while (true) {
                    repaint();
                }
            }
        }).start();
    }
}
