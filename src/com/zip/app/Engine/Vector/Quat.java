package com.zip.app.Engine.Vector;
import java.util.Locale;
public class Quat {
    public double r, i, j, k;
    public Quat() {
        i = j = k = r = 0.0f;
    }
    public Quat(double r, double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.r = r;
    }
    public Quat(Quat q) {
        i = q.i;
        j = q.j;
        k = q.k;
        r = q.r;
    }
    public Quat add(Quat q) {
        return new Quat(r + q.r, i + q.i, j + q.j, k + q.k);
    }
    public Quat sub(Quat q) {
        return new Quat(r - q.r, i - q.i, j - q.j, k - q.k);
    }
    public Quat mul(double a) {
        return new Quat(a * r, a * i, a * j, a * k);
    }
    public Quat mulR(Quat q) {
        return new Quat(this.r * q.r - this.i * q.i - this.j * q.j - this.k
                * q.k, this.r * q.i + this.i * q.r + this.j * q.k - this.k
                * q.j, this.r * q.j - this.i * q.k + this.j * q.r + this.k
                * q.i, this.r * q.k + this.i * q.j - this.j * q.i + this.k
                * q.r);
    }
    public Quat mulL(Quat q) {
        return new Quat(q.r * this.r - q.i * this.i - q.j * this.j - q.k
                * this.k, q.r * this.i + q.i * this.r + q.j * this.k - q.k
                * this.j, q.r * this.j + q.j * this.r + q.k * this.i - q.i
                * this.k, q.r * this.k + q.k * this.r + q.i * this.j - q.j
                * this.i);
    }
    public Quat mul(Quat q) {
        return mulR(q);
    }
    public double norma() {
        return Math.sqrt(r * r + i * i + j * j + k * k);
    }
    public Quat inv() {
        double norm = this.norma();
        norm = norm * norm;
        if (norm > 0)
            return new Quat(this.r / norm, -this.i / norm, -this.j / norm,
                    -this.k / norm);
        else
            return new Quat(0, 0, 0, 0);
    }
    public Quat log() {
        if ((this.i == 0) && (this.i == 0) && (this.i == 0)) {
            if (r > 0)
                return new Quat(Math.log(r), 0, 0, 0);
            else if (r < 0)
                return new Quat(Math.log(-r), 1, 0, 0);
            else
                // error log(0)
                return new Quat();
        } else {
            double s = Math.sqrt(i * i + j * j + k * k);
            double a = Math.atan2(s, r) / s;
            return new Quat(Math.log(this.norma()), a * i, a * j, a * k);
        }
    }
    public Quat exp() {
        if ((this.i == 0) && (this.j == 0) && (this.k == 0))
            return new Quat(Math.exp(this.r), 0, 0, 0);
        else {
            double s = Math.sqrt(i * i + j * j + k * k);
            double cos = Math.cos(s);
            // double exp = Math.exp(r);
            s = Math.exp(r) * Math.sin(s) / s;
            return new Quat(Math.exp(r) * cos, s * i, s * j, s * k);
        }
    }
    public Quat neg() {
        return new Quat(-this.r, -this.i, -this.j, -this.k);
    }
    public double dot(Quat q) {
        return this.i * q.i + this.j * q.j + this.k * q.k + this.r * q.r;
    }
    public Quat renorm() {
        double norm = this.norma();
        if (norm > 0)
            return new Quat(r / norm, i / norm, j / norm, k / norm);
        else
            return new Quat(0, 0, 0, 0);
    }
    public Mat4 toRotationMatrix() {
        Mat4 res = new Mat4Identity();
        this.renorm();
        res.mat[0][0] = 1 - 2 * (j * j + k * k);
        res.mat[1][0] = 2 * (i * j - r * k);
        res.mat[2][0] = 2 * (r * j + i * k);

        res.mat[0][1] = 2 * (i * j + r * k);
        res.mat[1][1] = 1 - 2 * (i * i + k * k);
        res.mat[2][1] = 2 * (k * j - i * r);

        res.mat[0][2] = 2 * (i * k - r * j);
        res.mat[1][2] = 2 * (k * j + i * r);
        res.mat[2][2] = 1 - 2 * (i * i + j * j);
        return res;
    }
    public static Quat fromRotationMatrix(Mat4 mat) {
        double r, i, j, k;
        double diagonal = mat.mat[0][0] + mat.mat[1][1] + mat.mat[2][2];

        if (diagonal > 0.0f) {
            r = (0.5f * Math.sqrt(diagonal + mat.mat[3][3]));
            i = (mat.mat[2][1] - mat.mat[1][2]) / (4 * r);
            j = (mat.mat[0][2] - mat.mat[2][0]) / (4 * r);
            k = (mat.mat[1][0] - mat.mat[0][1]) / (4 * r);
        } else {
            int[] indices = { 1, 2, 0 };
            int a = 0, b, c;

            if (mat.mat[1][1] > mat.mat[0][0])
                a = 1;
            if (mat.mat[2][2] > mat.mat[a][a])
                a = 2;

            b = indices[a];
            c = indices[b];

            diagonal = mat.mat[a][a] - mat.mat[b][b] - mat.mat[c][c]
                    + mat.mat[3][3];
            r = (0.5f * Math.sqrt(diagonal));
            i = (mat.mat[a][b] + mat.mat[b][a]) / (4 * r);
            j = (mat.mat[a][c] + mat.mat[c][a]) / (4 * r);
            k = (mat.mat[b][c] - mat.mat[c][b]) / (4 * r);
        }

        return new Quat(r, i, j, k);
    }
    public static Quat fromEulerAngles(double a, double b, double c) {
        /*
         * Quat Qi = new Quat(Math.cos(a/2),Math.sin(a/2),0,0); Quat Qj = new
         * Quat(Math.cos(b/2),0,Math.sin(b/2),0); Quat Qk = new
         * Quat(Math.cos(c/2),0,0,Math.sin(c/2));
         */
        Quat Qi = Quat.fromEulerAngle(a, 1, 0, 0);
        Quat Qj = Quat.fromEulerAngle(b, 0, 1, 0);
        Quat Qk = Quat.fromEulerAngle(c, 0, 0, 1);

        return new Quat(Qk.mul(Qj).mul(Qi));

        // this.renorm();
    }
    public static Quat fromEulerAngle(double angle, double x, double y, double z) {
        return new Quat(Math.cos(angle / 2), Math.sin(angle / 2) * x,
                Math.sin(angle / 2) * y, Math.sin(angle / 2) * z);
    }
    public Point3D toEulerAngle() {
        double angle = 2 * Math.acos(r);
        double x = this.i;
        double y = this.j;
        double z = this.k;

        double s = Math.sqrt(x * x + y * y + z * z);
        if (s < 0.0001d)
            return new Point3D(angle, 1, 0, 0);
        return new Point3D(angle, x / s, y / s, z / s);
    }
    public Quat lerp(Quat q, double t) {
        if (t >= 1)
            return new Quat(q);
        else if (t <= 0)
            return new Quat(this);
        else
            return new Quat((this.mul(1 - t)).add(q.mul(t)));
    }
    public Quat slerp(Quat q, double t) {
        double c = this.dot(q);
        if (c > 1.0)
            c = 1.0;
        else if (c < -1.0)
            c = -1.0;
        double uhel = Math.acos(c);
        if (Math.abs(uhel) < 1.0e-5)
            return new Quat(this);
        double s = 1 / Math.sin(uhel);
        if (t >= 1)
            return new Quat(this);
        else if (t <= 0)
            return new Quat(q);
        else
            return new Quat(this.renorm().mul(Math.sin((1 - t) * uhel) * s)
                    .add(q.renorm().mul(Math.sin(t * uhel) * s))).renorm();
    }
    public Quat squad(Quat q, Quat q1, Quat q2, double t) {
        return new Quat(this.slerp(q, t).slerp(q1.slerp(q2, t),
                (double) (2 * t * (1 - t))));
    }
    private Quat quadrangle(Quat q1, Quat q2) {
        Quat s1 = this.inv().mul(q1);
        Quat s2 = this.inv().mul(q2);
        return new Quat((s1.log().add(s2.log()).mul(-1 / 4)).exp());
    }
    public Quat squad2(Quat q1, Quat q2, Quat q3, double t) {

        Quat s1 = this.quadrangle(q1, q2);
        Quat s2 = q2.quadrangle(this, q3);
        return new Quat(this.slerp(q2, t).slerp(s1.slerp(s2, t),
                (double) (2 * t * (1 - t))));
    }
    public String string() {
        return String.format("[%4.1f;%4.1f;%4.1f;%4.1f]", r, i, j, k);
    }
    @Override
    public String toString() {
        return String.format(Locale.US, "(%4.1f,%4.1f,%4.1f,%4.1f)",r,i,j,k);
    }
}