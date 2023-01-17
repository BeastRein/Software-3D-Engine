package com.zip.app.Engine.Vector;
import java.util.Locale;
public class Mat4 {
    public double mat[][] = new double[4][4];
    public Mat4() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                mat[i][j] = 0.0f;
    }
    public Mat4(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        mat[0][0] = p1.x;
        mat[0][1] = p1.y;
        mat[0][2] = p1.z;
        mat[0][3] = p1.w;
        mat[1][0] = p2.x;
        mat[1][1] = p2.y;
        mat[1][2] = p2.z;
        mat[1][3] = p2.w;
        mat[2][0] = p3.x;
        mat[2][1] = p3.y;
        mat[2][2] = p3.z;
        mat[2][3] = p3.w;
        mat[3][0] = p4.x;
        mat[3][1] = p4.y;
        mat[3][2] = p4.z;
        mat[3][3] = p4.w;
    }
    public Mat4(Mat4 m) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                mat[i][j] = m.mat[i][j];
    }
    public Mat4(double[] m) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                mat[i][j] = m[i * 4 + j];
    }
    public Mat4(double[][] m) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                mat[i][j] = m[i][j];
    }
    public Mat4 add(Mat4 m) {
        Mat4 hlp = new Mat4();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                hlp.mat[i][j] = mat[i][j] + m.mat[i][j];
        return hlp;
    }
    public Mat4 mul(double d) {
        Mat4 hlp = new Mat4();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                hlp.mat[i][j] = mat[i][j] * d;
        return hlp;
    }
    public Mat4 mul(Mat4 m) {
        Mat4 hlp = new Mat4();
        double sum;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                sum = 0.0f;
                for (int k = 0; k < 4; k++)
                    sum += mat[i][k] * m.mat[k][j];
                hlp.mat[i][j] = sum;
            }
        return hlp;
    }
    @Override
    public String toString() {
        return String.format(Locale.US, "{%4.1f,%4.1f,%4.1f,%4.1f},\n" + "{%4.1f,%4.1f,%4.1f,%4.1f},\n" + "{%4.1f,%4.1f,%4.1f,%4.1f},\n" + "{%4.1f,%4.1f,%4.1f,%4.1f}",
                mat[0][0], mat[0][1], mat[0][2], mat[0][3],
                mat[1][0], mat[1][1], mat[1][2], mat[1][3],
                mat[2][0], mat[2][1], mat[2][2], mat[2][3],
                mat[3][0], mat[3][1], mat[3][2], mat[3][3]);
    }
    public String toString(String format) {
        return String.format(Locale.US, "{{" + format + "," + format + "," + format + "," + format + "}," +
                        "{" + format + "," + format + "," + format + "," + format + "},\n" +
                        "{" + format + "," + format + "," + format + "," + format + "},\n" +
                        "{" + format + "," + format + "," + format + "," + format + "}\n",
                mat[0][0], mat[0][1], mat[0][2], mat[0][3],
                mat[1][0], mat[1][1], mat[1][2], mat[1][3],
                mat[2][0], mat[2][1], mat[2][2], mat[2][3],
                mat[3][0], mat[3][1], mat[3][2], mat[3][3]);
    }
    public String string() {
        return String.format("[" + new Point3D(mat[0]).toString() + ";"
                + new Point3D(mat[1]).toString() + ";"
                + new Point3D(mat[2]).toString() + ";"
                + new Point3D(mat[3]).toString() + "]");
    }
    public String string(String format) {
        return String.format("[" + new Point3D(mat[0]).toString(format) + ";"
                + new Point3D(mat[1]).toString(format) + ";"
                + new Point3D(mat[2]).toString(format) + ";"
                + new Point3D(mat[3]).toString(format) + "]");
    }
    public String string2() {
        return String.format("\n|"
                + String.format("%4.1f;%4.1f;%4.1f;%4.1f", mat[0][0],
                mat[0][1], mat[0][2], mat[0][3])
                + "|\n|"
                + String.format("%4.1f;%4.1f;%4.1f;%4.1f", mat[1][0],
                mat[1][1], mat[1][2], mat[1][3])
                + "|\n|"
                + String.format("%4.1f;%4.1f;%4.1f;%4.1f", mat[2][0],
                mat[2][1], mat[2][2], mat[2][3])
                + "|\n|"
                + String.format("%4.1f;%4.1f;%4.1f;%4.1f", mat[3][0],
                mat[3][1], mat[3][2], mat[3][3]) + "|\n\n");
    }
    public String string2(String format) {
        return String.format("\n|"
                + String.format(format + ";" + format + ";" + format + ";"
                + format, mat[0][0], mat[0][1], mat[0][2], mat[0][3])
                + "|\n|"
                + String.format(format + ";" + format + ";" + format + ";"
                + format, mat[1][0], mat[1][1], mat[1][2], mat[1][3])
                + "|\n|"
                + String.format(format + ";" + format + ";" + format + ";"
                + format, mat[2][0], mat[2][1], mat[2][2], mat[2][3])
                + "|\n|"
                + String.format(format + ";" + format + ";" + format + ";"
                + format, mat[3][0], mat[3][1], mat[3][2], mat[3][3])
                + "|\n\n");
    }
}