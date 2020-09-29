package dvaTest.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server {

    public static int PORT = 3456;

    public ServerSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT, 50, InetAddress.getLocalHost());
    }
}
