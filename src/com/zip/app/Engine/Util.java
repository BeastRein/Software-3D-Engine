package com.zip.app.Engine;

import com.zip.app.Engine.Vector.Point3D;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Util {
    public static Point3D rotate(Point3D pos, Point3D rot) {
        //3d point rotational math
        double Axx = cos(rot.z)*cos(rot.x);
        double Axy = cos(rot.z)*sin(rot.x)*sin(rot.y) - sin(rot.z)*cos(rot.y);
        double Axz = cos(rot.z)*sin(rot.x)*cos(rot.y) + sin(rot.z)*sin(rot.y);

        double Ayx = sin(rot.z)*cos(rot.x);
        double Ayy = sin(rot.z)*sin(rot.x)*sin(rot.y) + cos(rot.z)*cos(rot.y);
        double Ayz = sin(rot.z)*sin(rot.x)*cos(rot.y) - cos(rot.z)*sin(rot.y);

        double Azx = -sin(rot.x);
        double Azy = cos(rot.x)*sin(rot.y);
        double Azz = cos(rot.x)*cos(rot.y);

        //return a point containing the rotated coordinates
        return new Point3D(
                Axx*pos.x + Axy*pos.y + Axz*pos.z,
                Ayx*pos.x + Ayy*pos.y + Ayz*pos.z,
                Azx*pos.x + Azy*pos.y + Azz*pos.z
        );
    }
    static float area(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (float) Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }
    public static double[] createPlane(double x1, double x2, double x3, double y1, double y2, double y3, double z1, double z2, double z3) {
        double[] plane = new double[4];
        plane[0] = y1*(z2 -z3) + y2*(z3 -z1) + y3*(z1 -z2);
        plane[1] = z1*(x2 -x3) + z2*(x3 -x1) + z3*(x1 -x2);
        plane[2] = x1*(y2 -y3) + x2*(y3 -y1) + x3*(y1 -y2);
        plane[3] = -( x1*(y2*z3 -y3*z2) + x2*(y3*z1 -y1*z3) + x3*(y1*z2 -y2*z1));
        return plane;
    }
    public static double resolveZ(double x, double y, double[] plane) {
        if (plane[2] == 0) {
            return -1;//Double.MAX_VALUE;
        }
        return (- (plane[0] * x + plane[1] * y + plane[3]) / plane[2]);
    }
    public static boolean isInside(int x1, int y1, int x2, int y2, int x3, int y3, int x, int y) {
        float A = area(x1, y1, x2, y2, x3, y3);
        float A1 = area(x, y, x2, y2, x3, y3);
        float A2 = area(x1, y1, x, y, x3, y3);
        float A3 = area(x1, y1, x2, y2, x, y);
        return (A == A1 + A2 + A3);
    }
}
