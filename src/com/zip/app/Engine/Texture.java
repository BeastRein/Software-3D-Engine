package com.zip.app.Engine;

import com.zip.app.Engine.Vector.Point3D;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Texture {
    public String filename;
    BufferedImage image;
    public Texture(String filename) {
        this.filename = filename;
        try {
            this.image = ImageIO.read(new File(filename));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public BufferedImage project(Point3D[] points, int width, int height) {
        try {
            BufferedImage texture = Pseudo3D.computeImage(image,
                    new Point2D.Double(points[0].x,points[0].y),
                    new Point2D.Double(points[1].x,points[1].y),
                    new Point2D.Double(points[2].x,points[2].y),
                    new Point2D.Double(points[3].x,points[3].y));
            return texture;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
