package dvaScreen.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port, 50, ServerManager.getThisAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
