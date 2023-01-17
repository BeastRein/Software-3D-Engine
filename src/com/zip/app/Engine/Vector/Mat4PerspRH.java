package com.zip.app.Engine.Vector;

public class Mat4PerspRH extends Mat4Identity {
    public Mat4PerspRH(double alpha, double k, double zn, double zf) {
        double h = (1.0 / Math.tan(alpha / 2.0));
        double w = k * h;
        mat[0][0] = w;
        mat[1][1] = h;
        mat[2][2] = zf / (zn - zf);
        mat[3][2] = zn * zf / (zn - zf);
        mat[2][3] = -1.0;
        mat[3][3] = 0.0;
    }
}