package client;

import java.net.Socket;

public class Client {
    private static final int PORT = 43594;

    public static void main(String[] args) {
        new Client().initialize();
    }

    public void initialize() {
        try {
            Socket socket = new Socket("localhost", PORT);
            Authenticate auth = new Authenticate(socket);
            auth.setSize(300, 80);
            auth.setLocation(500, 300);
            auth.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Started");
    }
}



