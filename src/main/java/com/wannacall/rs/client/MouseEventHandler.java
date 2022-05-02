package com.wannacall.rs.client;

import io.vertx.core.buffer.Buffer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MouseEventHandler implements MouseMotionListener, MouseListener {
    private OutputStream output;
    private JPanel panel;

    public MouseEventHandler(Socket socket, JPanel panel) throws IOException {
        this.output = socket.getOutputStream();
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var x = e.getX();
        var y = e.getY();
        var button = e.getButton();

        int targetWidth = 3840;
        int targetHeight = 2160;
        int scaleX = targetWidth / panel.getWidth();
        int scaleY = targetHeight / panel.getHeight();

        try {
            output.write(Buffer
                    .buffer()
                    .appendInt(1)
                    .appendInt(x * scaleX)
                    .appendInt(y * scaleY)
                    .appendInt(button)
                    .getBytes());
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        var x = e.getX();
        var y = e.getY();
        var button = e.getButton();

        int targetWidth = 3840;
        int targetHeight = 2160;
        int scaleX = targetWidth / panel.getWidth();
        int scaleY = targetHeight / panel.getHeight();


        /*System.out.println("Width = " + panel.getWidth() + " Height = " + panel.getHeight());
        System.out.println("MouseX = " + x + ", MouseY = " + y);
        System.out.println("ScaleX = " + scaleX + "ScaleY = " + scaleY);
        System.out.println("SentX = " + (x * scaleX) + " Sent Y = " + (y * scaleY));*/

        try {

            output.write(Buffer
                    .buffer()
                    .appendInt(1)
                    .appendInt(x * scaleX)
                    .appendInt(y * scaleY)
                    .appendInt(button)
                    .getBytes());

            output.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            output.write(Buffer
                    .buffer()
                    .appendInt(2)
                    .appendInt(e.getButton())
                    .getBytes());
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
