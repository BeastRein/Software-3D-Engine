package com.zip.app.Engine.Vector;

public class Mat4Identity extends Mat4 {
    public Mat4Identity() {
        for (int i = 0; i < 4; i++)
            mat[i][i] = 1.0f;
    }
}