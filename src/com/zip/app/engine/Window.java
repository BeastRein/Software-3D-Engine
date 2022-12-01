package com.zip.app.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zip.app.engine.Util.Loader;
import static com.zip.app.engine.Util.rotate;

public class Window extends JFrame implements KeyListener {
    boolean[] keysDown = new boolean[256];
    private BufferStrategy bufferStrategy;
    public int fps_count = 0;
    public int fps = 0;
    long last_time = 0;
    object obj = Loader("cube.json", new point(-1024,0,0));
    object obj1 = Loader("cube.json", new point(-512,0,0));
    object obj2 = Loader("cube.json", new point(0,0,1024));
    object obj3 = Loader("cube.json", new point(0,0,-1024));
    object obj4 = Loader("cube.json", new point(-1024,0,-1024));
    object obj5 = Loader("cube.json", new point(-1024,0, 1024));
    object obj6 = Loader("cube.json", new point(1024,0, -1024));
    object obj7 = Loader("cube.json", new point(1024,0,  1024));
    point[] camera = {
            new point(0,0,0),
            new point(0,0,0)
    };

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keysDown[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysDown[e.getKeyCode()] = false;
    }
    @Override
    public void paint(Graphics g) {
        //initialize dual buffer and graphics
        if (bufferStrategy == null) {
            this.createBufferStrategy(2);
            this.bufferStrategy = this.getBufferStrategy();
        }
        //BufferedImage bimg = new BufferedImage(getWidth()+512, getHeight()+512,1);
        //Graphics2D graphic2d = bimg.createGraphics();

        //BufferStrategy bufferStrategy = getBufferStrategy();
        Graphics2D graphic2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        //Graphics2D graphicsOutput = (Graphics2D) g;
        graphic2d.setColor(Color.BLACK);

        //clear screen and set draw color
        graphic2d.fillRect(0,0,this.getWidth()+512, this.getHeight()+512);
        graphic2d.setColor(Color.WHITE);

        //rotate cube for testing
        obj.rotate(new point(0.001,0,0));

        //load all objects that are in front of the camera into an array
        object[] objs = {obj, obj1, obj2, obj3, obj4, obj5, obj6, obj7};

        //get the sizes of the different parts of the object
        int point_len = 0;
        int line_len = 0;
        for (com.zip.app.engine.object object : objs) {
            point_len += object.projected_points.length;
            line_len += object.lines.length+2;
        }

        //set up render output arrays
        point[] render_points = new point[point_len];
        int[][] render_lines = new int[line_len][2];

        //combine the coordinates of the points of each object and the object coordinates
        for (int i = 0; i < objs.length; i++) {
            for (int i1 = 0; i1 < objs[i].projected_points.length; i1++) {
                point rot = rotate(objs[i].points[i1], objs[i].rotation);
                render_points[(i*objs[i].projected_points.length) + i1] = new point(
                        (rot.x + objs[i].location.x) + camera[0].x,
                        (rot.y + objs[i].location.y) + camera[0].y,
                        (rot.z + objs[i].location.z) + camera[0].z
                );
            }
        }

        //adjust the line indexes for the larger array
        for (int i = 0; i < objs.length; i++) {
            for (int i1 = 0; i1 < objs[i].lines.length; i1++) {
                render_lines[(i*objs[i].lines.length) + i1][0] = (i*objs[i].projected_points.length) + objs[i].lines[i1][0];
                render_lines[(i*objs[i].lines.length) + i1][1] = (i*objs[i].projected_points.length) + objs[i].lines[i1][1];
            }
        }

        //create one big object out of the other objects that allows for easy rotation around the camera
        object render_obj = new object(new point(0,0,0), new point(0,0,0), render_points, render_lines, new int[][] {});

        //set rotation with camera and render main object
        render_obj.rotation = camera[1];
        render_obj.project(camera, getWidth(), getHeight());
        render_obj.render(this, graphic2d);

        //draw camera info for debugging
        graphic2d.setColor(Color.white);
        graphic2d.drawString("X: " + (float)Math.round(camera[0].x*10)/10 + ", Y: " + (float)Math.round(camera[0].y*10)/10 + ", Z: " + (float)Math.round(camera[0].z*10)/10, 20,120);

        //draw fps counter
        graphic2d.setFont(new Font("Roberto", Font.BOLD, 20));
        graphic2d.drawString("FPS: " + fps, 20,100);

        //draw blockout border for debugging
        graphic2d.setColor(Color.PINK);
        //top margin 50
        //bottom margin 30
        //left & right margin 20
        graphic2d.drawLine(20,50,(int)(getWidth()-20),50);
        graphic2d.drawLine(20,getHeight()-30,(int)(getWidth()-20),getHeight()-30);
        graphic2d.drawLine(20,50,20,getHeight()-30);
        graphic2d.drawLine(getWidth()-20,50,getWidth()-20,getHeight()-30);
        //apply frame buffer to window
        graphic2d.dispose();
        //graphicsOutput.drawImage(bimg.getSubimage(256, 256, getWidth(),getHeight()), 0, 0, null);
        bufferStrategy.show();
        fps_count++;

    }

    public void fpsCounter() {
        fps = fps_count;
        fps_count = 0;
    }
    public void loop() {
        if (System.currentTimeMillis() > last_time + 1000) {
            last_time = System.currentTimeMillis();
            fpsCounter();
        }
        if (keysDown[KeyEvent.VK_UP]) {
            camera[0].x = camera[0].x + (2)*(Math.sin(camera[1].x));
            camera[0].z = camera[0].z - (2)*(Math.cos(camera[1].x));
        }
        if (keysDown[KeyEvent.VK_DOWN]) {
            camera[0].x = camera[0].x - (2)*(Math.sin(camera[1].x));
            camera[0].z = camera[0].z + (2)*(Math.cos(camera[1].x));
        }
        if (keysDown[KeyEvent.VK_LEFT]) {
            camera[1].x += 0.002;
        }
        if (keysDown[KeyEvent.VK_RIGHT]) {
            camera[1].x -= 0.002;
        }
        repaint();
    }
    public Window() {
        //init main window
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setIgnoreRepaint(true);

        //main loop thread
        ScheduledExecutorService main_thread = Executors.newScheduledThreadPool(1);
        main_thread.scheduleAtFixedRate(this::loop, 0, 1, TimeUnit.MILLISECONDS);

        addKeyListener(this);
    }
}
