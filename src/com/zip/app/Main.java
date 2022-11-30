package com.zip.app;

import com.zip.app.engine.object;
import com.zip.app.engine.point;
import static com.zip.app.engine.Util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame implements KeyListener {
    private BufferStrategy bufferStrategy;
    public int fps_count = 0;
    public int fps = 0;
    point[] camera = {
            new point(10,10,10),
            new point(0,0,0)
    };
    object obj = Loader("cube.json", new point(-1024,0,0));
    object obj1 = Loader("cube.json", new point(1024,0,0));
    object obj2 = Loader("cube.json", new point(0,0,1024));
    object obj3 = Loader("cube.json", new point(0,0,-1024));
    object obj4 = Loader("cube.json", new point(-1024,0,-1024));
    object obj5 = Loader("cube.json", new point(-1024,0, 1024));
    object obj6 = Loader("cube.json", new point(1024,0, -1024));
    object obj7 = Loader("cube.json", new point(1024,0,  1024));
    @Override
    public void paint(Graphics g) {
        //initialize dual buffer and graphics
        if (bufferStrategy == null) {
            this.createBufferStrategy(2);
            this.bufferStrategy = this.getBufferStrategy();
        }
        BufferStrategy bufferStrategy = getBufferStrategy();
        Graphics2D graphic2d = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphic2d.setColor(Color.BLACK);

        //clear screen and set draw color
        graphic2d.fillRect(0,0,this.getWidth(), this.getHeight());
        graphic2d.setColor(Color.WHITE);

        //rotate cube for testing
        obj.rotate(new point(0.001,0,0));

        //load all objects that are in front of the camera into an array
        com.zip.app.engine.object[] objl = {obj, obj1, obj2, obj3, obj4, obj5, obj6, obj7};
        object[] objs = new object[objl.length];
        for (int i = 0; i < objl.length; i++) {
            objs[i] = (object) Arrays.stream(objl).toArray()[i];
        }

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
        //apply frame buffer to window
        bufferStrategy.show();
        fps_count++;

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_LEFT) {
            camera[1].x += 0.05;
        } else if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
            camera[1].x -= 0.05;
        } else if(e.getKeyCode()== KeyEvent.VK_UP) {
            //move camera forward using sin and cos
            camera[0].x = camera[0].x + (5)*(Math.sin(camera[1].x));
            camera[0].z = camera[0].z - (5)*(Math.cos(camera[1].x));
        } else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
            //move camera backwards by reversing signs of forward
            camera[0].x = camera[0].x - (5)*(Math.sin(camera[1].x));
            camera[0].z = camera[0].z + (5)*(Math.cos(camera[1].x));
        } else if(e.getKeyCode()== KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void fpsCounter() {
        fps = fps_count;
        fps_count = 0;
    }
    Main() {
        //init main window
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setIgnoreRepaint(true);

        //main loop thread
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::repaint, 0, 1, TimeUnit.MILLISECONDS);
        //fps counter thread
        ScheduledExecutorService fps_thread = Executors.newScheduledThreadPool(1);
        fps_thread.scheduleAtFixedRate(this::fpsCounter, 0, 1, TimeUnit.SECONDS);
        //mouse control thread
        //ScheduledExecutorService mouse_thread = Executors.newScheduledThreadPool(1);
        //mouse_thread.scheduleAtFixedRate(this::mousehandler, 0, 1, TimeUnit.MILLISECONDS);

        //initialize key listener
        addKeyListener(this);
    }

    public static void main(String[] args) {
        //initialize main
        new Main();
    }
}