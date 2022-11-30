package com.zip.app.engine;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements KeyListener {
    point[] camera = {
            new point(0,0,0),
            new point(0,0,0)
    };
    boolean[] keysDown = new boolean[256];
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
}
