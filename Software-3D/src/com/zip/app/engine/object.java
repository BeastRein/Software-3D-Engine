package com.zip.app.engine;
import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class object {
    public point[] points;
    public point location;
    public point rotation;
    public int[][] lines;
    public int[][] faces;
    public point[] projected_points;
    public point[] rotated_points;
    point center;
    public object(point location, point rotation, point[] points, int[][] lines, int[][] faces) {
        //init object parts
        this.location = location;
        this.rotation = rotation;
        this.points = points;
        this.lines = lines;
        this.faces = faces;
        this.projected_points = this.points.clone();
        this.rotated_points = this.points.clone();
    }
    public void project(point[] camera, int width, int height) {
        //set center object
        this.center = new point((float)(width / 2), (float)(height / 2), 0);

        //loop all points and determine if the point should be visible
        for (var i = 0; i < points.length; i++) {
            //rotate and project the points
            point pos = Util.rotate(points[i], rotation);
            point point = new point(
                    (512 * (pos.x + location.x / 2)) / (pos.z + location.z),
                    (512 * (pos.y + location.y / 2)) / (pos.z + location.z),
                    (512 * (pos.z + location.z / 2)) / (pos.z + location.z)
            );
            //initially point is visible but conditions can make the point invisible
            point.visible = true;

            //hide point if it is outside the bounds of the screen
            if (point.x < -(width)) {
                point.visible = false;
            }
            if (point.x > (width)) {
                point.visible = false;
            }
            if (point.y < -(height)) {
                point.visible = false;
            }
            if (point.y > (height)) {
                point.visible = false;
            }
            //apply changes
            this.rotated_points[i] = pos;
            this.projected_points[i] = point;
        }
    }
    public void rotate(point rotation) {
        //add point to rotation
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
    }
    public void render(JFrame frame, Graphics2D graphic2d) {
        //draw points
        for (int i = 0; i < projected_points.length; i++) {
            point pnt = projected_points[i];
            point center = new point((float)(frame.getSize().width / 2), (float)(frame.getSize().height / 2), 0);
            if (pnt.visible && (rotated_points[i].z > 0)) {
                graphic2d.fillRect((int) (center.x + pnt.x), (int) (center.y + pnt.y), 3, 3);
            }
            //graphic2d.drawString(String.valueOf(rotated_points[i].z), (int) (center.x + pnt.x), (int) (center.y + pnt.y));
        }

        //draw lines
        for (int[] line : lines) {
            //initialize output array and get center point


            //get first and second points
            point pnt1 = projected_points[line[0]];
            point pnt2 = projected_points[line[1]];

            //only draw if point is visible
            if (pnt1.visible && pnt2.visible) {

                //Color oldColor = graphic2d.getColor();
                graphic2d.setColor(Color.RED);
                if (((rotated_points[line[0]].z+rotated_points[line[1]].z)/2 > 0)) {
                    graphic2d.drawLine((int) (center.x + pnt1.x), (int) (center.y + pnt1.y), (int) (center.x + pnt2.x), (int) (center.y + pnt2.y));
                }
                //graphic2d.setColor(oldColor);
            }
            //graphic2d.drawString("Z: " + (rotated_points[line[0]].z + rotated_points[line[1]].z) / 2, (int) (center.x + ((pnt1.x + pnt2.x) / 2)), (int) (center.y + ((pnt1.y + pnt2.y) / 2)));
        }

        //create color map
        Color[] colors = {
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE,
                Color.PINK,
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE,
                Color.PINK,
        };

        //get average z axis for each polygon
        int[][] out = new int[faces.length][4];
        for (int i = 0; i < faces.length; i++) {
            int x = 0;
            int y = 0;
            int z = 0;
            for (int i1 = 0; i1 < faces.length; i1++) {
                x += projected_points[faces[i][i1]].x;
                y += projected_points[faces[i][i1]].y;
                z += projected_points[faces[i][i1]].z;
            }
            out[i][0] = x / faces.length;
            out[i][1] = y / faces.length;
            out[i][2] = z / faces.length;
            out[i][3] = i;
        }
        //sort the array of indexes by the z axis
        java.util.Arrays.sort(out, Comparator.comparingInt(a -> a[2]));

        //use the sorted array to order the actual array of faces / polygons
        int[][] faces1 = new int[faces.length][faces.length];
        for (int i = 0; i < faces.length; i++) {
            faces1[i] = faces[(faces.length - 1) - out[i][3]];
            colors[i] = colors[(colors.length - 1) - out[i][3]];
        }

        //loop the sorted polygons (WIP)
        for (int i = 0; i < faces1.length; i++) {
            //create the x and y-axis for the polygons
            int[] polyX = new int[faces1[i].length];
            int[] polyY = new int[faces1[i].length];

            //load the polygon coordinates into the axis arrays
            for (int j = 0; j < faces1[i].length; j++) {
                polyX[j] = (int) (center.x + projected_points[faces1[i][j]].x);
                polyY[j] = (int) (center.y + projected_points[faces1[i][j]].y);
            }

            //initialize and draw the polygon
            Polygon poly = new Polygon(polyX, polyY, polyX.length);
            graphic2d.setColor(colors[i]);
            graphic2d.fillPolygon(poly);
            graphic2d.setColor(Color.BLACK);
            //graphic2d.drawString(String.valueOf(out[i][2]), (int) (center.x + out[i][0]), (int) (center.y + out[i][1]));
        }
    }
}