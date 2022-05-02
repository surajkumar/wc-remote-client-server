package client;

import client.actions.SendEvents;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class CreateFrame extends Thread {
    String width = "", height = "";
    private final JFrame frame = new JFrame();

    //JDesktopPane represents the main container that will contain all connected clients' screens

    private final JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private final JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
    private final JPanel cPanel = new JPanel();

    public CreateFrame(Socket cSocket, String width, String height) {

        this.width = width;
        this.height = height;
        this.cSocket = cSocket;
        start();
    }

    //Draw GUI per each connected client

    public void drawGUI() {
        frame.add(desktop, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Show thr frame in maximized state

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);        //CHECK THIS LINE
        frame.setVisible(true);
        interFrame.setLayout(new BorderLayout());
        interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
        interFrame.setSize(100, 100);
        desktop.add(interFrame);

        try {
            //Initially show the internal frame maximized
            interFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

        //This allows to handle KeyListener events
        cPanel.setFocusable(true);
        interFrame.setVisible(true);

    }

    public void run() {
        System.out.println("Create Frame Started");
        //Used to read screenshots
        InputStream in = null;
        //start drawing GUI
        drawGUI();

        try {
            in = cSocket.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Start receiving screenshots
        new Thread(new ReceiveScreen(in, cPanel)).start();
        //Start sending events to the client
        new SendEvents(cSocket, cPanel, width, height);
    }
}
