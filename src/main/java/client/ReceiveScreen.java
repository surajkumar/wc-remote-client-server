package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

class ReceiveScreen extends Thread {
    InputStream inputStream = null;
    Image image1 = null;
    private final ObjectInputStream cObjectInputStream = null;
    private JPanel cPanel = null;
    private final boolean continueLoop = true;

    public ReceiveScreen(InputStream in, JPanel p) {
        inputStream = in;
        cPanel = p;
    }

    public void run() {
        try {
            System.out.println("Waiting for picture");
            //Read screenshots of the client and then draw them
            while (continueLoop) {
             //   System.out.println("Drawing");


                Image image = ImageIO.read(ImageIO.createImageInputStream(inputStream));

                image = image.getScaledInstance(cPanel.getWidth(), cPanel.getHeight(), Image.SCALE_FAST);


                //Draw the received screenshots

                Graphics graphics = cPanel.getGraphics();
                graphics.drawImage(image, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);


            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
