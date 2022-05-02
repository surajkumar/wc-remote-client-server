package com.wannacall.rs.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JPanel {
    private Socket socket;
    private JFrame frame = new JFrame();

    public Client() throws Exception {

        Thread.sleep(2000);

        new Thread(() -> {
            frame.setSize(1080, 720);
            frame.setLocationRelativeTo(null);
            frame.setBackground(Color.BLACK);
            frame.add(this, BorderLayout.CENTER);
            frame.setVisible(true);
        }).start();

        new Thread(() -> {
            try {
                socket = new Socket("localhost", 43594);
                socket.getOutputStream().write(0);

                if(socket.isConnected()) {
                    try {
                        var mouse = new MouseEventHandler(socket, this);
                        //addKeyListener(mouse);
                        addMouseListener(mouse);
                        addMouseMotionListener(mouse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                while(socket.isConnected()) {
                    Thread.sleep(10);
                    getGraphics().drawImage(ImageIO.read(socket.getInputStream()), 0, 0, getWidth(), getHeight(), this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void main(String[] args) throws Exception {
        new Client();
    }



}
