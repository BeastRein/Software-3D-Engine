package com.zip.app.Engine;

public class Block {
    public Point3D location = new Point3D(0,0,0);
    public int type;
    Point3D[][] faces = new Point3D[6][4];
    public Block(int x, int y, int z, int type) {
        this.location.x = x;
        this.location.y = y;
        this.location.z = z;
        this.type = type;
        this.faces[0] = new Point3D[]{
                new Point3D(-100, -100, -100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(100, 100, -100), //br
                new Point3D(100, -100, -100) //bl
        };
        this.faces[1] = new Point3D[]{
                new Point3D(-100, -100, 100), //tl
                new Point3D(-100, 100, 100), //bl
                new Point3D(100, 100, 100), //br
                new Point3D(100, -100, 100) //bl
        };
        this.faces[2] = new Point3D[]{
                new Point3D(100, -100, -100), //tl
                new Point3D(100, 100, -100), //bl
                new Point3D(100, 100, 100), //br
                new Point3D(100, -100, 100) //bl
        };
        this.faces[3] = new Point3D[]{
                new Point3D(-100, -100, -100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(-100, 100, 100), //br
                new Point3D(-100, -100, 100) //bl
        };
        this.faces[4] = new Point3D[]{
                new Point3D(-100, 100,  100), //tl
                new Point3D(-100, 100, -100), //bl
                new Point3D(100, 100, -100), //br
                new Point3D( 100, 100, 100) //bl
        };
        this.faces[5] = new Point3D[]{
                new Point3D(-100, -100,  100), //tl
                new Point3D(-100, -100, -100), //bl
                new Point3D(100, -100, -100), //br
                new Point3D( 100, -100, 100) //bl
        };
    }
}
