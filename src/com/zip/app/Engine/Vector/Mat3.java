package com.zip.app.Engine.Vector;
import java.util.Locale;
public class Mat3 {
    public double mat[][] = new double[4][4];
    public Mat3() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mat[i][j] = 0.0f;
    }
    public Mat3(Vec3D v1, Vec3D v2, Vec3D v3) {
        mat[0][0] = v1.x;
        mat[0][1] = v1.y;
        mat[0][2] = v1.z;
        mat[1][0] = v2.x;
        mat[1][1] = v2.y;
        mat[1][2] = v2.z;
        mat[2][0] = v3.x;
        mat[2][1] = v3.y;
        mat[2][2] = v3.z;
    }
    public Mat3(Mat3 m) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mat[i][j] = m.mat[i][j];
    }
    public Mat3(double[] m) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mat[i][j] = m[i * 3 + j];
    }
    public Mat3(double[][] m) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mat[i][j] = m[i][j];
    }
    public Mat3(Mat4 m) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mat[i][j] = m.mat[i][j];
    }
    public Mat3 add(Mat3 m) {
        Mat3 hlp = new Mat3();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                hlp.mat[i][j] = mat[i][j] + m.mat[i][j];
        return hlp;
    }
    public Mat3 mul(double d) {
        Mat3 hlp = new Mat3();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                hlp.mat[i][j] = mat[i][j] * d;
        return hlp;
    }
    public Mat3 mul(Mat3 m) {
        Mat3 hlp = new Mat3();
        double sum;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                sum = 0.0f;
                for (int k = 0; k < 3; k++)
                    sum += mat[i][k] * m.mat[k][j];
                hlp.mat[i][j] = sum;
            }
        return hlp;
    }
    public Mat3 transpose() {
        Mat3 hlp = new Mat3();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                hlp.mat[i][j] = mat[j][i];
        return hlp;
    }
    public double det() {
        return  mat[0][0]*(mat[1][1]*mat[2][2] - mat[2][1]*mat[1][2]) -
                mat[0][1]*(mat[1][0]*mat[2][2] - mat[2][0]*mat[1][2]) +
                mat[0][2]*(mat[1][0]*mat[2][1] - mat[2][0]*mat[1][1]);
    }
    public Mat3 inverse() {
        Mat3 hlp = new Mat3();
        double det = 1.0/det();

        hlp.mat[0][0] = det*(mat[1][1]*mat[2][2] - mat[1][2]*mat[2][1]);
        hlp.mat[0][1] = det*(mat[0][2]*mat[2][1] - mat[0][1]*mat[2][2]);
        hlp.mat[0][2] = det*(mat[0][1]*mat[1][2] - mat[0][2]*mat[1][1]);

        hlp.mat[1][0] = det*(mat[1][2]*mat[2][0] - mat[1][0]*mat[2][2]);
        hlp.mat[1][1] = det*(mat[0][0]*mat[2][2] - mat[0][2]*mat[2][0]);
        hlp.mat[1][2] = det*(mat[0][2]*mat[1][0] - mat[0][0]*mat[1][2]);

        hlp.mat[2][0] = det*(mat[1][0]*mat[2][1] - mat[1][1]*mat[2][0]);
        hlp.mat[2][1] = det*(mat[0][1]*mat[2][0] - mat[0][0]*mat[2][1]);
        hlp.mat[2][2] = det*(mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0]);

        return hlp;
    }
    @Override
    public String toString() {
        return String.format(Locale.US, "{%4.1f,%4.1f,%4.1f},\n"+"{%4.1f,%4.1f,%4.1f},\n"+"{%4.1f,%4.1f,%4.1f}",
                mat[0][0], mat[0][1], mat[0][2],
                mat[1][0], mat[1][1], mat[1][2],
                mat[2][0], mat[2][1], mat[2][2]);
    }
    public String toString(String format) {
        return String.format(Locale.US, "{{"+format+","+format+","+format+"},"+
                        "{"+format+","+format+","+format+"},\n"+
                        "{"+format+","+format+","+format+"}\n",
                mat[0][0], mat[0][1], mat[0][2],
                mat[1][0], mat[1][1], mat[1][2],
                mat[2][0], mat[2][1], mat[2][2]);
    }
}